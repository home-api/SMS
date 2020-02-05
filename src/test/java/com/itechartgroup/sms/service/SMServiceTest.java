package com.itechartgroup.sms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SMServiceTest {

    private SMService smService;

    @BeforeEach
    public void setUp() {
        smService = new SMService();
    }

    @Test
    public void testMatch() throws IOException {
        // Given
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Jane", Set.of("movies", "music")));
        users.add(new User("Peter", Set.of("cars", "movies")));
        users.add(new User("Dustin", Set.of("movies", "cars", "hiking", "music")));
        users.add(new User("Lana", Set.of("cars", "dancing")));

        // When
        List<Match> actualMatchedUsers = smService.match(users);

        // Then
        List<Match> expectedMatchedUsers = new ArrayList<>();
        expectedMatchedUsers.add(new Match("Jane", "Dustin", List.of("movies", "music")));
        expectedMatchedUsers.add(new Match("Peter", "Lana", List.of("cars")));

        assertThat(actualMatchedUsers).isEqualTo(expectedMatchedUsers);
    }

    @Test
    public void testNoMatches() {
        // Given
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Jane", Set.of("music")));
        users.add(new User("Peter", Set.of("cars", "movies")));
        users.add(new User("Dustin", Set.of("dancing")));

        // When
        List<Match> actualMatchedUsers = smService.match(users);

        // Then
        assertThat(actualMatchedUsers).isEmpty();
    }

}