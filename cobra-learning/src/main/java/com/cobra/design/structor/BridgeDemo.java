package com.cobra.design.structor;

/**
 * 桥接模式
 * {@link java.sql.Statement}
 */
public class BridgeDemo
{
    public static void main(String[] args)
    {

        ScanService scanService = new ScanServiceImpl(()->{System.out.println("JD 扫码购物");},null);
        // 调用
        scanService.buy();

        scanService = new ScanServiceImpl(()->{System.out.println("TB 扫码购物");}, null);

        // 初始化传什么参数就是什么参数 相当于构造传参时就已经顶死了 而不是通过判断选择
        scanService.buy();
    }


}
class ScanServiceImpl implements ScanService{
    private ScanBuyService scanBuyService;
    private ScanLoginService scanLoginService;

    /** 构造器注入*/
    public ScanServiceImpl(ScanBuyService scanBuyService,ScanLoginService scanLoginService)
    {
        this.scanBuyService = scanBuyService;
        this.scanLoginService = scanLoginService;
    }

    @Override
    public void buy()
    {
        scanBuyService.buy();
    }

    @Override
    public void login()
    {
        scanLoginService.login();
    }
}

/** 扫描登陆和扫描支付*/
 interface ScanService{
    void buy();
    void login();
}
 interface ScanBuyService{
    void buy();
}
 interface ScanLoginService{
    void login();
}
