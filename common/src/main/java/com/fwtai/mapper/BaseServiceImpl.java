package com.fwtai.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    public abstract BaseMapper<T> getMapper();

    @Override
    public Integer executeAdd(final T record) {
        return getMapper().executeAdd(record);
    }

    @Override
    public Integer executeEdit(final T record) {
        return getMapper().executeEdit(record);
    }

    @Override
    public Integer executeDele(final T record) {
        return getMapper().executeDele(record);
    }

    @Override
    public T selectByPrimaryKey(final Integer id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public T selectByKey(final Integer id) {
        return getMapper().selectByKey(id);
    }

    @Override
    public T selectByKey(final String id) {
        return getMapper().selectByKey(id);
    }

    @Override
    public Map<String,Object> selectMap(final Integer kid){
        return getMapper().selectMap(kid);
    }

    @Override
    public Map<String,Object> selectMap(final String kid){
        return getMapper().selectMap(kid);
    }

    @Override
    public List<T> selectPageData(final T params){
        return getMapper().selectPageData(params);
    }

    @Override
    public List<T> selectPageData(final Map<String,Object> params){
        return getMapper().selectPageData(params);
    }

    @Override
    public List<T> selectPageData(final HashMap<String,Object> params){
        return getMapper().selectPageData(params);
    }

    @Override
    public Integer selectPageTotal(final T params){
        return getMapper().selectPageTotal(params);
    }

    @Override
    public Integer selectPageTotal(final Map<String,Object> params){
        return getMapper().selectPageTotal(params);
    }

    @Override
    public Integer selectPageTotal(final HashMap<String,Object> params){
        return getMapper().selectPageTotal(params);
    }

    @Override
    public Integer count(final T filter) {
        return getMapper().count(filter);
    }
}