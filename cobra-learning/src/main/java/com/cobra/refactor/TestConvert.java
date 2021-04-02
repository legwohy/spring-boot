package com.cobra.refactor;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by admin on 2019/9/27.
 */
public class TestConvert {
    public static void main(String[] args) {
        AbstractDTOConvert convert = new AbstractDTOConvert<TestA,TestB>() {
            @Override
            protected TestB doForward(TestA testA) {
                TestB testB = new TestB();
                BeanUtils.copyProperties(testA,testB);

                return testB;
            }

            @Override
            protected TestA doBackward(TestB testB) {
                TestA testA = new TestA();
                BeanUtils.copyProperties(testB,testA);

                return testA;
            }
        };

        /** testA--->testB */
        System.out.println("正向:"+convert.convert(new TestA()));

        /**testB-->testA */
        System.out.println("反向:"+convert.reverse().convert(new TestB()));

    }


    @Data
    private static  class TestA{
        private Integer id;
    }
    @Data
    private static class TestB{
        private Integer id;
        private String name;
    }
}
