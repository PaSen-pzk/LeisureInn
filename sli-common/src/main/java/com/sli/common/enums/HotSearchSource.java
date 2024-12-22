package com.sli.common.enums;

public enum HotSearchSource {

    DOUYIN("douyin","抖音热榜"),
    BAIDU("baidu", "百度热榜"),
    ZHIHU("zhihu", "知乎热榜"),
    BILIBILI("bilibili", "B站热榜")
    ;

    private String code;
    private String desc;

    HotSearchSource(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
