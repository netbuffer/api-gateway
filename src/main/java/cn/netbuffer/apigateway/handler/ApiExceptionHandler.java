package cn.netbuffer.apigateway.handler;

import cn.netbuffer.apigateway.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApiExceptionHandler implements IApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    public Object handle(ApiException apiException) {
        LOGGER.error("api invoke error:{}", apiException);
        return apiException.getMessage();
    }
}
