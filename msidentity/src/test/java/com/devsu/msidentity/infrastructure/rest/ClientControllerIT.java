package com.devsu.msidentity.infrastructure.rest;

import com.devsu.msidentity.infrastructure.messaging.ClientEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientEventPublisher eventPublisher;

    private static final String BASE_URL = "/api/v1/clients";

    private static final String VALID_CLIENT = """
            {
              "name": "Jose Lema",
              "gender": "M",
              "age": 30,
              "identification": "1234567890",
              "address": "Otavalo sn y principal",
              "phone": "098254785",
              "password": "1234",
              "status": true
            }
            """;

    @Test
    void createClient_shouldReturn201WithGeneratedClientId() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CLIENT))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId", notNullValue()))
                .andExpect(jsonPath("$.name").value("Jose Lema"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void createClient_whenDuplicateIdentification_shouldReturn409() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_CLIENT));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CLIENT))
                .andExpect(status().isConflict());
    }

    @Test
    void getClientById_whenExists_shouldReturn200() throws Exception {
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CLIENT))
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identification").value("1234567890"));
    }

    @Test
    void getClientById_whenNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClient_shouldReturn200WithUpdatedData() throws Exception {
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CLIENT))
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        String updated = """
                {
                  "name": "Jose Lema Updated",
                  "age": 31,
                  "identification": "1234567890",
                  "status": true
                }
                """;

        mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jose Lema Updated"));
    }

    @Test
    void deleteClient_shouldReturn204AndPublishEvent() throws Exception {
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CLIENT))
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isNoContent());

        verify(eventPublisher).publishClientDeleted(anyLong());
    }
}
