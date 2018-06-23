
package com.ihome.service;

import java.io.Serializable;

/**
 * service基类 ， 其他的service继承该接口类，就拥有基本的CRUD的能力
 * 功能描述: TODO service基类 ， 其他的service继承该接口类，就拥有基本的CRUD的能力
 * 
 * @逻辑说明: TODO 增加描述代码逻辑
 * 
 * @牵涉到的配置项: TODO 若果代码中逻辑牵涉到配置项在这里列出
 *
 * @编码实现人员 HOLI
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 May 19, 2017
 * @版本 TODO 填写版本
 * @修改历史 TODO 
 *@param <T>
 *@param <ID>
 */
public interface BaseService<T, ID extends Serializable>
{
    /**
     * 设置操作的对象实体
     * TODO 设置操作的对象实体
     * @author HOLI
     * @date May 19, 2017
     */
    void setBaseMapper();

    /**
     * 根据主键删除
     * TODO 根据主键删除
     * @author HOLI
     * @date May 19, 2017
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(ID id);

    /**
     * 增加
     * TODO 增加
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int insert(T record);

    /**
     * 选中不为空的字段进行增加
     * TODO 选中不为空的字段进行增加
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int insertSelective(T record);

    /**
     * 根据主键查询
     * TODO 根据主键查询
     * @author HOLI
     * @date May 19, 2017
     * @param id id
     * @return T
     */
    T selectByPrimaryKey(ID id);

    /**
     * 选中不为空的字段进行修改
     * TODO 选中不为空的字段进行修改
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据主键修改
     * TODO 根据主键修改
     * @author HOLI
     * @date May 19, 2017
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(T record);

}
