package tourGuide.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().isOk());

    }


    @Test
    public void getTripDealsIT() throws Exception {

        mockMvc.perform(get("/getTripDeals")
                        .param("userName", "internalUser0")
                        .param("attractionName", "Disneyland"))
                .andExpect(status().isOk());
    }
}

