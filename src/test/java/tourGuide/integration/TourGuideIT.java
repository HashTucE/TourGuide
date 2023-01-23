package tourGuide.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TourGuideIT {


    @Autowired
    private MockMvc mockMvc;



    @Test
    public void indexIT() throws Exception {

       mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Greetings from TourGuide!"));
    }


    @Test
    public void getNearbyAttractionsIT() throws Exception {

        mockMvc.perform(get("/getNearbyAttractions").param("userName", "internalUser0"))
                .andExpect(content().string(containsString("attractionName")))
                .andExpect(content().string(containsString("attractionLocation")))
                .andExpect(content().string(containsString("userLocation")))
                .andExpect(content().string(containsString("distance")))
                .andExpect(content().string(containsString("rewardPoints")))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(status().isOk());

    }


    @Test
    public void getTripDealsIT() throws Exception {

        mockMvc.perform(get("/getTripDeals")
                        .param("userName", "internalUser0")
                        .param("attractionName", "Disneyland"))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("price")))
                .andExpect(content().string(containsString("tripId")))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(status().isOk());
    }
}

