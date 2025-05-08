package com.jing.msc.cobweb.task;

import com.jing.msc.cobweb.entity.socket.InfoText;
import com.jing.msc.cobweb.util.WebsocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author : jing
 * @since : 2025/3/10 17:25
 */
@Component
public class TestTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Scheduled(cron = "*/6 * * * * ?")
    public void sayHello() {
        InfoText infoText = new InfoText();
        infoText.setTitle("内容获取进度");
        infoText.setMessage("你好");
        infoText.setRecipientId("12");
        infoText.setSendTime(LocalDateTime.now());
        infoText.setLatestReceiveTime(LocalDateTime.now().plusDays(3));
        WebsocketUtil.sendMessage("12", infoText, 0);
        logger.info("sayHello");
    }

}
