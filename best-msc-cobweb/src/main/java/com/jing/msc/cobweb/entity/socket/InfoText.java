package com.jing.msc.cobweb.entity.socket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * @author : jing
 * @since : 2025/3/10 11:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "websocket 信息文本")
public class InfoText {

    @Schema(description = "消息Id")
    private Long id;

    @Schema(description = "操作员Id")
    private String operatorId;

    @Schema(description = "发件人Id，允许代称管理员发送")
    private String senderId;

    @Schema(description = "通知等级")
    private Integer level;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String message;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "消息最晚接收时间")
    private LocalDateTime latestReceiveTime;

    @Schema(description = "收件人Id，0则是所有人")
    private String recipientId;

    @Schema(description = "未读人，0则是所有人")
    private final HashSet<String> unread = new HashSet<>();

    @Schema(description = "已读人")
    private final HashSet<String> read = new HashSet<>();

    @Schema(description = "只给在线人发送，0：否，1：是")
    private Integer isLine;

    @Schema(description = "通知等级 1，打开模态表单显示通知信息")
    public static final int LEVEL_01 = 1;

    @Schema(description = "通知等级 2，浏览器弹框显示通知信息")
    public static final int LEVEL_02 = 2;

    @Schema(description = "通知等级 3，浏览器控制台显示的通知信息")
    public static final int LEVEL_03 = 3;


    public void setLevel(Integer level) {
        switch (level) {
            case LEVEL_01:
            case LEVEL_02:
            case LEVEL_03:
                this.level = level;
                break;
            default:
                this.level = LEVEL_03;
        }
    }

    public String getUnread() {
        StringBuilder sb = new StringBuilder();
        for (String s : unread) {
            sb.append(s).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    /**
     * 每次从未读人中移除用户则已读增加一个用户
     *
     * @param userId 用户id
     */
    public void removeUnread(String userId) {
        unread.remove(userId);
        addRead(userId);
    }

    /**
     * 未读人数
     *
     * @return int
     */
    public int unreadSize() {
        return unread.size();
    }

    public void addRead(String userId) {
        read.add(userId);
    }

    /**
     * 已读人数
     *
     * @return 逗号隔开的操作员Id
     */
    public String getRead() {
        StringBuilder sb = new StringBuilder();
        for (String s : read) {
            sb.append(s).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    /**
     * 已读人数
     *
     * @return int
     */
    public int readSize() {
        return read.size();
    }

    @Override
    public String toString() {
        return "InfoText{" +
                "id=" + id +
                ", operatorId='" + operatorId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", level=" + level +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", sendTime=" + sendTime +
                ", latestReceiveTime=" + latestReceiveTime +
                ", recipientId='" + recipientId + '\'' +
                ", unread=" + unread +
                ", read=" + read +
                ", isLine=" + isLine +
                '}';
    }
}
