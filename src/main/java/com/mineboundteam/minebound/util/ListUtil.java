package com.mineboundteam.minebound.util;

import java.util.Random;

public class ListUtil {
    @SafeVarargs
    public static <T> T randomlyChooseFrom(T... values) {
        return values[new Random().nextInt(values.length)];
    }
}
