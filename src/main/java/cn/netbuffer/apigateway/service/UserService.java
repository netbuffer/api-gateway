package cn.netbuffer.apigateway.service;

import cn.netbuffer.apigateway.api.Api;
import cn.netbuffer.apigateway.dao.IUserDao;
import cn.netbuffer.apigateway.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Api(name = "cn.netbuffer.apigateway.api.userApi")
@Component("cn.netbuffer.apigateway.api.userApi")
@Transactional
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Resource
    private IUserDao userDao;

    @Retryable(maxAttempts = 5, value = {RuntimeException.class})
    public User getUser() {
        LOGGER.info("execute");
        throw new RuntimeException();
    }

    public User getUser(Long id) {
        return userDao.getOne(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public long count(){
        return userDao.count();
    }
}
