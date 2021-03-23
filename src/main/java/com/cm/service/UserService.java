    package com.cm.service;

    import com.cm.entity.User;
    import com.cm.utils.ResponseBase;
    import org.apache.ibatis.annotations.Param;

    import java.util.List;

    public interface UserService {

        void insertUser(User user);

        /**
         * 获取所有数据
         * @return
         */
        List<User> listUser();

        /**
         * 通过Redis注解获取所有数据
         * @return
         */
        List<User> listUserRedis();

        /**
         * 通过Redis注解获取所有数据
         * @return
         */
        List<User> listUserRedisUuid(String uuid);

        void deleteUser(String uuid);

        void updateUser(User user);

        void forSave();

        // token存到redis中
        public ResponseBase UserLogin(String username, String passwrod);

        // token存到数据库中
        public ResponseBase userLogin(String username, String passwrod);

        public ResponseBase getUserInfo(String userId);

        void deleteToken();
    }
