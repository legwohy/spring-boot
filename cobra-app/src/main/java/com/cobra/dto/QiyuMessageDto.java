package com.cobra.dto;

import lombok.Data;

@Data
public class QiyuMessageDto
{
    /**
     * 客服id
     */
    private String uid;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;




}
