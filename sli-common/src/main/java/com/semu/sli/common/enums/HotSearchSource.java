package com.semu.sli.common.enums;

public enum HotSearchSource {

    DOUYIN("douyin","抖音热搜"),
    BAIDU("baidu", "百度热搜"),
    ZHIHU("zhihu", "知乎热搜"),
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
