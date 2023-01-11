package tourGuide.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.User;
import tourGuide.tracker.Tracker;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {




    private Tracker tracker;
    @InjectMocks
    private UserRepository userRepository;



//    @Test
//    public void addShutDownHook_whenCalled_addsShutdownHook() {
//
//        // Arrange
//        // Act
//        userRepository.addShutDownHook();
//        // Assert
//        verify(tracker, times(1)).stopTracking();
//    }


//    @Test
//    public void addShutDownHook_whenCalled_notAddShutdownHook() {
//
//        // Arrange
//        // Act
//        userRepository.addShutDownHook();
//
//        // Assert
//        verify(tracker, never()).stopTracking();
//    }


//    @Test
//    public void initializeInternalUsers_returnsCorrectNumberOfInternalUsers(){
//
//        // Arrange
//        int expectedUserNumber = 3;
//        when(internalTestHelper.getInternalUserNumber()).thenReturn(expectedUserNumber);
//
//        // Act
//        Map<String, User> result = userRepository.initializeInternalUsers();
//
//        // Assert
//        assertEquals(expectedUserNumber, result.size());
//    }


//    @Test
//    public void initializeInternalUsers_internalHelperReturnInvalidNumber_throwsIllegalArgumentException(){
//
//        // Arrange
//        int expectedUserNumber = -3;
//        when(internalTestHelper.getInternalUserNumber()).thenReturn(expectedUserNumber);
//
//        // Act
//        // Assert
//        assertThrows(IllegalArgumentException.class, () -> userRepository.initializeInternalUsers());
//    }


    @Test
    public void generateUserLocationHistory_validUser_addsVisitedLocation(){
        // Arrange
        User user = new User(UUID.randomUUID(), "a", "a", "a");
        int expectedVisitedLocation = 3;

        // Act
        userRepository.generateUserLocationHistory(user);

        // Assert
        assertEquals(expectedVisitedLocation, user.getVisitedLocations().size());
    }



    @Test
    public void generateRandomLongitude_returnsRandomDoubleInRange() {

        // Act
        double result = userRepository.generateRandomLongitude();

        // Assert
        assertTrue(result >= -180 && result <= 180);
    }


    @Test
    public void generateRandomLongitude_returnsDifferentValueEveryTimeCalled(){

        // Act
        double firstResult = userRepository.generateRandomLongitude();
        double secondResult = userRepository.generateRandomLongitude();

        // Assert
        assertNotEquals(firstResult, secondResult);
    }


    @Test
    public void generateRandomLatitude_returnsRandomDoubleInRange() {

        // Act
        double result = userRepository.generateRandomLatitude();

        // Assert
        assertTrue(result >= -85.05112878 && result <= 85.05112878);
    }


    @Test
    public void generateRandomLatitude_returnsDifferentValueEveryTimeCalled(){

        // Act
        double firstResult = userRepository.generateRandomLatitude();
        double secondResult = userRepository.generateRandomLatitude();

        // Assert
        assertNotEquals(firstResult, secondResult);
    }


    @Test
    public void getRandomTime_returnsValidRandomDate() {

        // Arrange
        // Act
        Date randomDate = userRepository.getRandomTime();
        Date now = new Date();
        LocalDateTime time30daysago = LocalDateTime.now().minusDays(30);
        Date thirtyDaysAgo = Date.from(time30daysago.toInstant(ZoneOffset.UTC));

        // Assert
        assertTrue(randomDate.before(now) && randomDate.after(thirtyDaysAgo));
    }
}
