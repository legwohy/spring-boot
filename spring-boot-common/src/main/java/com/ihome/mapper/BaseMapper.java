
package com.xjx.mapper;

import java.io.Serializable;

/**
 * 
 * 功能描述: CRUD基类
 * @编码实现人员 lihong
 * @实现日期 2017年11月30日
 */
public interface BaseMapper<T, ID extends Serializable>
{
    /**
     * 根据主键删除
     *  根据主键删除
     * @author HOLI
     * @date May 19, 2017
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(ID id);
    /**
     * 增加
     *  增加
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int insert(T record);
    /**
     * 选中不为空的字段进行增加
     *  选中不为空的字段进行增加
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int insertSelective(T record);
    /**
     * 根据主键查询
     *  根据主键查询
     * @author HOLI
     * @date May 19, 2017
     * @param id id
     * @return T
     */
    T selectByPrimaryKey(ID id);
    /**
     * 选中不为空的字段进行修改
     *  选中不为空的字段进行修改
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(T record);
    /**
     * 根据主键修改
     *  根据主键修改
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(T record);
}