package com.cobra.script.jgroove;

import com.cobra.script.jgroove.pojo.ScriptDTO;
import org.junit.Test;

/**
 * 脚本测试
 * @author admin
 * @date 2021/2/2 17:52
 * @desc
 */
public class ScriptInfoServiceTest {
    ScriptInfoService scriptInfoService = new ScriptInfoService();
    @Test
    public void runScript() throws Exception{
        String srcPlainText = "test";
        String scriptContent = "import CipherCommonUtils;\n"
                        + "\n"
                        + "def getChain() {\n"
                        + "\tString secretKey = \"1234567890123456\"\n"
                        + "\tCipherCommonUtils.doAesEncrypt(data,secretKey)\n"
                        + "\treturn data\n"
                        + "} \n"
                        + "getChain()";
        ScriptDTO filterChainBo = new ScriptDTO();
        filterChainBo.setRequestBody(srcPlainText);
        Object re = scriptInfoService.runScript(filterChainBo, scriptContent);
        System.out.println(re);
    }

}