package tourGuide.controller;

import gpsUtil.location.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.dto.ClosestAttractionDto;
import tourGuide.exception.NotExistingAttractionException;
import tourGuide.exception.NotExistingUserException;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourGuideControllerTest {


    @Mock
    private TourGuideService tourGuideService;
    @InjectMocks
    private TourGuideController tourGuideController;




    @Test
    @DisplayName("should assert equal when compare to same message")
    public void indextest() {

        // Arrange
        String expectedResponse = "Greetings from TourGuide!";

        // Act
        String result = tourGuideController.index();

        // Assert
        assertEquals(expectedResponse, result);
    }


    @Test
    @DisplayName("should assert not equal when compare to other message")
    public void indexTest2() {

        // Arrange
        String expectedResponse = "Welcome to TourGuide!";

        // Act
        String result = tourGuideController.index();

        // Assert
        assertNotEquals(expectedResponse, result);
    }


    @Test
    @DisplayName("should assert equal when compare to other message")
    public void testGetNearbyAttractions_Positive() throws NotExistingUserException {

        // Arrange
        String userName = "john";
        List<ClosestAttractionDto> expectedAttractions = new ArrayList<>();
        ClosestAttractionDto closestAttractionDto1 = new ClosestAttractionDto("a", new Location(1.0,1.0),
                        new Location(1.0,1.0), 10.0, 1);
        ClosestAttractionDto closestAttractionDto2 = new ClosestAttractionDto("b", new Location(1.0,1.0),
                        new Location(1.0,1.0), 10.0, 1);
        expectedAttractions.add(closestAttractionDto1);
        expectedAttractions.add(closestAttractionDto2);

        when(tourGuideService.getClosestAttractionDtoList(userName)).thenReturn(expectedAttractions);

        // Act
        List<ClosestAttractionDto> result = tourGuideController.getNearbyAttractions(userName);

        // Assert
        assertEquals(expectedAttractions, result);
    }


    @Test
    @DisplayName("should throw exception when username invalid")
    public void getNearbyAttractionsNegativeTest() throws NotExistingUserException {

        // Arrange
        String userName = "invalid";
        when(tourGuideService.getClosestAttractionDtoList(userName)).thenThrow(new NotExistingUserException(userName));

        // Act
        // Assert
        assertThrows(NotExistingUserException.class, () -> tourGuideController.getNearbyAttractions(userName));
    }


    @Test
    @DisplayName("should return providers list")
    public void getTripDealsTest() throws NotExistingAttractionException, NotExistingUserException {

        // Arrange
        String userName = "a";
        String attractionName = "b";
        List<Provider> expectedDeals = new ArrayList<>();
        Provider provider1 = new Provider(UUID.randomUUID(), "c", 1.0);
        Provider provider2 = new Provider(UUID.randomUUID(), "d", 1.0);
        expectedDeals.add(provider1);
        expectedDeals.add(provider2);

        when(tourGuideService.getTripDeals(userName, attractionName)).thenReturn(expectedDeals);

        // Act
        List<Provider> result = tourGuideController.getTripDeals(userName, attractionName);

        // Assert
        assertEquals(expectedDeals, result);
    }


    @Test
    @DisplayName("should throw exception when username invalid")
    public void getTripDealsNegativeTest() throws NotExistingAttractionException, NotExistingUserException {
        // Arrange
        String userName = "a";
        String attractionName = "b";
        Mockito.when(tourGuideService.getTripDeals(userName, attractionName)).thenThrow(new NotExistingUserException(userName));

        // Act
        assertThrows(NotExistingUserException.class, () -> tourGuideController.getTripDeals(userName, attractionName));
    }


    @Test
    @DisplayName("should throw exception when attraction name invalid")
    public void getTripDealsNegativeTest2() throws NotExistingAttractionException, NotExistingUserException {
        // Arrange
        String userName = "a";
        String attractionName = "b";
        Mockito.when(tourGuideService.getTripDeals(userName, attractionName)).thenThrow(new NotExistingAttractionException(attractionName));

        // Act
        assertThrows(NotExistingAttractionException.class, () -> tourGuideController.getTripDeals(userName, attractionName));
    }




}
