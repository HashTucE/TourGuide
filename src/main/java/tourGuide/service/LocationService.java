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


    public VisitedLocation trackUserLocation(User user) {

        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);

        return visitedLocation;
    }



    public VisitedLocation getUserLocation(User user) {

        return (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
    }



    public Map<String, Location> getAllCurrentLocations() {

        List<User> users = userService.getAllUsers();
        Map<String, Location> locations = new ConcurrentHashMap<>();

        for (User user : users) {
            locations.put(String.valueOf(user.getUserId()), user.getLastVisitedLocation().location);
        }

        return locations;
    }
}
