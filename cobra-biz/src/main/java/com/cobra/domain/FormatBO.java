package com.cobra.domain;

import lombok.Data;

/**
 * @author admin
 * @date 2020/12/30 16:05
 * @desc
 */
@Data
public class FormatBO{
    private String encType;
    private Boolean isTransBigBefore;
    private Boolean isTransBigAfter;
}