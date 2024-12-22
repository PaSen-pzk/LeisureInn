package com.sli.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sli.dao.AbstractBaseDO;
import lombok.*;

import javax.persistence.Column;


@Getter
@Setter
@TableName("t_sli_visit_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SliVisitLogDO   extends AbstractBaseDO<SliVisitLogDO> {
    /**
     * 物理主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备类型，手机还是电脑
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 访问
     */
    private String ip;

    /**
     * IP地址
     */
    private String address;

    /**
     * 耗时
     */
    private Integer time;

    /**
     * 调用方法
     */
    private String method;

    /**
     * 参数
     */
    private String params;
}
