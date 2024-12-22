package com.sli.common.limit.slidingwindow;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlidingWindowRateLimit {
    /**
     * 请求的数量
     *
     * @return
     */
    int requests();

    /**
     * 时间窗口，单位为秒
     *
     * @return
     */
    int timeWindow();
}
