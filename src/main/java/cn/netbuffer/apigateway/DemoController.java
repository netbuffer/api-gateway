package cn.netbuffer.apigateway;

import cn.netbuffer.apigateway.model.User;
import cn.netbuffer.apigateway.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class DemoController {

    @Resource
    private UserService userService;

    @RequestMapping("/retry")
    public User getUser() {
        return userService.getUser();
    }
}
