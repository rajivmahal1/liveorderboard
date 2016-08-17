package com.rmahal.sbm.liveorderboard.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BigDecimalUtilsTest {

    @Test
    public void creates_fromString() {

        assertThat(asBigDecimal("1.234"), is(new BigDecimal("1.234")));
    }
}
