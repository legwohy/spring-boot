package com.cobra.entirty;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoFrom {
    private Integer id;

    private String realName;

    private String userPhone;

    private String idCardNumber;

    private String idcardImgZ;

    private String idcardImgF;

    private String idcardImgS;

    private String workAddress;

    private String firstContactName;

    private String firstContactPhone;

    private Integer firstContactRelation;

    private String secondContactName;

    private String secondContactPhone;

    private Integer secondContactRelation;

    private Integer channelId;

    private Integer merchantId;

    private Integer status;

    private Date updateStatusTime;

    private Date createTime;

    private Date updateTime;

    private Integer realNameStatus;

    private Integer updateContactCount;

    private Integer contactStatus;

    private Integer riskStatus;

    private Integer empowerStatus;

    private String jxlToken;

    private Date jxlTokenTime;

    private Integer jxlStatus;

    private Date jxlDetailTime;

    private Integer jxlDetailStatus;

    private String assessorWorkInfoId;

    private Integer qqContactStatus;

    private Integer riskRulesMerchantStatus;


}