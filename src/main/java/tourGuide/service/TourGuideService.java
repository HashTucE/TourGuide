package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.dto.ClosestAttractionDto;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class TourGuideService {



	private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardCentral;
	private final RewardsService rewardsService;
	private final UserService userService;
	private final TripPricer tripPricer = new TripPricer();
	private static final String tripPricerApiKey = "test-server-api-key";




	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, UserService userService, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		this.userService = userService;
		this.rewardCentral = rewardCentral;
	}



	public Attraction getAttractionByName(String attractionName) throws NotExistingAttractionException {

		List<Attraction> attractions = gpsUtil.getAttractions();

		for (Attraction attraction : attractions) {
			if (attraction.attractionName.equals(attractionName)) {
				System.out.println(attractionName + " found successfully");
				return attraction;
			}
		}
		System.out.println("NotExistingAttraction");
		throw new NotExistingAttractionException(attractionName);
	}

	
	public List<Provider> getTripDeals(String userName, String attractionName) throws NotExistingAttractionException, NotExistingUserException {

		User user = userService.getUser(userName);
		Attraction attraction = getAttractionByName(attractionName);
		int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey,
														attraction.attractionId,
														user.getUserPreferences().getNumberOfAdults(),
														user.getUserPreferences().getNumberOfChildren(),
														user.getUserPreferences().getTripDuration(),
														cumulativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}




	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {

		return gpsUtil.getAttractions().parallelStream()
				.sorted((a1, a2) -> Double.compare(
						rewardsService.getDistance(visitedLocation.location, a1),
						rewardsService.getDistance(visitedLocation.location, a2)))
				.limit(5)
				.collect(Collectors.toList());
	}



	public List<ClosestAttractionDto> getClosestAttractionDtoList(String userName) throws NotExistingUserException {

		User user = userService.getUser(userName);
		Location userLocation = user.getLastVisitedLocation().location;
		List<Attraction> closestAttractions = new CopyOnWriteArrayList<>(getNearByAttractions(user.getLastVisitedLocation()));
		List<ClosestAttractionDto> closestAttractionDtoList = new ArrayList<>();

		for (Attraction attraction : closestAttractions) {
			closestAttractionDtoList.add(new ClosestAttractionDto(
					attraction.attractionName,
					new Location(attraction.latitude, attraction.longitude),
					userLocation,
					rewardsService.getDistance(userLocation, new Location(attraction.latitude, attraction.longitude)),
					rewardCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId())
					));
		}
		return closestAttractionDtoList;
	}
}
