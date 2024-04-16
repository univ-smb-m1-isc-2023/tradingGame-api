
package tyg.tradinggame.tradinggame;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import tyg.tradinggame.tradinggame.application.game.GameService.GameInDTO;
import tyg.tradinggame.tradinggame.application.game.PlayerService.PlayerInDTO;
import tyg.tradinggame.tradinggame.controller.PlayerController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTests {

    @Autowired
    private PlayerController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(controller);
        assertNotNull(mockMvc);
    }

    @Test
    void testCreatePlayer() throws Exception {
        PlayerInDTO newPlayer = new PlayerInDTO("username_in", "password_in"); // replace with actual data
        String playerJson = new ObjectMapper().writeValueAsString(newPlayer);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(newPlayer.username())))
                .andExpect(jsonPath("$.id", is(1)));

    }

}