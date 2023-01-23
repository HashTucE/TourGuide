package tourGuide.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tourGuide.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;



    @Test
    @DisplayName("should return a user when the input is valid")
    public void getUserTest() throws NotExistingUserException {

        // Arrange
        User user = new User(UUID.randomUUID(), "a", "a", "a");
        when(userRepository.getInternalUsersMap()).thenReturn(Map.of("a", user));
        userService.init();

        // Act
        User retrievedUser = userService.getUser("a");

        // Assert
        assertEquals(user, retrievedUser);
    }


    @Test
    @DisplayName("should throw exception when the input is invalid")
    public void getUserNegativeTest() {

        // Arrange
        when(userRepository.getInternalUsersMap()).thenReturn(new HashMap<>());
        userService.init();

        // Assert
        assertThrows(NotExistingUserException.class, () -> userService.getUser("a"));
    }


    @Test
    @DisplayName("should return a list of all users when there are users in the map")
    public void getAllUsersTest() {

        // Arrange
        User user1 = new User(UUID.randomUUID(), "a", "a", "a");
        User user2 = new User(UUID.randomUUID(), "b", "b", "b");
        User user3 = new User(UUID.randomUUID(), "c", "c", "c");
        Map<String, User> userMap = new HashMap<>();
        userMap.put(user1.getUserName(), user1);
        userMap.put(user2.getUserName(), user2);
        userMap.put(user3.getUserName(), user3);
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        List<User> allUsers = userService.getAllUsers();

        // Assert
        assertNotNull(allUsers);
        assertEquals(3, allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
        assertTrue(allUsers.contains(user3));
    }


    @Test
    @DisplayName("should return an empty list when there are no users in the map")
    public void getAllUsersNegativeTest() {
        // Arrange
        Map<String, User> userMap = new HashMap<>();
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        List<User> allUsers = userService.getAllUsers();

        // Assert
        assertNotNull(allUsers);
        assertTrue(allUsers.isEmpty());
    }


    @Test
    @DisplayName("should add user to the map when given a valid user")
    public void addUserTest() {

        // Arrange
        User user = new User(UUID.randomUUID(), "a", "a", "a");
        Map<String, User> userMap = new HashMap<>();
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        userService.addUser(user);

        // Assert
        assertTrue(userMap.containsKey("a"));
        assertEquals(user, userMap.get("a"));
    }


    @Test
    @DisplayName("should not add user to the map when given a user with duplicate username")
    public void addUserNegativeTest() {

        // Arrange
        User user1 = new User(UUID.randomUUID(), "a", "a", "a");
        User user2 = new User(UUID.randomUUID(), "a", "b", "b");
        Map<String, User> userMap = new HashMap<>();
        userMap.put(user1.getUserName(), user1);
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        userService.addUser(user2);

        // Assert
        assertEquals(1, userMap.size());
        assertEquals(user1, userMap.get("a"));
    }


    @Test
    @DisplayName("should return true if the user exists")
    public void getUserPreferencesTest() throws NotExistingUserException {
        // Arrange

        UserPreferences userPreferences = new UserPreferences();
        User user = new User(UUID.randomUUID(), "a", "a", "a");
        user.setUserPreferences(userPreferences);
        Map<String, User> userMap = new HashMap<>();
        userMap.put(user.getUserName(), user);
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        UserPreferences result = userService.getUserPreferences(user.getUserName());

        // Assert
        assertEquals(userPreferences, result);
    }


    @Test
    @DisplayName("should return false if the user does not exist")
    public void getUserPreferencesNegativeTest() {

        // Arrange
        Map<String, User> userMap = new HashMap<>();
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        // Assert
        assertThrows(NotExistingUserException.class,
                () -> userService.getUserPreferences("invalidUsername"));
    }


    @Test
    @DisplayName("should update user information")
    public void updateUserPreferencesTest() throws NotExistingUserException {

        // Arrange
        User user = new User(UUID.randomUUID(), "a", "a", "a");
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setTripDuration(2);
        userPreferences.setNumberOfAdults(1);
        userPreferences.setNumberOfChildren(0);
        user.setUserPreferences(userPreferences);

        Map<String, User> userMap = new HashMap<>();
        userMap.put(user.getUserName(), user);

        UserPreferencesDto userPreferencesDto = new UserPreferencesDto();
        userPreferencesDto.setTripDuration(4);
        userPreferencesDto.setNumberOfAdults(2);
        userPreferencesDto.setNumberOfChildren(1);

        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        userService.updateUserPreferences(user.getUserName(), userPreferencesDto);

        // Assert
        assertEquals(4, user.getUserPreferences().getTripDuration());
        assertEquals(2, user.getUserPreferences().getNumberOfAdults());
        assertEquals(1, user.getUserPreferences().getNumberOfChildren());
    }


    @Test
    @DisplayName("should throw exception when updating a non-existing user")
    public void updateUserPreferencesNegativeTest() {

        // Arrange
        UserPreferencesDto userPreferencesDto = new UserPreferencesDto();
        Map<String, User> userMap = new HashMap<>();
        when(userRepository.getInternalUsersMap()).thenReturn(userMap);
        userService.init();

        // Act
        // Assert
        assertThrows(NotExistingUserException.class,
                () -> userService.updateUserPreferences("invalidUsername", userPreferencesDto));
    }




}
