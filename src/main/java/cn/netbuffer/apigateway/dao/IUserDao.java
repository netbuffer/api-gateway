package cn.netbuffer.apigateway.dao;

import cn.netbuffer.apigateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}