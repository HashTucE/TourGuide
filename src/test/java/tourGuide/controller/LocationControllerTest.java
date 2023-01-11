package tourGuide.controller;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.service.LocationService;
import tourGuide.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationControllerTest {


    @Mock
    private UserService userService;
    @Mock
    private LocationService locationService;
    @InjectMocks
    private LocationController locationController;

    @Test
    public void testGetLocation_Positive() throws NotExistingUserException {

        // Arrange
        UUID id = new UUID(12312, 12312);
        User user = new User(id, "a", "a", "a");
        VisitedLocation visitedLocation = new VisitedLocation(
                new UUID(12312, 12312),
                new Location(45.5231,-122.6765),
                new Date());
        user.addToVisitedLocations(visitedLocation);
        when(userService.getUser(user.getUserName())).thenReturn(user);
        when(locationService.getUserLocation(user)).thenReturn(visitedLocation);

        // Act
        Location result = locationController.getLocation(user.getUserName());

        // Assert
        assertEquals(user.getLastVisitedLocation().location, result);
    }


    @Test
    public void testGetLocation_Negative() throws NotExistingUserException {

        // Arrange
        String userName = "invalid";
        Mockito.when(userService.getUser(userName)).thenThrow(new NotExistingUserException(userName));

        // Act
        // Assert
        assertThrows(NotExistingUserException.class, () -> locationController.getLocation(userName));
    }


    @Test
    public void testGetAllCurrentLocations() {

        // Arrange
        UUID id = new UUID(12312, 12312);
        User user = new User(id, "a", "a", "a");
        VisitedLocation visitedLocation = new VisitedLocation(
                new UUID(12312, 12312),
                new Location(45.5231,-122.6765),
                new Date());
        user.addToVisitedLocations(visitedLocation);

        UUID id2 = new UUID(1231, 1231);
        User user2 = new User(id2, "b", "a", "a");
        VisitedLocation visitedLocation2 = new VisitedLocation(
                new UUID(1231, 1231),
                new Location(45.523,-122.676),
                new Date());
        user2.addToVisitedLocations(visitedLocation2);

        Map<String, Location> expectedLocations = new HashMap<>();
        expectedLocations.put(String.valueOf(user.getUserId()), user.getLastVisitedLocation().location);
        expectedLocations.put(String.valueOf(user2.getUserId()), user2.getLastVisitedLocation().location);
        Mockito.when(locationService.getAllCurrentLocations()).thenReturn(expectedLocations);

        // Act
        Map<String, Location> result = locationController.getAllCurrentLocations();

        // Assert
        assertEquals(expectedLocations, result);
    }
}
