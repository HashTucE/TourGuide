package tourGuide.repository;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.tracker.Tracker;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

@Repository
public class UserRepository {
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory


    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final Map<String, User> internalUserMap = new HashMap<>();

    public final Tracker tracker;
    boolean testMode = true;


    public UserRepository() {

    if(testMode) {
        logger.info("TestMode enabled");
        logger.debug("Initializing users");
        initializeInternalUsers();
        logger.debug("Finished initializing users");
    }
    tracker = new Tracker(this);
    addShutDownHook();
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(tracker::stopTracking));
    }


    /**
     * Initializes a number of internal users and stores them in a Map,
     * where the keys are the usernames and the values are the User objects.
     * @return Map<String, User>
     */
    public Map<String, User> initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
        return internalUserMap;
    }


    /**
     * Generates a location historic for a given user by adding 3 randomly generated
     * VisitedLocation objects to the user's list of visited locations.
     * @param user User
     */
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i-> user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(),
                                    new Location(generateRandomLatitude(),
                                    generateRandomLongitude()),
                                    getRandomTime())));
    }


    /**
     * Generates a random double value within a certain range,
     * which represents a longitude value.
     * @return double
     */
    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }


    /**
     * Generates a random double value within a certain range,
     * which represents a latitude value.
     * @return double
     */
    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }


    /**
     * Generates a random date-time within the past 30 days
     * and then converts it to a Date object.
     * @return Date
     */
    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
