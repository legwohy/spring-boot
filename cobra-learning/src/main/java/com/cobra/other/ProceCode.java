package com.cobra.other;

/**
 * 算法与复杂度
 * <a href='https://blog.csdn.net/zolalad/article/details/11848739'>算法的时间复杂度和空间复杂度</a>
 * 1、阶乘求和
 * 1！+2！+...+100!
 * z←0
 * t←0
 * for x←1 to 100
 *  for y←1 to x
 *      t=t*y
 *   end
 *   z←z+t
 * end
 *
 * 2、等比数列求和
 * a^1+a^2+...+a^n
 * x←0
 * y←0
 * for x←1 to n
 *   y←y+a^x
 * end
 * 3、获取手续费(不算零头 比如 13 收取2)
 * if x<=2
 *      return 0
 *  else if x>2 and x<=10
 *      return 2
 *  else if x>10
 *      return x/10 *2
 *
 *  4、a b c 最值
 *  if a>b then max←a,min←b else max←b,min←a
 *  if c>max then max←c
 *  if c<min then min←c
 *
 * 5、自然升序(取出一个值一次排队) 冒泡排序 时间复杂度 O(n^2)
 * for m←1 to 100
 *  min←A[m]
 *  for n←x+1 to 100
 *     if min>A[n] then min←A[n]
 *   end
 *   A[m]←min
 * end
 *
 * 6 插入排序
 * 自然升序
 * mid←0
 * min←1
 * max←100
 * if x<A[min] then mid←min
 * else if x>A[max] then mid←max
 * mid←(min+max)/2
 * if x>A[mid] then min←mid
 *  else max←mid
 *  冒泡排序
 *
 */
public class ProceCode
{
}
