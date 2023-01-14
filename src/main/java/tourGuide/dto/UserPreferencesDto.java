package tourGuide.dto;


import jakarta.validation.constraints.Min;

public class UserPreferencesDto {


    @Min(value = 0, message = "lower price cannot be negative")
    private int lowerPricePoint;

    @Min(value = 0, message = "high price cannot be negative")
    private int highPricePoint;

    @Min(value = 1, message = "at least 1 night minimum")
    private int tripDuration;

    @Min(value = 1, message = "at least 1 adult minimum")
    private int numberOfAdults;

    @Min(value = 0, message = "children number cannot be negative")
    private int numberOfChildren;



    public int getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(int lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public int getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(int highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
