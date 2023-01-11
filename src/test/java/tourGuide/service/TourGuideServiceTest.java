package tourGuide.service;


import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.RewardCentral;
import tourGuide.dto.ClosestAttractionDto;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {


    @Mock
    private GpsUtil gpsUtil;
    @Mock
    private RewardsService rewardsService;
    @Mock
    private UserService userService;
    @Mock
    private RewardCentral rewardCentral;
    @Spy
    @InjectMocks
    private TourGuideService tourGuideService;




    @Test
    public void testGetAttractionByName_validAttractionName_returnsAttraction() throws NotExistingAttractionException {

        // Arrange
        List<Attraction> attractions = Arrays.asList(
                new Attraction("a", "a", "a", 10, 10),
                new Attraction("b", "a", "a", 1, 1),
                new Attraction("c", "a", "a", 100, 100));
        String attractionName = "a";
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        Attraction result = tourGuideService.getAttractionByName(attractionName);

        // Assert
        assertEquals(attractionName, result.attractionName);
    }


    @Test
    public void testGetAttractionByName_InvalidAttractionName_throwsNotExistingAttractionException() {

        // Arrange
        List<Attraction> attractions = Arrays.asList(
                new Attraction("a", "a", "a", 10, 10),
                new Attraction("b", "a", "a", 1, 1),
                new Attraction("c", "a", "a", 100, 100));
        String attractionName = "Invalid Attraction";
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        // Assert
        assertThrows(NotExistingAttractionException.class, () -> tourGuideService.getAttractionByName(attractionName));
    }


    @Test
    public void testGetNearByAttractions_validVisitedLocation_returnsSortedAttractions() {

        // Arrange
        Attraction attraction1 = new Attraction("a", "a", "a", 43.582767, -110.821999);
        Attraction attraction2 = new Attraction("b", "a", "a", 35.141689, -115.510399);
        Attraction attraction3 = new Attraction("c", "a", "a", 33.881866, -115.90065);
        Attraction attraction4 = new Attraction("d", "a", "a", 35.985512, -92.757652);
        Attraction attraction5 = new Attraction("e", "a", "a", 34.52153, -93.042267);
        Attraction attraction6 = new Attraction("f", "a", "a", 33.817595, -117.922008);
        List<Attraction> attractions = Arrays.asList(attraction1, attraction2, attraction3, attraction4, attraction5, attraction6);
//        Location location = new Location(0.0, 0.0);
        VisitedLocation visitedLocation = new VisitedLocation(
                new UUID(1231, 1231),
                new Location(30.0,30.0),
                new Date());
//        Mockito.lenient().when(rewardsService.getDistance(location, attraction1)).thenReturn(0.0);
//        Mockito.lenient().when(rewardsService.getDistance(location, attraction2)).thenReturn(10.0);
//        Mockito.lenient().when(rewardsService.getDistance(location, attraction3)).thenReturn(0.0);
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        List<Attraction> result = tourGuideService.getNearByAttractions(visitedLocation);

        // Assert
        assertEquals(5, result.size());
        assertEquals("a", result.get(0).attractionName);
        assertEquals("b", result.get(1).attractionName);
        assertEquals("c", result.get(2).attractionName);
    }


    @Test
    public void testGetClosestAttractionDtoList_validInput() throws NotExistingUserException {

        // Arrange
        Location userLocation = new Location(45.5231, -122.6765);
        UUID id = new UUID(12312, 12312);
        User user = new User(id, "John", "a", "a");
        VisitedLocation visitedLocation = new VisitedLocation(
                new UUID(12312, 12312),
                new Location(45.5231,-122.6765),
                new Date());
        user.addToVisitedLocations(visitedLocation);
        Attraction attraction1 = new Attraction("a", "a", "a", 45.5231, -122.6761);
        Attraction attraction2 = new Attraction("b", "a", "a", 45.5236, -122.6765);
        Attraction attraction3 = new Attraction("c", "a", "a", 45.5241, -122.6769);
        List<Attraction> nearbyAttractions = Arrays.asList(attraction1, attraction2, attraction3);

        when(userService.getUser(user.getUserName())).thenReturn(user);
        doReturn(nearbyAttractions).when(tourGuideService).getNearByAttractions(user.getLastVisitedLocation());
        Mockito.lenient().when(rewardsService.getDistance(userLocation, new Location(45.5231, -122.6761))).thenReturn(500.0);
        Mockito.lenient().when(rewardsService.getDistance(userLocation, new Location(45.5236, -122.6765))).thenReturn(200.0);
        Mockito.lenient().when(rewardsService.getDistance(userLocation, new Location(45.5241, -122.6769))).thenReturn(50.0);
        when(rewardCentral.getAttractionRewardPoints(attraction1.attractionId, id)).thenReturn(10);
        when(rewardCentral.getAttractionRewardPoints(attraction2.attractionId, id)).thenReturn(20);
        when(rewardCentral.getAttractionRewardPoints(attraction3.attractionId, id)).thenReturn(30);

        // Act
        List<ClosestAttractionDto> closestAttractionDtoList = tourGuideService.getClosestAttractionDtoList(user.getUserName());

        // Assert
        assertNotNull(closestAttractionDtoList);
        assertEquals(3, closestAttractionDtoList.size());
        assertEquals("a", closestAttractionDtoList.get(0).getAttractionName());
        assertEquals("b", closestAttractionDtoList.get(1).getAttractionName());
        assertEquals("c", closestAttractionDtoList.get(2).getAttractionName());
        assertEquals(user.getLastVisitedLocation().location, closestAttractionDtoList.get(0).getUserLocation());
//        assertEquals(50, closestAttractionDtoList.get(0).getDistance(), 0.1);
        assertEquals(10, closestAttractionDtoList.get(0).getRewardPoints());
    }


    @Test
    public void testGetClosestAttractionDtoList_invalidInput() throws NotExistingUserException {

        // Arrange
        String userName = "InvalidUser";
        when(userService.getUser(userName)).thenThrow(new NotExistingUserException(userName));

        // Act
        // Assert
        assertThrows(NotExistingUserException.class, () -> tourGuideService.getClosestAttractionDtoList(userName));
    }


}
