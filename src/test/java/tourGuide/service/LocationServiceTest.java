package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {


    @Mock
    private GpsUtil gpsUtil;
    @Mock
    private RewardsService rewardsService;
    @Mock
    private ExecutorService executorService;
    @Mock
    private UserService userService;
    @InjectMocks
    private LocationService locationService;



//    @Test
//    public void testTrackUserLocation() {
//        // Arrange
//        UUID id = new UUID(12312, 12312);
//        User user = new User(id, "a", "a", "a");
//        VisitedLocation vl = new VisitedLocation(
//                new UUID(12312, 12312),
//                new Location(10,20),
//                new Date());
//        when(gpsUtil.getUserLocation(user.getUserId())).thenReturn(new VisitedLocation(id, vl.location, new Date()));
//
//        // Act
//        CompletableFuture<VisitedLocation> future = locationService.trackUserLocation(user);
//
//        // Assert
//        assertTrue(future.isDone());
//        assertTrue(future.join() instanceof VisitedLocation);
//        verify(gpsUtil, times(1)).getUserLocation(user.getUserId());
//        verify(rewardsService, times(1)).calculateRewards(user);
//    }


    @Test
    public void testGetUserLocation() {

        // Arrange
        UUID id = new UUID(12312, 12312);
        User user = new User(id, "a", "a", "a");
        VisitedLocation vl1 = new VisitedLocation(
                new UUID(12312, 12312),
                new Location(10,20),
                new Date());
        user.addToVisitedLocations(vl1);

        // Act
        VisitedLocation result = locationService.getUserLocation(user);

        // Assert
        assertEquals(20, result.location.longitude);
        assertEquals(10, result.location.latitude);
        assertNotNull(user.getVisitedLocations());
    }


    @Test
    public void testGetAllCurrentLocations() {

        // Arrange
        UUID id = new UUID(1231, 1231);
        User user = new User(id, "a", "a", "a");
        VisitedLocation vl = new VisitedLocation(
                new UUID(1231, 1231),
                new Location(10,20),
                new Date());
        user.addToVisitedLocations(vl);

        UUID id2 = new UUID(12312, 12312);
        User user2 = new User(id2, "a", "a", "a");
        VisitedLocation vl2 = new VisitedLocation(
                new UUID(12312, 12312),
                new Location(100,200),
                new Date());
        user2.addToVisitedLocations(vl2);

        List<User> users = Arrays.asList(user, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        Map<String, Location> result = locationService.getAllCurrentLocations();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsValue(vl.location));
        assertTrue(result.containsValue(vl2.location));
    }
}




