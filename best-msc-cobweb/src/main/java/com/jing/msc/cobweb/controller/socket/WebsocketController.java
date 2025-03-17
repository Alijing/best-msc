package com.jing.msc.cobweb.controller.socket;

import com.jing.common.core.util.JsonUtils;
import com.jing.msc.cobweb.entity.socket.InfoText;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.util.WebsocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * 处理类前端连接访问到此类
 *
 * @author : jing
 * @since : 2025/3/10 14:05
 */
@Component
@ServerEndpoint(value = "/ws/cobweb/{account}")
public class WebsocketController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 通知信息
     */
    private final InfoText infoText = new InfoText();

    /**
     * 注入 ApplicationContext
     */
    @Autowired
    private ApplicationContext applicationContext;

    private static NovelService novelService;

    @PostConstruct
    public void init() {
        // 在初始化时手动获取 novelService
        novelService = applicationContext.getBean(NovelService.class);
    }

    /**
     * 连接事件，加入注解
     *
     * @param account 传入进来的用户账号
     * @param session 当前用户的session
     */
    @OnOpen
    public void onOpen(@PathParam(value = "account") String account, Session session) {
        logger.info("用户【{}】已上线", account);
        infoText.setOperatorId(account);
        // 添加到 session 的映射关系中
        WebsocketUtil.addSession(account, session);
        // 广播通知，某用户上线了，一次只能执行一次发送不然连接会断掉
        // WebsocketUtil.sendMessageForAll(infoText);
        // 检测当前用户是否还有未读消息，如果有则发送给前端
        WebsocketUtil.unreadInfo(account);
    }

    /**
     * 断开连接事件，加入注解
     * 用户断开连接
     */
    @OnClose
    public void onClose() {
        logger.info("用户【{}】已下线", infoText.getOperatorId());
        // infoText.setMessage("用户【"+ infoText.getOperatorId() + "】已下线");
        // infoText.setLevel(InfoText.LEVEL_03);
        // 广播通知，某用户下线了
        // WebsocketUtil.sendMessageForAll(infoText);
        // 移除 session 的映射关系
        WebsocketUtil.removeSession(infoText.getOperatorId());
    }

    /**
     * 当接收到用户上传的消息
     *
     * @param message 前端传入的消息，json 格式
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("用户发送的信息【{}】", message);
        InfoText bean = JsonUtils.toBean(message, InfoText.class);
        if (bean == null) {
            return;
        }
        if ("INTERRUPTED".equals(bean.getTitle())) {
            novelService.cancelCrawl(Long.valueOf(bean.getMessage()));
            return;
        }
        // 直接广播
        WebsocketUtil.sendMessageForAll(bean);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (Exception ex) {
            logger.error("websocket error", ex);
        }
        logger.error("websocket error", throwable);
    }

}
