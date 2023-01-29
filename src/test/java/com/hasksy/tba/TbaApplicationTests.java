package com.hasksy.tba;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/database-test.properties")
@ActiveProfiles("test")
class TbaApplicationTests {

    @Test
    void contextLoads() {
    }

}
