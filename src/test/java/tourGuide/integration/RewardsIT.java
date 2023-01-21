package tourGuide.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsIT {


    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getRewardsIT() throws Exception {

        mockMvc.perform(get("/getRewards").param("userName", "internalUser0"))
                .andExpect(status().isOk())
                .andReturn();
    }
}

