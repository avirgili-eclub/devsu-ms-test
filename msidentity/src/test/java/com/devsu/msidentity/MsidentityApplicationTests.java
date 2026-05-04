package com.devsu.msidentity;

import com.devsu.msidentity.infrastructure.messaging.ClientEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MsidentityApplicationTests {

    @MockitoBean
    private ClientEventPublisher eventPublisher;

    @Test
    void contextLoads() {
    }
}
