package tourGuide.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.dto.ClosestAttractionDto;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.List;

@RestController
public class TourGuideController {

	@Autowired
	private TourGuideService tourGuideService;
    private static final Logger log = LogManager.getLogger(TourGuideController.class);


    /**
     * Handles a GET http request to display a string,
     * it maps the request to the "/" endpoint.
     * @return String
     */
    @RequestMapping("/")
    public String index() {

        log.info("GET request received to /");
        return "Greetings from TourGuide!";
    }


    /**
     * Handles a GET http request to retrieve the nearby attractions of a specific user,
     * it maps the request to the "/getNearbyAttractions" endpoint.
     * @param userName String
     * @return List<ClosestAttractionDto>
     * @throws NotExistingUserException with wrong userName
     */
    @RequestMapping("/getNearbyAttractions") 
    public List<ClosestAttractionDto> getNearbyAttractions(@RequestParam String userName) throws NotExistingUserException {

        log.info("GET request received to /getNearbyAttractions");
    	return tourGuideService.getClosestAttractionDtoList(userName);
    }


    /**
     * Handles a GET http request to retrieve the trip deals for a specific user and
     * attraction, it maps the request to the "/getTripDeals" endpoint.
     * @param userName String
     * @param attractionName String
     * @return List<Provider>
     * @throws NotExistingAttractionException with wrong attractionName
     * @throws NotExistingUserException with wrong userName
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName, @RequestParam String attractionName) throws NotExistingAttractionException, NotExistingUserException {

        log.info("GET request received to /getTripDeals");
    	return tourGuideService.getTripDeals(userName, attractionName);
    }
}