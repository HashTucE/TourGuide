package tourGuide.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {


    @InjectMocks
    private UserRepository userRepository;




    @Test
    @DisplayName("should assert size of visited location history equal 3")
    public void generateUserLocationHistoryTest() {

        // Arrange
        User user = new User(UUID.randomUUID(), "a", "a", "a");
        int expectedVisitedLocation = 3;

        // Act
        userRepository.generateUserLocationHistory(user);

        // Assert
        assertEquals(expectedVisitedLocation, user.getVisitedLocations().size());
    }



    @Test
    @DisplayName("should return a random longitude in range")
    public void generateRandomLongitudeTest() {

        // Act
        double result = userRepository.generateRandomLongitude();

        // Assert
        assertTrue(result >= -180 && result <= 180);
    }


    @Test
    @DisplayName("should return a different value every time called")
    public void generateRandomLongitudeTest2() {

        // Act
        double firstResult = userRepository.generateRandomLongitude();
        double secondResult = userRepository.generateRandomLongitude();

        // Assert
        assertNotEquals(firstResult, secondResult);
    }


    @Test
    @DisplayName("should return a random latitude in range")
    public void generateRandomLatitudeTest() {

        // Act
        double result = userRepository.generateRandomLatitude();

        // Assert
        assertTrue(result >= -85.05112878 && result <= 85.05112878);
    }


    @Test
    @DisplayName("should return a different value every time called")
    public void generateRandomLatitudeTest2() {

        // Act
        double firstResult = userRepository.generateRandomLatitude();
        double secondResult = userRepository.generateRandomLatitude();

        // Assert
        assertNotEquals(firstResult, secondResult);
    }


    @Test
    @DisplayName("should return valid random date")
    public void getRandomTimeTest() {

        // Arrange
        // Act
        Date randomDate = userRepository.getRandomTime();
        LocalDateTime time30daysago = LocalDateTime.now().minusDays(30);
        Date thirtyDaysAgo = Date.from(time30daysago.toInstant(ZoneOffset.UTC));

        // Assert
        assertTrue(randomDate.after(thirtyDaysAgo));
    }
}
