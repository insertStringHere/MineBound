package com.mineboundteam.minebound.util;

import java.text.DecimalFormat;

public class StringUtil {
    public static String formatDecimal(Number num) {
        return new DecimalFormat("0.##").format(num);
    }

    public static String pluralize(Number num, String singular) {
        return pluralize(num, singular, singular + "s");
    }

    public static String pluralize(Number num, String singular, String plural) {
        return formatDecimal(num) + " " + (num.doubleValue() == 1 ? singular : plural);
    }

    public static String percentage(double num) {
        return String.format("%.0f%%", num * 100);
    }
}
