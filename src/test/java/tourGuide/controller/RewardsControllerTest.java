package tourGuide.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.service.RewardsService;
import tourGuide.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsControllerTest {


    @Mock
    private RewardsService rewardsService;
    @Mock
    private UserService userService;
    @InjectMocks
    private RewardsController rewardsController;



    @Test
    public void testGetRewards_Positive() throws NotExistingUserException {

        // Arrange
        String userName = "a";
        UUID id = new UUID(12312, 12312);
        User user = new User(id, userName, "a", "a");
        List<UserReward> expectedRewards = new ArrayList<>();
        when(userService.getUser(userName)).thenReturn(user);
        when(rewardsService.getUserRewards(user)).thenReturn(expectedRewards);

        // Act
        List<UserReward> result = rewardsController.getRewards(userName);

        // Assert
        assertEquals(expectedRewards, result);
    }


    @Test
    public void testGetRewards_Negative() throws NotExistingUserException {

        // Arrange
        String userName = "a";
        Mockito.when(userService.getUser(userName)).thenThrow(new NotExistingUserException(userName));

        // Act
        assertThrows(NotExistingUserException.class, () -> rewardsController.getRewards(userName));
    }
}
