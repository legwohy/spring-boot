package com.cobra.prc.rpc.provider;

import com.cobra.prc.rpc.api.IProductService;
import com.cobra.prc.rpc.api.Product;


public class ProductServiceImpl implements IProductService{
    @Override
    public Product queryById(Integer id) {
        Product product = new Product();
        product.setId(id);
        product.setName("测试");
        product.setPrice(1.2F);

        return product;
    }
}
