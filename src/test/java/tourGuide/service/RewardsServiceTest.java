package tourGuide.service;


import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.RewardCentral;
import tourGuide.model.User;
import tourGuide.model.UserReward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {


    @Mock
    private GpsUtil gpsUtil;
    @Mock
    private RewardCentral rewardCentral;
    @Spy
    @InjectMocks
    private RewardsService rewardsService;


    @Test
    @DisplayName("should return the user rewards")
    public void getUserRewardsTest() {

        // Arrange
        UUID id = new UUID(12312, 12312);
        User user = new User(id, "a", "a", "a");
        VisitedLocation vl = new VisitedLocation(
                new UUID(12312, 12312),
                new Location(10,20),
                new Date());
        Attraction attraction = new Attraction("a", "a", "a", 10, 10);
        UserReward userReward = new UserReward(vl, attraction, 1);
        List<UserReward> rewards = new ArrayList<>();
        rewards.add(userReward);
        user.addUserReward(userReward);

        // Act
        List<UserReward> returnedRewards = rewardsService.getUserRewards(user);

        // Assert
        assertEquals(rewards, returnedRewards);
    }


    @Test
    @DisplayName("calculate the rewards for a user")
    public void calculateRewardsTest() {

        // Arrange
        User user = new User(UUID.randomUUID(), "userTest", "a", "a");
        VisitedLocation location1 = new VisitedLocation(user.getUserId(), new Location(33.817595, -117.922008), new Date());
        VisitedLocation location2 = new VisitedLocation(user.getUserId(), new Location(4.1381, 118.3533), new Date());
        user.addToVisitedLocations(location1);
        user.addToVisitedLocations(location2);
        Attraction attraction1 = new Attraction("Attraction 1", "a", "a", 33.817595, -117.922008);
        Attraction attraction2 = new Attraction("Attraction 2", "a", "a", 4.1381, 118.3533);
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        doReturn(attractions).when(rewardsService).getAttractions();
        doReturn(5).when(rewardsService).getRewardPoints(attraction1, user);
        doReturn(10).when(rewardsService).getRewardPoints(attraction2, user);

        // Act
        rewardsService.calculateRewards(user);
        verify(gpsUtil, times(1)).getAttractions();

        // Assert
        List<UserReward> rewards = user.getUserRewards();
        assertEquals(2, rewards.size());
        assertEquals("Attraction 1", rewards.get(0).attraction.attractionName);
        assertEquals(5, rewards.get(0).getRewardPoints());
        assertEquals("Attraction 2", rewards.get(1).attraction.attractionName);
        assertEquals(10, rewards.get(1).getRewardPoints());
    }






    @Test
    @DisplayName("should return true when proximity buffer")
    public void nearAttractionTest() {

        // Arrange
        Location location = new Location(48.8584, 2.2945);
        VisitedLocation vl = new VisitedLocation(new UUID(12312, 12312), location, new Date());
        Attraction attraction = new Attraction("Eiffel Tower", "a", "a",48.8584, 2.2945);
        rewardsService.setProximityBuffer(50);
        doReturn(50.0).when(rewardsService).getDistance(attraction, location);

        // Act
        boolean result = rewardsService.nearAttraction(vl, attraction);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("should return false when not in proximity buffer")
    public void nearAttractionNegativeTest() {

        // Arrange
        Location location = new Location(48.8584, 2.2945);
        VisitedLocation vl = new VisitedLocation(new UUID(12312, 12312), location, new Date());
        Attraction attraction = new Attraction("Eiffel Tower", "a", "a",48.8584, 2.2945);
        rewardsService.setProximityBuffer(49);
        doReturn(50.0).when(rewardsService).getDistance(attraction, location);

        // Act
        boolean result = rewardsService.nearAttraction(vl, attraction);

        // Assert
        assertFalse(result);
    }


    @Test
    @DisplayName("should return the number of reward points for an attraction")
    public void getRewardPointsTest() {

        // Arrange
        Attraction attraction = new Attraction("Eiffel Tower", "a", "a",48.8584, 2.2945);
        UUID idAttraction = attraction.attractionId;
        UUID idUser = new UUID(12312, 12312);
        User user = new User(idUser, "a", "a", "a");
        int expectedPoints = 50;
        when(rewardCentral.getAttractionRewardPoints(idAttraction, idUser)).thenReturn(expectedPoints);

        // Act
        int result = rewardsService.getRewardPoints(attraction, user);

        // Assert
        assertEquals(expectedPoints, result);
    }


    @Test
    @DisplayName("should return the distance between two locations")
    public void getDistanceTest() {

        // Arrange
        double latitude1 = 50.0;
        double longitude1 = 20.0;
        Location location1 = new Location(latitude1, longitude1);

        double latitude2 = 60.0;
        double longitude2 = 30.0;
        Location location2 = new Location(latitude2, longitude2);

        double expectedDistance = 794.03;

        // Act
        double result = rewardsService.getDistance(location1, location2);

        // Assert
        assertEquals(expectedDistance, result, 0.01);
    }
}
