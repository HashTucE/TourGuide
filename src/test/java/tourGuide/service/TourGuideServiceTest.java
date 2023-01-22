package tourGuide.service;


import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.dto.ClosestAttractionDto;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {


    @Mock
    private TripPricer tripPricer;
    @Mock
    private GpsUtil gpsUtil;
    @Mock
    private RewardsService rewardsService;
    @Mock
    private UserService userService;
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
    public void getTripDeals_WhenValidInputs_ShouldReturnFilteredProviders() throws NotExistingAttractionException, NotExistingUserException {

        // Arrange
        String userName = "testUser";
        String attractionName = "testAttraction";
        User user = new User(UUID.randomUUID(), userName, "p", "e");
        Money low = Money.of(new BigDecimal(110), "USD");
        Money high = Money.of(new BigDecimal(130), "USD");
        user.setUserPreferences(new UserPreferences(low, high, 1, 1, 0));
        Attraction attraction = new Attraction(attractionName, "testCity", "testState", 0, 0);
        List<Provider> providers = List.of(
                new Provider(attraction.attractionId, "Provider1", 150),
                new Provider(attraction.attractionId, "Provider2", 120),
                new Provider(attraction.attractionId, "Provider3", 180),
                new Provider(attraction.attractionId, "Provider4", 210),
                new Provider(attraction.attractionId, "Provider5", 250));


        Provider expected = new Provider(attraction.attractionId, "Provider2", 120);

        when(userService.getUser(userName)).thenReturn(user);
        doReturn(attraction).when(tourGuideService).getAttractionByName(attractionName);
        when(tripPricer.getPrice(anyString(), any(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(providers);

        // Act
        List<Provider> filteredProviders = tourGuideService.getTripDeals(userName, attractionName);
        Provider actual = filteredProviders.get(0);

        // Assert
        assertEquals(expected.name, actual.name);
        assertEquals(expected.price, actual.price);
    }


    @Test
    public void getTripDeals_WhenInvalidAttraction_ShouldThrowNotExistingAttractionException() throws NotExistingUserException, NotExistingAttractionException {

        // Arrange
        String userName = "testUser";
        String attractionName = "testAttraction";
        User user = new User(UUID.randomUUID(), userName, "p", "e");
        when(userService.getUser(userName)).thenReturn(user);
        doThrow(NotExistingAttractionException.class).when(tourGuideService).getAttractionByName(attractionName);

        // Act
        // Assert
        assertThrows(NotExistingAttractionException.class, () -> tourGuideService.getTripDeals(userName, attractionName));
    }


    @Test
    public void getTripDeals_WhenInvalidUser_ShouldThrowNotExistingUserException() throws NotExistingUserException {

        // Arrange
        String userName = "testUser";
        String attractionName = "testAttraction";
        doThrow(NotExistingUserException.class).when(userService).getUser(userName);

        // Act
        // Assert
        assertThrows(NotExistingUserException.class, () -> tourGuideService.getTripDeals(userName, attractionName));
    }




    @Test
    public void testGetNearByAttractions_validVisitedLocation_returnsSortedAttractions() {

        // Arrange
        Attraction attraction1 = new Attraction("attraction1", "a", "a", 0.0, 0.0);
        Attraction attraction2 = new Attraction("attraction2", "a", "a", 0.0, 0.0);
        Attraction attraction3 = new Attraction("attraction3", "a", "a", 0.0, 0.0);
        Attraction attraction4 = new Attraction("attraction4", "a", "a", 0.0, 0.0);
        Attraction attraction5 = new Attraction("attraction5", "a", "a", 0.0, 0.0);
        Attraction attraction6 = new Attraction("attraction6", "a", "a", 0.0, 0.0);
        List<Attraction> attractions = Arrays.asList(attraction1, attraction2, attraction3, attraction4, attraction5, attraction6);
        VisitedLocation visitedLocation = new VisitedLocation(
                new UUID(1231, 1231),
                new Location(0.0,0.0),
                new Date());
        when(rewardsService.getDistance(visitedLocation.location, attraction1)).thenReturn(20.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction2)).thenReturn(10.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction3)).thenReturn(0.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction4)).thenReturn(50.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction5)).thenReturn(100.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction6)).thenReturn(90.0);
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        List<Attraction> result = tourGuideService.getNearByAttractions(visitedLocation);

        // Assert
        assertEquals(5, result.size());
        assertEquals("attraction3", result.get(0).attractionName);
        assertEquals("attraction2", result.get(1).attractionName);
        assertEquals("attraction1", result.get(2).attractionName);
        assertEquals("attraction4", result.get(3).attractionName);
        assertEquals("attraction6", result.get(4).attractionName);
    }


    @Test
    public void testGetClosestAttractionDtoList_validInput() throws NotExistingUserException {

        // Arrange
        User user = new User(UUID.randomUUID(), "userTest", "a", "a");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(0.0,0.0), new Date());
        user.addToVisitedLocations(visitedLocation);

        Attraction attraction1 = new Attraction("attraction1", "a", "a", 0.0, 0.0);
        Attraction attraction2 = new Attraction("attraction2", "a", "a", 0.0, 0.0);
        Attraction attraction3 = new Attraction("attraction3", "a", "a", 0.0, 0.0);
        List<Attraction> nearbyAttractions = Arrays.asList(attraction1, attraction2, attraction3);

        when(userService.getUser(user.getUserName())).thenReturn(user);
        doReturn(nearbyAttractions).when(tourGuideService).getNearByAttractions(user.getLastVisitedLocation());
        when(rewardsService.getDistance(visitedLocation.location, attraction1)).thenReturn(10.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction2)).thenReturn(20.0);
        when(rewardsService.getDistance(visitedLocation.location, attraction3)).thenReturn(30.0);
        when(rewardsService.getAttractionRewardPoints(attraction1.attractionId, user.getUserId())).thenReturn(10);
        when(rewardsService.getAttractionRewardPoints(attraction2.attractionId, user.getUserId())).thenReturn(20);
        when(rewardsService.getAttractionRewardPoints(attraction3.attractionId, user.getUserId())).thenReturn(30);

        // Act
        List<ClosestAttractionDto> closestAttractionDtoList = tourGuideService.getClosestAttractionDtoList(user.getUserName());

        // Assert
        assertNotNull(closestAttractionDtoList);
        assertEquals(3, closestAttractionDtoList.size());
        assertEquals("attraction1", closestAttractionDtoList.get(0).getAttractionName());
        assertEquals("attraction2", closestAttractionDtoList.get(1).getAttractionName());
        assertEquals("attraction3", closestAttractionDtoList.get(2).getAttractionName());
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
