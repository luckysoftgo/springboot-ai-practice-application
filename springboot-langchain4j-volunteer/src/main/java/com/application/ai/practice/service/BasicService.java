package com.application.ai.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BasicService<T> extends IService<T> {

    T getById(Long id);

    boolean saveEntity(T entity);

    boolean updateEntity(T entity);

    boolean removeByIds(Long ids);

    LambdaQueryWrapper<T> getLambdaWrapper();

}