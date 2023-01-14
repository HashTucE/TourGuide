package tourGuide;


import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.repository.UserRepository;
import tourGuide.service.LocationService;
import tourGuide.service.RewardsService;
import tourGuide.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest

public class TestPerformance {
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */


	@Autowired
	private GpsUtil gpsUtil;

	@Autowired
	private RewardCentral rewardCentral;

	@Autowired
	private RewardsService rewardsService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocationService locationService;



	@BeforeEach
	public void setDefaultLocale() {
		Locale.setDefault(Locale.UK);
	}




	@Test
	@DisplayName("Up to 100000 users track location tested in less than 15 minutes")
	public void highVolumeTrackLocation() {

		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);

		List<User> allUsers = userService.getAllUsers();

		// Create a list of CompletableFutures to track user locations
		List<CompletableFuture<VisitedLocation>> locationFutures = new ArrayList<>();
		for (User user : allUsers) {
			locationFutures.add(locationService.trackUserLocation(user));
		}

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// Wait for all user location tracking to complete
		CompletableFuture.allOf(locationFutures.toArray(new CompletableFuture[0])).join();

		stopWatch.stop();
		userRepository.tracker.stopTracking();

		System.out.println(locationFutures.size() + " users tracked correctly");
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

		// Assert that all futures are done
		locationFutures.forEach(future -> assertTrue(future.isDone()));
		// Assert that all futures return VisitedLocation objects
		locationFutures.forEach(future -> {try {assertTrue(future.get() instanceof VisitedLocation);}
											catch (InterruptedException | ExecutionException e) {throw new RuntimeException(e);}});
		// Assert that the number of results equals the number of users
		assertEquals(allUsers.size(), locationFutures.size());
		// Assert that all results are unique
		assertEquals(locationFutures.stream().map(CompletableFuture::join).distinct().count(), locationFutures.size());
		// Assert that the total time elapsed is less than 15 minutes
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}



	@Test
	@DisplayName("Up to 100000 users get rewards tested in less than 20 minutes")
	public void highVolumeGetRewards() {

		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = userService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

		allUsers.forEach(u -> rewardsService.calculateRewards(u));

		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();
		userRepository.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
}
