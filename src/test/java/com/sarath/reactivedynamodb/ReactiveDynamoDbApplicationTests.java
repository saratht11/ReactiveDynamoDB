package com.sarath.reactivedynamodb;

import com.sarath.reactivedynamodb.config.TestDynamoDbConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestDynamoDbConfig.class)
public class ReactiveDynamoDbApplicationTests {

	@Test
	@Ignore("Disable context test for reactive improvements focus")
	public void contextLoads() {
	}

}
