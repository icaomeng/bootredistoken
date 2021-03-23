package com.cm.service.impl;

import com.cm.entity.JsUser;
import com.cm.entity.Token;
import com.cm.entity.User;
import com.cm.mapper.TokenMapper;
import com.cm.mapper.UserMapper;
import com.cm.utils.*;
import com.cm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseApiService implements UserService {
    // 查询时候使用@Cacheable(cacheNames = "listuser", key = "123")注解
    // 删除、修改、增加时候使用 @CacheEvict(cacheNames = "listuser", key = "123")  删除缓存
    // 这样缓存就同步了

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private ProductToken productToken;

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public List<User> listUser() {
        return userMapper.listUser();
    }

    @Override
    @Cacheable(cacheNames = "listuser", key = "123")
    public List<User> listUserRedis() {
        return userMapper.listUserRedis(null);
    }

    @Override
    @Cacheable(cacheNames = "listuser", key = "#uuid")
    public List<User> listUserRedisUuid(String uuid) {
        return userMapper.listUserRedis(uuid);
    }

    @Override
    @CacheEvict(cacheNames = "listuser", key = "123")
    public void deleteUser(String uuid) {
        userMapper.deleteUser(uuid);
    }

    @Override
    @CacheEvict(cacheNames = "listuser", key = "123")
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    @CacheEvict(cacheNames = "listuser", key = "123")
    public void forSave() {
        for (int i = 1; i <=110 ; i++) {
            User user=new User();
            user.setUuid(CommUtils.getUUID());
            user.setUserName("吴亦凡");
            user.setCity("加拿大");
            user.setUserAge(i);
            user.setUserSex("男");
            userMapper.insertUser(user);
        }
    }

    @Override
    public ResponseBase UserLogin(String username, String passwrod) {
        // 1.校验登录是否成功
        JsUser jsUser = userMapper.login(username, passwrod);
        if(CommUtils.isEmpty(jsUser)){
            return setResultError("账户名密码错误！！！");
        }else{
            // 成功后生成一个token
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            Map<String, String> mapInfo = productToken.productToken(jsUser.getUuid(), token);
            //3.返回token信息（有效期50分钟）
            return setResultSuccess((Object)mapInfo);
        }
    }

    @Override
    public ResponseBase userLogin(String username, String passwrod) {
        /**
         存到数据库中的token，设值定时任务，半小时后删除token
         如果该用户半小时内登录了，token重置
         * */
        // 1.校验登录是否成功
        JsUser jsUser = userMapper.login(username, passwrod);
        int count = tokenMapper.getUserId(jsUser.getUuid());
        // 如果查到，说明重复登录
        if(!CommUtils.isEmpty(count)){
            // 重置实效，根据用户id修改时间
            tokenMapper.updateByUserId(jsUser.getUuid(),DateUtils.addDateMinutes(new Date(), 30));
        }

        if(CommUtils.isEmpty(jsUser)){
            return setResultError("账户名密码错误！！！");
        }else{
            // 成功后生成一个token
            Token token=new Token();
            token.setUuid(CommUtils.getUUID());
            token.setToken(jsUser.getUuid()+UUID.randomUUID().toString().substring(0,5));
            // 存储时间，比当前时间大30分钟，用于判断token是否过期
            token.setCreateTime(DateUtils.addDateMinutes(new Date(), 30));
            tokenMapper.saveToken(token);
            //3.返回token信息
            return setResultSuccess(token);
        }
    }

    @Override
    public ResponseBase getUserInfo(String userId) {
        JsUser jsUser = userMapper.getJsUser(userId);
        if(jsUser != null) {
            return setResultSuccess((Object)jsUser);
        } else {
            return setResultSuccess("无此用户");
        }
    }

    @Override
    public void deleteToken() {
        // 获取到所有token数据
        List<Token> token = tokenMapper.selectToken();
        // 判断当前时间是否大于数据库时间
        for (int i = 0; i < token.size(); i++) {
            if(token.get(i).getCreateTime().getTime()<System.currentTimeMillis()){
                tokenMapper.deleteByUuid(token.get(i).getUuid());
            }
        }
    }


}
