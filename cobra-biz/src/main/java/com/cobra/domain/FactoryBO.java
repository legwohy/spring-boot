package com.cobra.domain;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @date 2020/12/30 16:11
 * @desc
 */
@Data
public class FactoryBO {
    private String src;
    private List<FormatBO> formatBOS;
}
