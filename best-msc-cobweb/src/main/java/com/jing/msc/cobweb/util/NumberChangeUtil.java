package com.jing.msc.cobweb.util;


import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.util
 * @date : 2022/3/22 17:20
 * @description :
 */
public class NumberChangeUtil {
    /**
     * 最大支持9千兆
     */
    private static String[] NUMBER_ZH = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    /**
     * 最大支持9千兆
     */
    private static String[] NUMBER_UNIT = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "兆", "十", "百", "千"};

    /**
     * @param number
     * @return
     */
    public static String digital2Chinese(long number) {
        long value = number;
        boolean isNegative = false;
        if (value < 0) {
            value = -value;
            isNegative = true;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(value);

        String valueStr = value + "";

        Integer[] digits = new Integer[valueStr.length()];

        //循环数存储每一位数字，从低到高
        for (int i = 0; i < valueStr.length(); i++) {
            //value.intValue()%10;
            digits[i] = bigDecimal.divideAndRemainder(BigDecimal.valueOf(10))[1].intValue();
            bigDecimal = bigDecimal.divide(BigDecimal.valueOf(10));
        }
        StringBuilder sb = new StringBuilder();

        // 从高到低
        CollectionUtils.reverseArray(digits);

        boolean flush = false;
        boolean needFilling = true;

        if (digits.length > 16) {
            throw new ArrayIndexOutOfBoundsException("数字太大了，超出汉字可读范围");
        }

        for (int i = 0; i < digits.length; i++) {
            if (digits[i].equals(0)) {
                if (needFilling) {
                    if (digits.length - i - 1 == 4) {
                        sb.append("万");
                        needFilling = false;
                    }
                    if (digits.length - i - 1 == 8) {
                        sb.append("亿");
                        needFilling = false;
                    }
                    if (digits.length - i - 1 == 12) {
                        sb.append("兆");
                        needFilling = false;
                    }
                }

                flush = true;
                continue;
            }
            if (flush) {
                sb.append("零");
                flush = false;
            }
            sb.append(NUMBER_ZH[digits[i]]).append(NUMBER_UNIT[digits.length - i - 1]);
            needFilling = !Arrays.asList(4, 8, 12).contains(digits.length - i - 1);
        }

        if (isNegative) {
            return "负" + sb;
        }
        return sb.toString();
    }

}
