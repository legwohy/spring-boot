package com.cobra.test;


import org.junit.Test;

public class TestString {
    @Test public void testReplace(){
        String content = "a{b}cd{e}fgh{d}";
        String key = "b,e,d";
        String value = "1,2,3";
        String[] keyArr = key.split(",");
        String[] valueArr = value.split(",");
        for (int i = 0;i<keyArr.length;i++){
            String target = "{"+keyArr[i]+"}";
            if(content.contains(target)){
                content = content.replace(target,valueArr[i]);
            }
        }



        System.out.println("---------->"+content);
    }
}
