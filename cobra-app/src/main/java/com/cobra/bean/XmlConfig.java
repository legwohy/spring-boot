package com.cobra.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 引入xml中的bean
 */
@Configuration
@ImportResource(locations = "bean/applicationContext.xml")
public class XmlConfig
{
}
