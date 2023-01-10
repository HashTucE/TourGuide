package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import tourGuide.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocationService {



    private final GpsUtil gpsUtil;
    private final UserService userService;
    private final RewardsService rewardsService;



    public LocationService(GpsUtil gpsUtil, UserService userService, RewardsService rewardsService) {
        this.gpsUtil = gpsUtil;
        this.userService = userService;
        this.rewardsService = rewardsService;
    }


    /**
     * Track the location of a user and add that location to a list of visited locations for that user.
     * It then calls a method to calculate rewards for the user.
     * @param user user
     * @return VisitedLocation
     */
    public VisitedLocation trackUserLocation(User user) {

        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);

        return visitedLocation;
    }


    /**
     * Retrieves the last visited location for a given user.
     * If the size of the list is greater than 0, it returns the last location visited.
     * If the list is empty, it  tracks the current location of the user.
     * @param user User
     * @return VisitedLocation
     */
    public VisitedLocation getUserLocation(User user) {

        return (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
    }


    /**
     * Retrieves the current locations of all users.
     * It first retrieves a list of all users.
     * Then it creates an empty ConcurrentHashMap named locations.
     * The for loop iterates through the list of users to retrieve the user's last visited location.
     * @return Map<String, Location>
     */
    public Map<String, Location> getAllCurrentLocations() {

        List<User> users = userService.getAllUsers();
        Map<String, Location> locations = new ConcurrentHashMap<>();

        for (User user : users) {
            locations.put(String.valueOf(user.getUserId()), user.getLastVisitedLocation().location);
        }

        return locations;
    }
}
