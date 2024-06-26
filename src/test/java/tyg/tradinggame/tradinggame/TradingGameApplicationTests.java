package tyg.tradinggame.tradinggame;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tyg.tradinggame.tradinggame.controller.TradingGameController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@EnabledIf(expression = "${deployment.testing.active}", loadContext = true)
@SpringBootTest
@AutoConfigureMockMvc
public class TradingGameApplicationTests {

    @Autowired
    private TradingGameController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(controller);
        assertNotNull(mockMvc);
    }

    @Test
    void testHelloWorld() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/");

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("Hello world!"));
    }

}
