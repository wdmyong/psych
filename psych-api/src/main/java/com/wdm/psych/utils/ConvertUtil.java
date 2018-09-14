package com.wdm.psych.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author wdmyong
 */
public final class ConvertUtil {

    private ConvertUtil() {
    }

    public long convertStr2Long(String s) {
        if (StringUtils.isBlank(s) || !NumberUtils.isDigits(s)) {
            throw new IllegalArgumentException();
        }
        return NumberUtils.toLong(s, 0);
    }
}
