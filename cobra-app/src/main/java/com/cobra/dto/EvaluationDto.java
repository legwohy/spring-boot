package com.cobra.dto;

import lombok.Data;

/**
 * @author  admin
 */
@Data
public class EvaluationDto
{
    /**
     * 用户id
     */
    private String uid;
    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 评价值
     */
    private String evaluation;

    /**
     * 备注
     */
    private String remarks;
}
