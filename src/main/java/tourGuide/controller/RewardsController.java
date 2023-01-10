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




    @RequestMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) throws NotExistingUserException {

        return rewardsService.getUserRewards(userService.getUser(userName));
    }
}
