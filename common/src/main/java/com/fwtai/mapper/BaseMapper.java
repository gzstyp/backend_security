package com.fwtai.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

    Integer executeAdd(final T record);

    Integer executeEdit(final T record);

    Integer executeDele(final T record);

    T selectByPrimaryKey(final Integer id);

    T selectByKey(final Integer kid);

    T selectByKey(final String kid);

    Map<String,Object> selectMap(final Integer kid);

    Map<String,Object> selectMap(final String kid);

    List<T> selectPageData(final T params);

    List<T> selectPageData(final Map<String,Object> params);

    List<T> selectPageData(final HashMap<String,Object> params);

    Integer selectPageTotal(final T params);

    Integer selectPageTotal(final Map<String,Object> params);

    Integer selectPageTotal(final HashMap<String,Object> params);

    Integer count(T filter);
}