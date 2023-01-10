package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.UserReward;
import tourGuide.service.RewardsService;
import tourGuide.service.UserService;

import java.util.List;

@RestController
public class RewardsController {


    @Autowired
    private RewardsService rewardsService;
    @Autowired
    private UserService userService;


    /**
     * Handles a GET http request to retrieve the rewards
     * of a specific user, it maps the request to the "/getRewards" endpoint.
     * @param userName String
     * @return List<UserReward>
     * @throws NotExistingUserException with wrong userName
     */
    @RequestMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) throws NotExistingUserException {

        return rewardsService.getUserRewards(userService.getUser(userName));
    }
}
