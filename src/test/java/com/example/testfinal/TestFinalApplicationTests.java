package com.example.testfinal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@SpringBootTest
@ActiveProfiles("test")
class TestFinalApplicationTests {

    @Test
    void contextLoads() {
    }


}
