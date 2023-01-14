package tourGuide.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.UserPreferences;
import tourGuide.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;




    @Test
    public void getUserPreferences_validInput_returnsUserPreferences() throws NotExistingUserException {

        // Arrange
        String userName = "john";
        UserPreferences expectedPreferences = new UserPreferences();
        when(userService.getUserPreferences(userName)).thenReturn(expectedPreferences);

        // Act
        UserPreferences result = userController.getUserPreferences(userName);

        // Assert
        assertEquals(expectedPreferences, result);
    }


    @Test
    public void getUserPreferences_invalidInput_throwsNotExistingUserException() throws NotExistingUserException {

        // Arrange
        String userName = "unknown";
        when(userService.getUserPreferences(userName)).thenThrow(new NotExistingUserException(userName));

        // Act
        // Assert
        assertThrows(NotExistingUserException.class, () -> userController.getUserPreferences(userName));
    }


    @Test
    public void updateUserPreferences_validInput_returnsSuccessResponse() throws NotExistingUserException {

        // Arrange
        String userName = "a";
        UserPreferencesDto userPreferencesDto = new UserPreferencesDto();

        // Act
        ResponseEntity<String> response = userController.updateUserPreferences(userName, userPreferencesDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User preferences updated successfully", response.getBody());
        verify(userService).updateUserPreferences(userName, userPreferencesDto);
    }


    @Test
    public void updateUserPreferences_invalidInput_throwsNotExistingUserException() throws NotExistingUserException {

        // Arrange
        String userName = "unknown";
        UserPreferencesDto userPreferencesDto = new UserPreferencesDto();
        doThrow(new NotExistingUserException(userName)).when(userService).updateUserPreferences(userName, userPreferencesDto);

        // Act
        // Assert
        assertThrows(NotExistingUserException.class, () -> userController.updateUserPreferences(userName, userPreferencesDto));
    }





}
