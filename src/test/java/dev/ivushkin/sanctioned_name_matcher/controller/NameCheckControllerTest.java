package dev.ivushkin.sanctioned_name_matcher.controller;

import dev.ivushkin.sanctioned_name_matcher.dto.MatchResultDto;
import dev.ivushkin.sanctioned_name_matcher.service.NameCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NameCheckController.class)
@Import(NameCheckControllerTest.MockConfig.class)
class NameCheckControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public NameCheckService nameCheckService() {
            return mock(NameCheckService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NameCheckService nameCheckService;

    @Test
    void shouldReturnMatchResultDto() throws Exception {
        var inputName = "Ben Osama Ladn";
        var normalized = "ben ladn osama";
        var matched = "Osama Bin Laden";
        double score = 0.898;

        var dto = new MatchResultDto(true, inputName, normalized, matched, score);
        when(nameCheckService.check(inputName)).thenReturn(dto);

        mockMvc.perform(post("/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "name": "Ben Osama Ladn" }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.match").value(true))
                .andExpect(jsonPath("$.inputName").value(inputName))
                .andExpect(jsonPath("$.normalizedInput").value(normalized))
                .andExpect(jsonPath("$.matchedName").value(matched))
                .andExpect(jsonPath("$.similarityScore").value(score));
    }
}
