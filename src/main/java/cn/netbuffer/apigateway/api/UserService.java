package cn.netbuffer.apigateway.api;

import cn.netbuffer.apigateway.model.User;
import org.springframework.stereotype.Service;

@Api(name = "cn.netbuffer.apigateway.api.userApi")
@Service("cn.netbuffer.apigateway.api.userApi")
public class UserService {

    public User getUser(Long id) {
        return new User(id);
    }

    public User saveUser(User user) {
        return user;
    }
}
