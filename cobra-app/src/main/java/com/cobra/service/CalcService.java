package com.cobra.service;

import com.cobra.domain.pojo.DataVO;
import org.springframework.stereotype.Service;

@Service
public class CalcService {
    public int add(int num1,int num2){

        return num1+num2;
    }
    public Integer addForObject(DataVO data){

        return data.getNum1()+data.getNum2();
    }


}
