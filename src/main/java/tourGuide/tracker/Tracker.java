package tourGuide.tracker;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.model.User;
import tourGuide.repository.UserRepository;
import tourGuide.service.LocationService;
import tourGuide.service.UserService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tracker extends Thread {
	private final Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	@Autowired
	private UserService userService;
	@Autowired
	private LocationService locationService;

	private boolean stop = false;

	public Tracker(UserRepository userRepository) {

		executorService.submit(this);
	}


	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}


	/**
	 * Tracks the location of all users in the system,
	 * until the thread is interrupted or the stop variable is set to true.
	 * Enters an infinite loop, where it repeatedly performs the following steps:
	 * It breaks out of the loop if the current thread has been interrupted or the stop variable is set to true.
	 * It retrieves a list of all users from the userService.
	 * It starts the stopwatch and iterates over the list of users to tracks their location.
	 * It stops the stopwatch and logs the elapsed time in seconds.
	 * It reset the stopWatch
	 * It put the thread to sleep for a given interval.
	 */
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}
			List<User> users = userService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			users.forEach(u -> locationService.trackUserLocation(u));
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
