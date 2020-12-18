package com.cobra.config.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 引入xml中的bean
 */
@Configuration
@ImportResource(locations = "classpath:bean/applicationContext.xml")
public class XmlConfig
{
}
