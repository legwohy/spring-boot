package com.cobra.util;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
	
	/** 
	* @Title: genImgCode 
	* @author wupingjun
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param resp
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public static String genImgCode(HttpServletResponse resp) {
		int width = 70, height = 23;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics grap = image.getGraphics();
		Random random = new Random();
		grap.setColor(new Color(255, 255, 255));
		grap.fillRect(0, 0, width, height);

		grap.setColor(getcolor(200, 250));
		for (int i = 0; i < 10; i++) {
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(12);
			int y2 = random.nextInt(12);
			grap.drawLine(x1, y1, x2, y2);
		}

		grap.setFont(new Font("Times New Roman", Font.BOLD, 18));
		String myRand = "";
		for (int i = 0; i < 4; i++) {
			int index = 0;
			String sourcenum = "123456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";
			index = (int) (Math.random() * 100) % sourcenum.length();
			String rand = sourcenum.substring(index, index + 1);
			myRand = myRand + rand;
			grap.setColor(new Color(10 + random.nextInt(100), 10 + random
					.nextInt(100), 10 + random.nextInt(100)));
			grap.drawString(rand, 13 * i + 6, 16);
		}
		grap.dispose();
		try {
			ImageIO.write(image, "JPEG", resp.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myRand;
	}
	
}
