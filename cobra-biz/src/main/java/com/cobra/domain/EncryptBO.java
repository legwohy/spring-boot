package com.cobra.domain;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @date 2020/12/30 15:22
 * @desc
 */
@Data
public class EncryptBO {
    private String src;
    private String encType;
    private Boolean isTransBigBefore;
    private Boolean isTransBigAfter;

}

