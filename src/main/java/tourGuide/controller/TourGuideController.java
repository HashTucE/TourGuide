package tourGuide.controller;

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




    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    

    

    @RequestMapping("/getNearbyAttractions") 
    public List<ClosestAttractionDto> getNearbyAttractions(@RequestParam String userName) throws NotExistingUserException {
    	return tourGuideService.getClosestAttractionDtoList(userName);
    }


    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName, @RequestParam String attractionName) throws NotExistingAttractionException, NotExistingUserException {
    	return tourGuideService.getTripDeals(userName, attractionName);
    }
}