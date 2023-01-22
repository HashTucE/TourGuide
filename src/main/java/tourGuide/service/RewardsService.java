package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.model.User;
import tourGuide.model.UserReward;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RewardsService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
	private int proximityBuffer = 10;

	private final RewardCentral rewardsCentral;
	private final List<Attraction> attractions;
	private final ExecutorService executorService = Executors.newFixedThreadPool(70);
	private static final Object globalLock = new Object();

	private static final Logger log = LogManager.getLogger(RewardsService.class);

	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.rewardsCentral = rewardCentral;
		attractions = gpsUtil.getAttractions();
	}


	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}



	/**
	 * Retrieves a list of rewards associated with a given user.
	 * @param user User
	 * @return List<UserReward>
	 */
	public List<UserReward> getUserRewards(User user) {

		log.info("List of UserReward returned by getUserRewards with " + user.getUserName());
		return user.getUserRewards();
	}


	/**
	 * This method exist only because when attractions was mocker into test, empty list was returned
	 * @return attraction list
	 */
	public List<Attraction> getAttractions() {return attractions;}


	/**
	 * Calculates rewards for a given user based on their visited locations.
	 * If the user hasn't been rewarded for visiting the attraction, it checks whether
	 * the VisitedLocation is close enough to the Attraction and returns a boolean.
	 * If the VisitedLocation is close enough to the Attraction, the code calls the
	 * getRewardPoints(attraction, user) method, which calculates the number
	 * of reward points for the user, and then adds a new UserReward object to the
	 * user's list of rewards.
	 * @param user User
	 */
	public void calculateRewards(User user) {

		// Create a list to store the future tasks
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (Attraction attraction : getAttractions()) {
			// For each attraction, create a future task to calculate the user's rewards for that attraction
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				for (VisitedLocation visitedLocation : user.getVisitedLocations()) {
					if(user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
						if (nearAttraction(visitedLocation, attraction)) {
							int points = getRewardPoints(attraction, user);
							// Synchronize the user object using the global lock
							synchronized (globalLock) {
								user.addUserReward(new UserReward(visitedLocation, attraction, points));
								log.info("Rewards of " + user.getUserName() + " calculated by calculateRewards");
							}
						}
					}
				}
			}, executorService);
			futures.add(future);
		}
		// Wait for all the future tasks to complete
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

	}



	/**
	 * Checks whether a given VisitedLocation is close enough to a given Attraction.
	 * Returns true if the location is close enough to the attraction.
	 * @param visitedLocation VisitedLocation
	 * @param attraction Attraction
	 * @return boolean
	 */
	public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {

		log.info("nearAttraction called with " + attraction.attractionName);

		return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
	}


	/**
	 * Calculates the number of reward points for a user based on their visit to a given attraction.
	 * @param attraction Attraction
	 * @param user User
	 * @return int RewardPoints
	 */
	public int getRewardPoints(Attraction attraction, User user) {

		log.info("getRewardPoints called with " + attraction.attractionName + " and " + user.getUserName());
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}


	/**
	 * Calculates the distance between two locations in miles.
	 * First it converts the latitude and longitude of both locations from degrees to radians.
	 * calculates the angle between the two locations using the haversine formula,
	 * which is an equation giving great-circle distances between two points on a sphere
	 * from their longitudes and latitudes.
	 * @param loc1 Location
	 * @param loc2 Location
	 * @return distance
	 */
	public double getDistance(Location loc1, Location loc2) {

		log.info("getDistance called");
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;

	}


	/**
	 * Call getAttractionRewardPoints from RewardsService
	 * Calculate the reward points from an attraction
	 * @param attractionId id of the attraction
	 * @param userId id of the user
	 * @return reward points
	 */
	public int getAttractionRewardPoints(UUID attractionId, UUID userId) {

		log.info("getAttractionRewardPoints called from RewardsService");
		return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
	}
}
