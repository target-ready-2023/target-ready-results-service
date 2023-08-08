package com.target.targetreadyresultsservice.ConstantConfig;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DateTimeConfig {
    public static final int DAY_BEFORE_AC_START = 31;
    public static final int MONTH_BEFORE_AC_START = 5;
    public static final int DAY_AFTER_AC_END = 1;
    public static final int MONTH_AFTER_AC_END = 4;

    public static final int HOUR_BEFORE_DAY_START = 8;
    public static final int MINUTE_BEFORE_DAY_START = 59;
    public static final int HOUR_AFTER_DAY_END = 16;
    public static final int MINUTE_AFTER_DAY_END = 0;
}
