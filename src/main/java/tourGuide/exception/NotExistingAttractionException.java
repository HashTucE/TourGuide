package tourGuide.exception;

public class NotExistingAttractionException extends Exception{


    public NotExistingAttractionException(String attractionName) {

        super(attractionName + " does not exist");
    }
}
