package tourGuide.integration;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.model.User;
import tourGuide.repository.UserRepository;
import tourGuide.service.LocationService;
import tourGuide.service.UserService;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;




    @BeforeEach
    public void init() {userRepository.initializeInternalUsers();}




    @Test
    public void getLocationIT() throws Exception {

        User user = userService.getUser("internalUser0");
        VisitedLocation vl = new VisitedLocation(user.getUserId(), new Location(1.0,1.0), new Date());
        user.addToVisitedLocations(vl);

        mockMvc.perform(get("/getLocation").param("userName", "internalUser0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{latitude: 1.0, longitude: 1.0}"));
    }


    @Test
    public void getLocationNegativeIT() throws Exception {

        mockMvc.perform(get("/getLocation").param("userName", "invalid"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getAllCurrentLocationsIT() throws Exception {

        mockMvc.perform(get("/getAllCurrentLocations"))
                .andExpect(status().isOk());
    }
}

