package com.cobra.service;

import com.cobra.domain.EncryptBO;
import com.cobra.domain.FactoryBO;
import com.cobra.domain.FormatBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2020/12/30 15:38
 * @desc
 */
@Component
public class EncryptFactory {
    Map<String, PwdFilter> items = new HashMap<>();

    @Autowired
    private void init(List<PwdFilter> filters){
        filters.forEach(filter -> items.put(filter.name(), filter));
    }

    public void execute(FactoryBO factoryBO){
        EncryptBO encryptBO = new EncryptBO();
        encryptBO.setSrc(factoryBO.getSrc());
        for (FormatBO bo : factoryBO.getFormatBOS()) {
            PwdFilter filter = items.get(bo.getEncType());

            encryptBO.setEncType(bo.getEncType());
            encryptBO.setIsTransBigBefore(bo.getIsTransBigBefore());
            encryptBO.setIsTransBigAfter(bo.getIsTransBigAfter());
            filter.before(encryptBO);
            filter.handle(encryptBO);
            filter.after(encryptBO);
        }

        factoryBO.setSrc(encryptBO.getSrc());
    }
}
