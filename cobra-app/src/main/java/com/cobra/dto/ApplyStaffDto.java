package com.cobra.dto;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class ApplyStaffDto
{
    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户发起咨询客服操作的页面 url，比如商品链接，订单页面等
     */
    private String fromPage;

    /**
     * fromPage 页面的标题
     */
    private String fromTitle;

    /**
     * 用户ip
     */
    private String fromIp;

    /**
     * 	用户设备类型信息
     */
    private String deviceType;

    /**
     * 产品标识，可以是 Android 应用的报名，iOS 应用的 bundleid 等
     */
    private String productId;

    /**
     * 请求分配的客服类型，如果传0，表示机器人，传1表示人工。默认为机器人
     */
    private String staffType;

    /**
     * 只请求该 ID 的客服，客服 ID 可在管理后台查看
     */
    private String staffId;

    /**
     * 只请求该客服分组 ID 内的客服，分组 ID 可在管理后台查看
     */
    private String groupId;

    /**
     * 申请人工客服之前是否先申请机器人开关，0代表关闭，1代表启用
     */
    private String robotShuntSwitch;

    /**
     * 该访客这次会话的vip等级，从0到11
     */
    private String level;

    /**
     * 有多个机器人时，指定接入某一个机器人
     */
    private String robotId;

}
