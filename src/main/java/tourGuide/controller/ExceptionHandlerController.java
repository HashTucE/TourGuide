package tourGuide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {


    /**
     * Handles the MethodArgumentNotValidException
     * that is thrown when the input validation fails.
     * It starts by create a list of field errors.
     * It creates a list of string to store the error messages.
     * Finally, it returns an instance of ResponseEntity containing the
     * list of error messages and a status code of bad request.
     * @param ex exception
     * @return ResponseEntity<List<String>>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles the NotExistingUserException
     * that is thrown when the user is not found in the system.
     * It returns an instance of ResponseEntity containing the exception message
     * and a status code of not found.
     * @param ex exception
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(NotExistingUserException.class)
    public ResponseEntity<String> handleNotExistingUserException(NotExistingUserException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    /**
     * Handles the NotExistingAttractionException
     * that is thrown when the attraction is not found in the system.
     * It returns an instance of ResponseEntity containing the exception message
     * and a status code of not found.
     * @param ex exception
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(NotExistingAttractionException.class)
    public ResponseEntity<String> handleNotExistingAttractionException(NotExistingAttractionException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}

