package tourGuide.controller;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.NotExistingUserException;
import tourGuide.service.LocationService;
import tourGuide.service.UserService;

import java.util.Map;

@RestController
public class LocationController {


    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;





    @RequestMapping("/getLocation")
    public Location getLocation(@RequestParam String userName) throws NotExistingUserException {
        VisitedLocation visitedLocation = locationService.getUserLocation(userService.getUser(userName));
        return visitedLocation.location;
    }


    @RequestMapping("/getAllCurrentLocations")
    public Map<String, Location> getAllCurrentLocations() {

        return locationService.getAllCurrentLocations();
    }
}
