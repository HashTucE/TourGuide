package tourGuide.controller;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger log = LogManager.getLogger(LocationController.class);

    /**
     * Handles a GET http request to retrieve the last location
     * of a user, it maps the request to the "/getLocation" endpoint.
     * @param userName String
     * @return Location
     * @throws NotExistingUserException with wrong userName
     */
    @RequestMapping("/getLocation")
    public Location getLocation(@RequestParam String userName) throws NotExistingUserException {

        log.info("GET request received to /getLocation");
        VisitedLocation visitedLocation = locationService.getUserLocation(userService.getUser(userName));
        return visitedLocation.location;
    }


    /**
     * Handles a GET http request to retrieve the current location
     * of all the users, it maps the request to the "/getAllCurrentLocations" endpoint.
     * @return Map<String, Location>
     */
    @RequestMapping("/getAllCurrentLocations")
    public Map<String, Location> getAllCurrentLocations() {

        log.info("GET request received to /getAllCurrentLocations");
        return locationService.getAllCurrentLocations();
    }
}
