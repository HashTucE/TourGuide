package tourGuide.service;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.exception.NotExistingUserException;
import tourGuide.model.User;
import tourGuide.model.UserPreferences;
import tourGuide.repository.UserRepository;

import javax.money.Monetary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {


    private Map<String, User> usersMap;
    private final UserRepository userRepository;

    private static final Logger log = LogManager.getLogger(UserService.class);


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostConstruct
    public void init() {
        usersMap = userRepository.getInternalUsersMap();
    }


    /**
     * Retrieves a user by their name.
     * The method starts by attempting to retrieve the user from a map object.
     * It then uses an if statement, if the user variable is null it throws a NotExistingUserException
     * Otherwise, the method returns the user object.
     * @param userName String
     * @return User
     * @throws NotExistingUserException with wrong userName
     */
    public User getUser(String userName) throws NotExistingUserException {

        User user = usersMap.get(userName);
        if (user == null) {
            log.error("getUser called with wrong userName");
            throw new NotExistingUserException(userName);
        }
        log.info(userName + " returned by getUser");
        return user;
    }


    /**
     * Retrieves a list of all users in the system.
     * The method creates an ArrayList initialized with the values of the usersMap object.
     * @return List<User>
     */
    public List<User> getAllUsers() {

        log.info("List of all users returned by getAllUsers");
        return new ArrayList<>(usersMap.values());
    }


    /**
     * Adds a new user to the system.
     * Checks if a user with the same name already exists.
     * If the user with the same name doesn't exist, it adds the user to the map.
     * @param user User
     */
    public void addUser(User user) {

        if(!usersMap.containsKey(user.getUserName())) {
            usersMap.put(user.getUserName(), user);
            log.info(user.getUserName() + " added from addUser");
        }
    }


    /**
     * Retrieves the user preferences for a given user.
     * @param userName String
     * @return UserPreferences
     * @throws NotExistingUserException with wrong userName
     */
    public UserPreferences getUserPreferences(String userName) throws NotExistingUserException {

        log.info(userName + "'s preferences returned from getUserPreferences");
        return getUser(userName).getUserPreferences();
    }


    /**
     * Updates the user preferences for a given user.
     * It retrieves the user preferences and assigns it to a variable.
     * Finally, it updates the user preferences.
     * @param userName String
     * @param userPreferencesDto UserPreferencesDto
     * @throws NotExistingUserException with wrong userName
     */
    public void updateUserPreferences(String userName, UserPreferencesDto userPreferencesDto) throws NotExistingUserException {

        User user = getUser(userName);

        UserPreferences userPreferencesToUpdate = user.getUserPreferences();
        userPreferencesToUpdate.setLowerPricePoint(Money.of(userPreferencesDto.getLowerPricePoint(), Monetary.getCurrency("USD")));
        userPreferencesToUpdate.setHighPricePoint(Money.of(userPreferencesDto.getHighPricePoint(), Monetary.getCurrency("USD")));
        userPreferencesToUpdate.setTripDuration(userPreferencesDto.getTripDuration());
        userPreferencesToUpdate.setNumberOfAdults(userPreferencesDto.getNumberOfAdults());
        userPreferencesToUpdate.setNumberOfChildren(userPreferencesDto.getNumberOfChildren());
        log.info(userName + "'s preferences returned from updateUserPreferences");
    }

}
