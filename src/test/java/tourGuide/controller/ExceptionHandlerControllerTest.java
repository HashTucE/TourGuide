package tourGuide.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerTest {



    @InjectMocks
    private ExceptionHandlerController ex;



    @Test
    public void handleValidationExceptions_ValidMethodArgumentNotValidException_ReturnsBadRequestWithErrorMessages() {

        //Arrange
        BindingResult bindingResult = new BindException(new Object(), "user");
        bindingResult.addError(new FieldError("user", "name", "name is required"));
        bindingResult.addError(new FieldError("user", "phone", "phone is required"));
        bindingResult.addError(new FieldError("user", "email", "email is required"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("name is required");
        errorMessages.add("phone is required");
        errorMessages.add("email is required");

        //Act
        ResponseEntity<List<String>> responseEntity = ex.handleValidationExceptions(exception);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessages, responseEntity.getBody());
    }


    @Test
    public void handleNotExistingUserException_validNotExistingUserException_returnsNotFound() {

        // Arrange
        NotExistingUserException exception = new NotExistingUserException("User");

        // Act
        ResponseEntity<String> responseEntity = ex.handleNotExistingUserException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User does not exist", responseEntity.getBody());
    }


    @Test
    public void handleNotExistingAttractionException_validNotExistingAttractionException_returnsNotFound() {

        // Arrange
        NotExistingAttractionException exception = new NotExistingAttractionException("Attraction");

        // Act
        ResponseEntity<String> responseEntity = ex.handleNotExistingAttractionException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Attraction does not exist", responseEntity.getBody());
    }





}
