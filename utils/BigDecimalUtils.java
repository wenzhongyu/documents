package com.qiandai.fourfactors.common.utils;

import java.math.BigDecimal;

/**
 * Created by yuwenzhong on 2016/8/16.
 */
public class BigDecimalUtils {

    /**
     * 提供精确的乘法运算，并对运算结果截位.
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param v1
     * @param v2
     * @param scale 运算结果小数后精确的位数
     * @return
     */
    public static double multiply(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     *  提供（相对）精确的除法运算.
     * 由scale参数指定精度，以后的数字四舍五入.
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return
     */
    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    public static void main(String[] args) {
//        Double multiply = divide(150, 100, 2);
//        System.out.println(multiply);
//        int value = multiply.intValue();
//        System.out.println(value);
//    }
}
