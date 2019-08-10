package com.cobra.design.structor;

/**
 * 门面模式
 * 1） 简化接口，聚合子系统的实现
 * 2）门面接口和子系统接口自动组合实现
 * 3） tomcat
 * {@link org.apache.catalina.connector.RequestFacade}
 * {@link org.springframework.web.servlet.DispatcherServlet}
 *
 *
 */
public class FacadeDemo
{
    public static void main(String[] args)
    {

    }
    private static class ServiceA{
        public void save(){}
    }
    private static class ServiceB{
        public void delete(){}
    }
    /** 由于接口的单一职责 使得接口之间没有相互依赖 因此需要聚合使用才能实现功能 */
    private static class ServiceFacade{
        private ServiceA serviceA;
        private ServiceB serviceB;
        public void service(){}
    }
}
