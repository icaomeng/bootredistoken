package com.cm.mapper;

import com.cm.entity.JsUser;
import com.cm.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    void insertUser(User user);

    /**
     * 获取所有数据
     * @return
     */
    List<User> listUser();

    /**
     * 获取所有数据
     * @return
     */
    List<User> listUserRedis(@Param("uuid")String uuid);

    void deleteUser(@Param("uuid")String uuid);

    void updateUser(User user);

    JsUser login(@Param("username")String username, @Param("password")String password);

    JsUser getJsUser(@Param("uuid")String uuid);

}
