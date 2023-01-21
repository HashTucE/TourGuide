package tourGuide.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tourGuide.dto.UserPreferencesDto;
import tourGuide.repository.UserRepository;
import tourGuide.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;




    @BeforeEach
    public void init() {
        userRepository.initializeInternalUsers();
    }



    @Test
    public void getUserPreferencesIT() throws Exception {

        mockMvc.perform(get("/getPreferences")
                        .param("userName", "internalUser0"))
                .andExpect(status().isOk())
                .andReturn();

    }


    @Test
    public void updateUserPreferencesIT() throws Exception {

        // Arrange
        UserPreferencesDto userPreferencesDto = new UserPreferencesDto();
        userPreferencesDto.setNumberOfAdults(1);
        userPreferencesDto.setTripDuration(1);
        userService.updateUserPreferences("internalUser0", userPreferencesDto);

        // Act
        mockMvc.perform(put("/updatePreferences")
                        .param("userName", "internalUser0")
                        .content(asJsonString(userPreferencesDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("User preferences updated successfully"))
                .andExpect(status().isOk())
                .andReturn();
    }



    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
