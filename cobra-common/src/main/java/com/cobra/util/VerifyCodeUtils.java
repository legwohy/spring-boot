package com.cobra.util;

import java.awt.*;
import java.util.Random;

/** 
* @ClassName: VerifyCodeUtils 
* @Description: 随机生成随机数图片
* @author wupingjun 
* @date May 16, 2016 3:02:49 PM 
*  
*/
public class VerifyCodeUtils {
	
	static Color getcolor(int forecolor, int backcolor) {
		Random random = new Random();
		if (forecolor > 255)
			forecolor = 255;
		if (backcolor > 255)
			backcolor = 255;
		int red = forecolor + random.nextInt(backcolor - forecolor);
		int green = forecolor + random.nextInt(backcolor - forecolor);
		int black = forecolor + random.nextInt(backcolor - forecolor);
		return new Color(red, green, black);
	}
	

	
}
