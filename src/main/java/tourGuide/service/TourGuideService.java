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


	/**
	 * Retrieves an attraction by its name from a list of attractions.
	 * First, the method gets a list of attractions.
	 * Then it uses a for-each loop to iterate through the list of attractions.
	 * If a match is found, the method returns the attraction object.
	 * Otherwise, the method throws a NotExistingAttractionException.
	 * @param attractionName name of attraction
	 * @return Attraction
	 * @throws NotExistingAttractionException with attractionName
	 */
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


	/**
	 * Retrieves a list of trip deals for a given user and attraction.
	 * First uses the "userName" parameter to get the user object.
	 * Then it uses the "attractionName" parameter to get the attraction object.
	 * It then calculates the cumulative rewards points of the user.
	 * Then it returns a list of providers that have the trip deals available with them.
	 * Finally, it sets the returned list of providers as the user's "tripDeals"
	 * and returns the same list of providers.
	 * @param userName name of user
	 * @param attractionName name of attraction
	 * @return List<Provider>
	 * @throws NotExistingAttractionException with wrong attractionName
	 * @throws NotExistingUserException with wrong userName
	 */
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
		// filter providers by price range
		List<Provider> filteredProviders = providers.stream()
				.filter(provider -> provider.price >= user.getUserPreferences().getLowerPricePoint().getNumber().doubleValue()
						&& provider.price <= user.getUserPreferences().getHighPricePoint().getNumber().doubleValue())
				.collect(Collectors.toList());

		user.setTripDeals(filteredProviders);
		return filteredProviders;
	}


	/**
	 * Retrieves a list of nearby attractions for a given location.
	 * First, it gets a list of all available attractions.
	 * It then uses a parallel stream to iterate through the list of attractions,
	 * sorting them based on the distance from the visited location.
	 * It then limits to 5 attractions and collects the results into a list.
	 * @param visitedLocation VisitedLocation
	 * @return List<Attraction>
	 */
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {

		return gpsUtil.getAttractions().parallelStream()
				.sorted((a1, a2) -> Double.compare(
						rewardsService.getDistance(visitedLocation.location, a1),
						rewardsService.getDistance(visitedLocation.location, a2)))
				.limit(5)
				.collect(Collectors.toList());
	}


	/**
	 * Retrieves a list of the closest attraction details to the user based on their last visited location.
	 * The method starts getting the user object and the last visited location of this user.
	 * It uses this user location to get a list of the closest attractions.
	 * It then creates two lists, an closestAttractions list of 5 attractions
	 * and another list closestAttractionDtoList which stores the ClosestAttractionDto objects.
	 * Then it uses a for-each loop to iterate through the list of the closest attractions
	 * and for each attraction it creates a new ClosestAttractionDto object and adds it to the closestAttractionDtoList.
	 * @param userName String
	 * @return List<ClosestAttractionDto>
	 * @throws NotExistingUserException with wrong userName
	 */
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
