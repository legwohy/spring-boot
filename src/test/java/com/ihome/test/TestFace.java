package com.ihome.test;

import com.alibaba.fastjson.JSONObject;
import com.ihome.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/6/1.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFace {

    @Test public void checkIdcardZ() {
        System.out.println("---------->校验省份证正面开始");
        String apiKey = "LGq9uVn4OQnzFgNlSQ_S8-706xmcq5ij";
        String apiSecret = "NFzaxWUh4_o3st_Kr-kCOb53ZIbSbJ0V";
        String faceApiBaseUrl = "https://api.faceid.com";
        Map<String, String> textMap = new HashMap<>(), fileMap = new HashMap<>();
        // 可以设置多个input的name，value
        textMap.put("api_key", apiKey);
        textMap.put("api_secret", apiSecret);
        // 返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。
        textMap.put("legality", "1");
        fileMap.put("image", "file:///F:/temp/card_zm.jpg");// 本地图片
        String idcardResultStr = HttpUtil.formUploadImageNew(faceApiBaseUrl + "/faceid/v1/ocridcard", textMap, fileMap, "");

        System.out.println("返回结果:"+idcardResultStr);
        if(StringUtils.isEmpty(idcardResultStr)){
            throw new RuntimeException("检查结果为空:");

        }
        JSONObject idcardResultJson = JSONObject.parseObject(idcardResultStr);
        if(!"front".equals(idcardResultJson.getString("side"))){
            throw new RuntimeException("请上传身份证正面!:");

        }
        String idcard = idcardResultJson.getString("id_card_number");
        String realName = idcardResultJson.getString("name");

        System.out.println("idcard="+idcard+",realName="+realName);

        System.out.println("---------->校验省份证正面结束");
    }

    @Test public void checkFace() {
       System.out.println("-------------->识别开始");

        String apiKey = "LGq9uVn4OQnzFgNlSQ_S8-706xmcq5ij";
        String apiSecret = "NFzaxWUh4_o3st_Kr-kCOb53ZIbSbJ0V";
        String faceApiFaceUrl = "https://api.megvii.com";// 人脸
        Map<String, String> textMap = new HashMap<>(), fileMap = new HashMap<>();
        // 可以设置多个input的name，value
        textMap.put("api_key", apiKey);
        textMap.put("api_secret", apiSecret);
        // 确定本次比对为“有源比对”或“无源比对”。取值只为“1”或“0”
        textMap.put("comparison_type", "1");
        // 确定待比对图片的类型。取值只为“meglive”、“facetoken”、“raw_image”三者之一
        textMap.put("face_image_type", "raw_image");
        textMap.put("idcard_name", "罗才磊");// 身份证姓名
        textMap.put("idcard_number", "500225199906106854");// 身份证号码
        // 对验证照作人脸检测时发现有多张脸，是否立即返回错误，或者取最大的一张脸继续比对。本参数取值只能是 “1” 或 "0" （缺省值为“1”）:
        textMap.put("fail_when_multiple_faces","0");
        textMap.put("face_quality_threshold", "0");
        textMap.put("multi_oriented_detection", "1");
        String headFileUrl = "F:\\temp\\test.png";// 人头地址
        fileMap.put("image", headFileUrl.replaceAll("\\\\", "\\/"));
        System.out.println(("人脸识别参数params=={} 图片地址=={}"+ textMap.toString()+","+fileMap.toString()));
        String faceResultStr = HttpUtil.formUploadImageNew(faceApiFaceUrl + "/faceid/v2/verify", textMap, fileMap, "");

        System.out.println("返回结果----------->"+faceResultStr);
        if(faceResultStr==null||"".equals(faceResultStr)){
            System.out.println("人脸识别失败!");

        }

        System.out.println("----------->识别结束");




    }
}
