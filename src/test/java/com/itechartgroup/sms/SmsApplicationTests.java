package com.itechartgroup.sms;

import com.itechartgroup.sms.service.SMService;
import com.itechartgroup.sms.web.SMController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmsApplicationTests {

    @Autowired
    private SMController smController;
    @Autowired
    private SMService smService;

    @Test
    void contextLoads() {
        assertThat(smController).isNotNull();
        assertThat(smService).isNotNull();
    }

}
