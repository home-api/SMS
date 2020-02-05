package com.itechartgroup.sms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechartgroup.sms.service.Match;
import com.itechartgroup.sms.service.SMService;
import com.itechartgroup.sms.service.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SMControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SMService smService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnMatchedUsers() throws Exception {
        // Given
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User("Egor", Set.of("music", "cars")));
        expectedUsers.add(new User("Ivan", Set.of("cars", "hiking")));

        List<Match> matchedUsers = new ArrayList<>();
        matchedUsers.add(new Match("Egor", "Ivan", Set.of("dancing")));

        when(smService.match(expectedUsers)).thenReturn(matchedUsers);

        // When, Then
        String expectedRequestBody = objectMapper.writeValueAsString(expectedUsers);
        String expectedResponseBody = objectMapper.writeValueAsString(matchedUsers);

        this.mockMvc.perform(
                post("/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedResponseBody)));
    }

}