package tourGuide.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.UserPreferences;
import tourGuide.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;





    @GetMapping("/getPreferences")
    public UserPreferences getUserPreferences(@RequestParam String userName) throws NotExistingUserException {
        return userService.getUserPreferences(userName);
    }


    @PutMapping("/updatePreferences")
    public ResponseEntity<String> updateUsePreferences(
            @RequestParam String userName,
            @RequestBody @Valid UserPreferencesDto userPreferencesDto) throws NotExistingUserException {

        userService.updateUserPreferences(userName, userPreferencesDto);

        return new ResponseEntity<>("User preferences updated successfully", HttpStatus.OK);
    }
}
