package cn.netbuffer.apigateway.handler;

import cn.netbuffer.apigateway.exception.ApiException;

public interface IApiExceptionHandler {
    Object handle(ApiException apiException);
}
