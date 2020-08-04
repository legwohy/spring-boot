package com.cobra.service;

import com.cobra.entity.DataVO;
import org.springframework.stereotype.Service;

@Service
public class CalcService {
    public int add(int num1,int num2){

        return num1+num2;
    }
    public Integer add2(DataVO data){

        return data.getNum1()+data.getNum2();
    }


}
