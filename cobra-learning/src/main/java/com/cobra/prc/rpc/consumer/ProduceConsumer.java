package com.cobra.prc.rpc.consumer;

import com.cobra.prc.rpc.RpcFrame;
import com.cobra.prc.rpc.api.IProductService;
import com.cobra.prc.rpc.api.Product;

/**
 * 消费者
 */
public class ProduceConsumer {
    public static void main(String[] args) throws Throwable{
        IProductService productService = (IProductService) RpcFrame.rpc(IProductService.class);
        Product product = productService.queryById(1002);

        System.out.println(product);
    }
}
