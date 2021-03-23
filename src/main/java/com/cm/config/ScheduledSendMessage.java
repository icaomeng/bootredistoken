package com.cm.config;
import com.cm.entity.Token;
import com.cm.mapper.TokenMapper;
import com.cm.utils.Time;
import com.cm.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务，删除token，每分钟执行一次
 */
@Component
@EnableScheduling
public class ScheduledSendMessage {


    @Autowired
    private TokenMapper tokenMapper;

    // 每分钟执行一次
    @Scheduled(cron = "*/10 * * * * ?")
    private void deleteToken() {
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
