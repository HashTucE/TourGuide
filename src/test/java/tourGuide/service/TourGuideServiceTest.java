package tourGuide.service;


import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.exception.NotExistingAttractionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {


    @Mock
    private GpsUtil gpsUtil;
    @Mock
    private RewardsService rewardsService;
    @InjectMocks
    private TourGuideService tourGuideService;




    @Test
    public void testGetAttractionByName_validAttractionName_returnsAttraction() throws NotExistingAttractionException {

        // Arrange
        List<Attraction> attractions = Arrays.asList(
                new Attraction("Eiffel Tower", "a", "a", 10, 10),
                new Attraction("a", "a", "a", 1, 1),
                new Attraction("b", "b", "b", 100, 100));
        String attractionName = "Eiffel Tower";
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        Attraction result = tourGuideService.getAttractionByName(attractionName);

        // Assert
        assertEquals(attractionName, result.attractionName);
    }


    @Test
    public void testGetAttractionByName_InvalidAttractionName_throwsNotExistingAttractionException() {

        // Arrange
        List<Attraction> attractions = Arrays.asList(
                new Attraction("Eiffel Tower", "a", "a", 10, 10),
                new Attraction("a", "a", "a", 1, 1),
                new Attraction("b", "b", "b", 100, 100));
        String attractionName = "Invalid Attraction";
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        // Assert
        assertThrows(NotExistingAttractionException.class, () -> tourGuideService.getAttractionByName(attractionName));
    }


    @Test
    public void testGetNearByAttractions_validVisitedLocation_returnsSortedAttractions() {

        // Arrange
        Attraction attraction1 = new Attraction("1", "Jackson Hole", "WY", 43.582767, -110.821999);
        Attraction attraction2 = new Attraction("2", "Kelso", "CA", 35.141689, -115.510399);
        Attraction attraction3 = new Attraction("3", "Joshua Tree National Park", "CA", 33.881866, -115.90065);
        Attraction attraction4 = new Attraction("4", "St Joe", "AR", 35.985512, -92.757652);
        Attraction attraction5 = new Attraction("5", "Hot Springs", "AR", 34.52153, -93.042267);
        Attraction attraction6 = new Attraction("6", "Anaheim", "CA", 33.817595, -117.922008);
        List<Attraction> attractions = Arrays.asList(attraction1, attraction2, attraction3, attraction4, attraction5, attraction6);
//        Location location = new Location(0.0, 0.0);
        VisitedLocation visitedLocation = new VisitedLocation(
                new UUID(1231, 1231),
                new Location(30.0,30.0),
                new Date());
//        Mockito.lenient().when(rewardsService.getDistance(location, attraction1)).thenReturn(0.0);
//        Mockito.lenient().when(rewardsService.getDistance(location, attraction2)).thenReturn(10.0);
//        Mockito.lenient().when(rewardsService.getDistance(location, attraction3)).thenReturn(0.0);
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // Act
        List<Attraction> result = tourGuideService.getNearByAttractions(visitedLocation);

        // Assert
        assertEquals(5, result.size());
        assertEquals("1", result.get(0).attractionName);
        assertEquals("2", result.get(1).attractionName);
        assertEquals("3", result.get(2).attractionName);
    }

}
