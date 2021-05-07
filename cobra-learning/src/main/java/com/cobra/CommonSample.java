package com.cobra;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.cobra.script.jgroove.utils.CipherCommonUtils;
import com.cobra.util.StringCommonUtils;
import com.zhongan.xd.orion.gateway.scorpoin.common.ZhongAnApiClient;
import com.zhongan.xd.orion.gateway.scorpoin.common.ZhongAnOpenException;
import com.zhongan.xd.orion.gateway.scorpoin.common.dto.CommonRequest;
import com.zhongan.xd.orion.gateway.scorpoin.common.dto.CommonResponse;
import com.zhongan.xd.orion.gateway.scorpoin.common.enums.SignTypeEnum;
import com.zhongan.xd.orion.gateway.scorpoin.signature.SignatureUtils;

public class CommonSample {

    public static void main(String[] args) throws Exception {
       // test();
        String content = "1234";

        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBImrOLkLteSSgbyr4kANAwvMnHNAbWefHVaA0uIRy9u+JnBFAyGAsFg40tLPmLYQJ9R/fkZvzcfrsfUMzY3s5kU/KE7siCBjPUA03dDdjSqktmI6+KWT8d+ATAY7KBCtuAGAzYkfEq2i8XQmszGvxopyb6RVpUtiqGzNJjzw/DQIDAQAB";
        String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIEias4uQu15JKBvKviQA0DC8ycc0BtZ58dVoDS4hHL274mcEUDIYCwWDjS0s+YthAn1H9+Rm/Nx+ux9QzNjezmRT8oTuyIIGM9QDTd0N2NKqS2Yjr4pZPx34BMBjsoEK24AYDNiR8SraLxdCazMa/GinJvpFWlS2KobM0mPPD8NAgMBAAECgYB/2YhvC4Q9wwPNR2L+5u4hjyHXpoIvoiqEwzSzHcAcZnkv//Fg90GoREnaStIV4RhqF2NwIKSLOVzaCQageSzEn7Qe5bt0cHOap0hm/UO+wdWmU9areXNyAn66Z1Laryzy4DlLx40cVMcDLiwdYVH9uJLe251E3F0YeSf0JtH2TQJBAMazptSlqRnyVwVw4hNqxUgf74/iyQOerOXBEwqT+8O2ahYmroWLgbM/UjJXGv0OmsIiPc7K4UhNYbqSQ2WITFMCQQCmXz3Jua5WDaYRIRq9A2E/cLZ+gp6221SP34u0R+9hrNrcp9dnyv2KuMTA3ymoEgtc4iKMXHs2SZBrlHHd0NsfAkBlL8t/crVAj1adYLTgKnk4l3rJjTXMcBugDMiDlISbT+OKUDyDqV1tSIu1MwJ9Z9iLkCBllI9Q4IcqGZ3UsKodAkAfU4ea2C+PXHqMdyNFu8Ev5NU39PhVje7aOElh5LmaNqXvLltM29fpqujBOEPrtZoWHy5KJ4+7pRZga08VaSIZAkA8y1rd9v/p6QX4a0DpV96SIFn9BJtGlj26+D3kE3uhaoXJ8A6vmVobFq8WjsEsFpOqUY5T580QtYm5WSAdLeQm";

        String cipherText =SignatureUtils.rsaEncrypt(content, pubKey,"utf-8");
        System.out.println(content.equals(CipherCommonUtils.doRSAPrivateDecrypt(cipherText,priKey)));

    }

    private static void test() throws ZhongAnOpenException{
        String appkey = "mhelsiodnyvn76xtefu0x8fk8r6h4by9";
        String prk = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMbzV/Ptwy31chqFORjD7zzd03HvfhUTOA1dEi8UpsuHgt/J6jXYc6lpYLw1YlO13Mk8KZBY4rBc8VuUgeJFHQMhQQdAhgYvJuKWaDEacQoIvYiIuBwVQDkF8zMyyvEcZFMlZLYSJ0wL9L/uhmXDoqhLxT+7p95k7b+9cn76JiIhAgMBAAECgYBTaKozJjdyCTkxTTFG5L5kMkv3tISTqV8SmzaIbowPQFzqHekHchvrprZbsbXrio9p/sMeNWW0swKtdnGNv5QNgl1QPLcjvXn5azTvmkXnfK1oN9NgXvTKeO3ULliJ1uhz8DoIDG6cYv/ltM86OBdppmjxKsEYzWo2NlgU62FqkQJBAO402yvdxMnXnxeqqooqXLhuFMQhI8rwLacRZGwBymS8w75z0zpqoIda8xsAJ1277NpQ74OurjESGmeK6pmB0MsCQQDVz89DIyn8MgSS/h+r6mIBqigGq9awdLQlOWqiJMswrrn+fsQlwNYjebR7rEReD0O38WRFqtDh8z2S5HS0NtdDAkAbZ9FfdqBRaJFRM71zL/SC84dIGVf43TEja4jWNE6I0H7kxoaizemyKPCXRqlAy4Iwi+dCizJXSvHmv8DF8H2JAkEAgYXbA8vIglt2g+loHEfnSSTgDHKKouh/vNEUdNcfNAWU7MO0ZT51OpZerFDGNci/eC3GVaHN2iL1GQhGXmmeawJBAI42WyQOWjUZhOGgbs24ipEwzlZS4oIWP1klTeRbL8qwik1OWQVzZ+Clo1fRdSlb/O2jsVG9sbEp0ug6Lbb4KfI=";

        ZhongAnApiClient client = new ZhongAnApiClient("dev",
                        "https://test-gateway.zhongan.io/Gateway.dod", appkey,
                prk, "1.0.0", SignTypeEnum.RSA.getCode());
        client.setSalt("TEST");
        Map<String, String> reqHeaders = new HashMap<>();
        reqHeaders.put("Accept", "application/json");
        client.setReqHeaders(reqHeaders);
        CommonRequest request = new CommonRequest("zhongan.xd.scorpio.api.dataapi");
        JSONObject params = new JSONObject();


        params.put("merchantNo","5001");
        params.put("encryptType","PLAIN");
        params.put("tradeType","CMSIMTFAUTH");
        params.put("requestMode","1");
        params.put("serviceId","ZA.XD.AUTH");
        params.put("requestTime","1615341584100");
        params.put("productCode","1234");
        params.put("requestNo", UUID.randomUUID().toString().replaceAll("-",""));

        JSONObject serviceParam = new JSONObject();
        serviceParam.put("name","13984351608");
        serviceParam.put("certNo","522225198908056361");
        serviceParam.put("mobile","冉萍");

        params.put("bizParam",serviceParam);
        request.setParams(params);

        CommonResponse response = (CommonResponse) client.call(request);
        System.err.println(response);
        System.err.println(client.getResHeaders());
    }

}
