package com.cm.mapper;

import com.cm.entity.JsUser;
import com.cm.entity.Token;
import com.cm.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TokenMapper {

    // 保存token到数据库
    void saveToken(Token token);

    // 根据用户id查看是否存在token
    int getUserId(@Param("userId")String userId);

    // 重置token到期时间
    void updateByUserId(@Param("userId")String userId,@Param("createTime") Date createTime);

    // 获取所有token数据，用于定时任务删除
    List<Token> selectToken();

    // 根据uuid删除
    void deleteByUuid(@Param("uuid")String uuid);

    // 根据前端传的数据判断该token是否存在
    int getType(@Param("uuid")String uuid,@Param("token")String token);
}
