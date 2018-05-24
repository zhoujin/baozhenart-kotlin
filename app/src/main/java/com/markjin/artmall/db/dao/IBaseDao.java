package com.markjin.artmall.db.dao;


import java.util.List;

public interface IBaseDao<T> {

    long insert(T entity);

    int update(T entity, T where);

    int delete(T entity);

    List<T> query(T where);

    List<T> query(T where, String orderBy, String startIndex, String limit);

    List<T> query(String sql);
}
