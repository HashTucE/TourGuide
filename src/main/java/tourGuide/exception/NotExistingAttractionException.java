package tourGuide.exception;

public class NotExistingAttractionException extends Exception{


    /**
     * Defines a custom exception named NotExistingAttractionException.
     * Indicates that an attraction with the given name does not exist returning a string.
     * @param attractionName String
     */
    public NotExistingAttractionException(String attractionName) {

        super(attractionName + " does not exist");
    }
}
