package com.cobra.filter.response;

import lombok.Data;

/**
 * 测试自动注入bean
 */
@Data
public class TestFilterAutoBean {
    private Integer id;
    private String name;
    public TestFilterAutoBean(Integer id, String name){
        this.id = id;
        this.name = name;
    }
}
