package com.cobra.tomcat;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 嵌入式tomcat容器定制化 实现接口 EmbeddedServletContainerCustomizer
 */
@Configuration
public class TomcatConfiguration implements EmbeddedServletContainerCustomizer
{
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container)
    {
        System.err.println(container.getClass());
        if(container instanceof TomcatEmbeddedServletContainerFactory)
        {
            TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory) container;
            Connector connector = new Connector("HTTP/1.1");
            connector.setURIEncoding("UTF-8");
            factory.addAdditionalTomcatConnectors(connector);

            // 添加上下文 相当于 new TomcatContextCustomizer
            factory.addContextCustomizers(context->{
                if(context instanceof StandardContext){
                    StandardContext standardContext = (StandardContext) context;
                    standardContext.setDefaultContextXml("");
                }
            });
            factory.addConnectorCustomizers();

        }

    }
}
