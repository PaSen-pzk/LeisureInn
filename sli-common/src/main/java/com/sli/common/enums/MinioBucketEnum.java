package com.sli.common.enums;

/**
 * Mini枚举o Bucket
 */
public enum MinioBucketEnum {

    /**  **/
    BUCKET_DEFAULT("default", "默认桶"),
    BUCKET_LOG("log", "日志桶"),
    BUCKET_IMAGE("image", "图片桶"),
    BUCKET_FILE("file", "文件桶"),
    BUCKET_VIDEO("video", "视频桶"),
    BUCKET_AUDIO("audio", "音频桶"),
    BUCKET_OTHER("other", "其他桶"),
    BUCKET_TEST("test", "测试桶"),
    BUCKET_TEMP("temp", "临时桶"),
    BUCKET_BACKUP("backup", "备份桶"),
    BUCKET_PRIVATE("private", "私有桶"),
    BUCKET_PUBLIC("public", "公开桶"),

    ;



    private String bucketName;
    private String bucketPath;

    MinioBucketEnum(String bucketName, String bucketPath) {
        this.bucketName = bucketName;
        this.bucketPath = bucketPath;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getBucketPath() {
        return bucketPath;
    }
}
