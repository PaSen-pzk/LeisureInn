package com.sli.common.enums;

import java.util.EnumSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HotSearchEnum {

    /**
     * 抖音
     */
    DOUYIN("DOUYIN", "抖音"),
    BAIDU("BAIDU", "百度"),
    ZHIHU("ZHIHU", "知乎"),
    SOUGOU("SOUGOU", "搜狗"),
    HUPU("HUPU", "虎扑"),
    TOUTIAO("TOUTIAO", "头条"),
    WEIBO("WEIBO", "微博"),
    TENCENT("TENCENT", "腾讯"),
    JUEJIN("JUEJIN", "掘金"),
    CSDN("CSDN", "CSDN"),
    TIEBA("TIEBA", "贴吧"),
    BILIBILI("BILIBILI", "B站");

    private String code;
    private String desc;

    public static HotSearchEnum of(byte code) {
        return EnumSet.allOf(HotSearchEnum.class).stream().filter(x -> x.code.equals(code)).findFirst().orElse(null);
    }

}
