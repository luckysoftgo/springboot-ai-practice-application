package com.application.ai.practice.service.impl;


import com.application.ai.practice.service.BasicService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public abstract class BasicServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BasicService<T> {

    @Override
    public T getById(Long id) {
        return super.getById(id);
    }

    @Override
    public boolean saveEntity(T entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateEntity(T entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeByIds(Long ids) {
        return super.removeById(ids);
    }

    @Override
    public LambdaQueryWrapper<T> getLambdaWrapper() {
        return new LambdaQueryWrapper<>();
    }
}
