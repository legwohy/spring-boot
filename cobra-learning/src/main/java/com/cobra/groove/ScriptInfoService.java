package com.cobra.groove;

import com.cobra.groove.pojo.ScriptDTO;

import javax.script.*;

/**
 * @author admin
 * @date 2021/2/2 17:50
 * @desc
 */
public class ScriptInfoService {
    private final static ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");

    /**
     * 编译
     * @param scriptContent
     * @return
     * @throws ScriptException
     */
    public CompiledScript compileScript(String scriptContent) throws ScriptException{
        Compilable compilable = (Compilable) engine;
        return compilable.compile(scriptContent);
    }
    public Object runScript(Object envArgs, String scriptContent) throws Exception {
        CompiledScript function = compileScript(scriptContent);
        Bindings bindings = engine.createBindings();

        if (envArgs instanceof ScriptDTO) {
            ScriptDTO filterChainBo = (ScriptDTO) envArgs;
            bindings.put("data", filterChainBo);
        } else {
            bindings.put("data", envArgs);
        }
        Object scResult = function.eval(bindings);
        return scResult;
    }
}
