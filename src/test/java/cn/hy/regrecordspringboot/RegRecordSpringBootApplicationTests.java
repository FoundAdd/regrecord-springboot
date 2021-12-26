package cn.hy.regrecordspringboot;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegRecordSpringBootApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(RegRecordSpringBootApplicationTests.class);
    @Test
    void contextLoads() {
        logger.error("this is a test logger");
    }

}
