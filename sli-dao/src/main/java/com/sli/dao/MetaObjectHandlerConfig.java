package com.sli.dao;

import java.util.Calendar;
import java.util.Date;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = Calendar.getInstance().getTime();
        this.fillStrategy(metaObject, "gmtCreate", date);
        this.fillStrategy(metaObject, "gmtModified", date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date date = Calendar.getInstance().getTime();
        this.setFieldValByName("gmtModified", date, metaObject);
    }
}
