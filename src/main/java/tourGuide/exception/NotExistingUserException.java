package tourGuide.exception;

public class NotExistingUserException extends Exception{

    public NotExistingUserException(String userName) {

        super(userName + " does not exist");
    }
}
