package cn.netbuffer.apigateway;

import cn.netbuffer.apigateway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class ApiGatewayApplicationTests {

    @Resource
    private UserService userService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCount() {
        log.info("count users:{}", userService.count());
    }

}
