package com.darren.butterknife_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)//生命周期
@Target(ElementType.FIELD)//类型
public @interface BindView {
    int value();
}