package cn.netbuffer.apigateway.service;

import cn.netbuffer.apigateway.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Retryable(maxAttempts = 5, value = {RuntimeException.class})
    public User getUser() {
        LOGGER.info("execute");
        throw new RuntimeException();
    }
}
