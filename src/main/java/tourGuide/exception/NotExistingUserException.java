package tourGuide.exception;

public class NotExistingUserException extends Exception{

    /**
     * Defines a custom exception named NotExistingUserException.
     * Indicates that a user with the given name does not exist returning a string.
     * @param userName String
     */
    public NotExistingUserException(String userName) {

        super(userName + " does not exist");
    }
}
