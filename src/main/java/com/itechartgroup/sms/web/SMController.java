package com.itechartgroup.sms.web;

import com.itechartgroup.sms.service.Match;
import com.itechartgroup.sms.service.SMService;
import com.itechartgroup.sms.service.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class SMController {

    private final SMService smService;

    @PostMapping(value = "/match", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<Match> match(@RequestBody List<User> users) {
        return smService.match(users);
    }

}
