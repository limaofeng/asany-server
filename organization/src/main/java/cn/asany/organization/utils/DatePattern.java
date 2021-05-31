package cn.asany.organization.utils;

import lombok.Getter;

/**
 * 日期格式枚举类
 *
 * @author ZhouLuhao
 * @version 1.0
 * @date 2019/3/25 10:51
 */
@Getter
public enum DatePattern {

    YYYY("yyyy"),
    YYYY_ZH_CN("yyyy年"),
    MM("MM"),
    MM_ZH_CN("MM月"),
    DD("dd"),
    DD_ZH_CN("dd日"),
    YYYY_MM_DASH("yyyy-mm"),
    YYYY_MM_SLANT("yyyy/mm"),
    YYYY_MM_ZH_CN("yyyy年mm月"),
    MM_DD_DASH("MM-dd"),
    MM_DD_SLANT("MM/dd"),
    MM_DD_ZH_CN("MM月dd日"),
    HH_MM("HH:mm"),
    HH_MM_ZH_CN("HH时mm分"),
    YYYY_MM_DD("yyyyMMdd"),
    YYYY_MM_DD_DASH("yyyy-MM-dd"),
    YYYY_MM_DD_SLANT("yyyy/MM/dd"),
    YYYY_MM_DD_ZH_CN("yyyy年MM月dd日"),
    YYYY_MM_DD_HH_MM("yyyyMMddHHmm"),
    YYYY_MM_DD_HH_MM_DASH("yyyy-MM-dd HH:mm"),
    YYYY_MM_DD_HH_MM_SLANT("yyyy/MM/dd HH:mm"),
    YYYY_MM_DD_HH_MM_ZH_CN("yyyy年MM月dd日 HH:mm"),
    YYYY_MM_DD_HH_MM_SS("yyyyMMddHHmmss"),
    YYYY_MM_DD_HH_MM_SS_DASH("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_SLANT("yyyy/MM/dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_ZH_CN("yyyy年MM月dd日 HH:mm:ss"),
    HH_MM_SS("HH:mm:ss"),
    HH_MM_SS_ZH_CN("HH时mm分ss秒");

    private String pattern;

    DatePattern(String pattern) {
        this.pattern = pattern;
    }
}