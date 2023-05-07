package com.mineboundteam.minebound.util;

import java.text.DecimalFormat;

public class StringUtil {
    public static String pluralize(Number num, String word) {
        return pluralize(num, word, word + "s");
    }

    public static String pluralize(Number num, String singular, String plural) {
        return new DecimalFormat("0.#").format(num) + " " + (num.doubleValue() == 1 ? singular : plural);
    }

    public static String percentage(double num) {
        return String.format("%.0f%%", num * 100);
    }
}
