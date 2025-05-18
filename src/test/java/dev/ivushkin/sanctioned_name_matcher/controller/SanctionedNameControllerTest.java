package dev.ivushkin.sanctioned_name_matcher.controller;

import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import dev.ivushkin.sanctioned_name_matcher.service.SanctionedNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SanctionedNameController.class)
class SanctionedNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SanctionedNameService service;

    @BeforeEach
    void setup() {
        reset(service);
    }

    @TestConfiguration
    static class Config {
        @Bean
        public SanctionedNameService sanctionedNameService() {
            return Mockito.mock(SanctionedNameService.class);
        }
    }

    @Test
    void shouldReturnListOfNames() throws Exception {
        var name = createEntity(1L, "Osama Bin Laden", "bin laden osama");

        when(service.getListOfSanctionedNames()).thenReturn(List.of(name));

        mockMvc.perform(get("/sanctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sanctionedName").value("Osama Bin Laden"))
                .andExpect(jsonPath("$[0].normalizedName").value("bin laden osama"));
    }

    @Test
    void shouldReturnSingleNameById() throws Exception {
        var name = createEntity(2L, "Ali Bin", "ali bin");

        when(service.getById(2L)).thenReturn(name);

        mockMvc.perform(get("/sanctions/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.sanctionedName").value("Ali Bin"))
                .andExpect(jsonPath("$.normalizedName").value("ali bin"));
    }

    @Test
    void shouldAddNewSanctionedName() throws Exception {
        var saved = createEntity(3L, "Joe Smith", "joe smith");

        when(service.addSanctionedName("Joe Smith")).thenReturn(saved);

        mockMvc.perform(post("/sanctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "name": "Joe Smith" }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.sanctionedName").value("Joe Smith"))
                .andExpect(jsonPath("$.normalizedName").value("joe smith"));
    }

    @Test
    void shouldUpdateSanctionedName() throws Exception {
        var updated = createEntity(4L, "Updated Name", "updated name");

        when(service.changeSanctionedName(eq(4L), eq("Updated Name"))).thenReturn(updated);

        mockMvc.perform(put("/sanctions/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "name": "Updated Name" }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.sanctionedName").value("Updated Name"))
                .andExpect(jsonPath("$.normalizedName").value("updated name"));
    }

    @Test
    void shouldDeleteSanctionedName() throws Exception {
        doNothing().when(service).deleteSanctionedName(5L);

        mockMvc.perform(delete("/sanctions/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestForBlankName() throws Exception {
        mockMvc.perform(post("/sanctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "name": "  " }
                            """))
                .andExpect(status().isBadRequest());
    }

    private SanctionedName createEntity(long id, String name, String normalizedName) {
        var entity = new SanctionedName();
        entity.setId(id);
        entity.setName(name);
        entity.setNormalizedName(normalizedName);
        return entity;
    }
}
