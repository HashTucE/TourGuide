package tourGuide.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tourGuide.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {


    private Map<String, User> usersMap;
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostConstruct
    private void init() {
        usersMap = userRepository.initializeInternalUsers();
    }


    public User getUser(String userName) throws NotExistingUserException {

        User user = usersMap.get(userName);
        if (user == null) {
            throw new NotExistingUserException(userName);
        }
        return user;
    }



    public List<User> getAllUsers() {

        List<User> allUsers = new ArrayList<>(usersMap.values());
        System.out.println("getAllUsers: allUsers = " + allUsers);
        return allUsers;
    }



    public void addUser(User user) {

        if(!usersMap.containsKey(user.getUserName())) {
            usersMap.put(user.getUserName(), user);
        }
    }



    public UserPreferences getUserPreferences(String userName) throws NotExistingUserException {

        return getUser(userName).getUserPreferences();
    }


    public void updateUserPreferences(String userName, UserPreferencesDto userPreferencesDto) throws NotExistingUserException {

        User user = getUser(userName);

        UserPreferences userPreferencesToUpdate = user.getUserPreferences();
        userPreferencesToUpdate.setTripDuration(userPreferencesDto.getTripDuration());
        userPreferencesToUpdate.setNumberOfAdults(userPreferencesDto.getNumberOfAdults());
        userPreferencesToUpdate.setNumberOfChildren(userPreferencesDto.getNumberOfChildren());
    }

}
