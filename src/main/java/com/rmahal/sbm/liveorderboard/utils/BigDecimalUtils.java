package com.rmahal.sbm.liveorderboard.utils;

import java.math.BigDecimal;

public interface BigDecimalUtils {

    static BigDecimal asBigDecimal(String s) {
        return new BigDecimal(s);
    }
}
