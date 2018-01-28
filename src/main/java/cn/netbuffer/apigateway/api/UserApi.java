package cn.netbuffer.apigateway.api;

import cn.netbuffer.apigateway.model.User;
import org.springframework.stereotype.Component;

@Api(name = "cn.netbuffer.apigateway.api.userApi")
@Component("cn.netbuffer.apigateway.api.userApi")
public class UserApi {

    public User getUser(Long id) {
        return new User(id);
    }

    public User saveUser(User user) {
        return user;
    }
}
