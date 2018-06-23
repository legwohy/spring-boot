
package com.xjx.service;

import static com.xjx.constant.ErrorCode.ERROR;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xjx.exception.ServiceException;
import com.xjx.mapper.BaseMapper;
import com.xjx.utils.DateUtils;
import com.xjx.utils.UUidGenIdUtil;

public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);

    private BaseMapper<T, ID> baseMapper;

    // @Autowired
    public void setBaseMapper(BaseMapper<T, ID> baseMapper)
    {
        this.baseMapper = baseMapper;
    }

    @Override
    public int deleteByPrimaryKey(ID id)
    {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(T record)
    {
        insertModelInitial(record);
        return baseMapper.insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(ID id)
    {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T record)
    {
        updateModelInitial(record);
        return baseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record)
    {
        updateModelInitial(record);
        return baseMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insert(T record)
    {
        insertModelInitial(record);
        return baseMapper.insert(record);
    }

    private void insertModelInitial(T record)
    {
        Class<?> clazz = record.getClass();
        try
        {
            Method getId = clazz.getMethod("getId");
            Object id = getId.invoke(record);

            if (id == null && "java.lang.String".equals(getId.getReturnType().getName()))
            {
                Method setId = clazz.getMethod("setId", getId.getReturnType());
                setId.invoke(record, UUidGenIdUtil.genId32AndDate());
            }
        }
        catch (Exception e)
        {
            String error = record.getClass().getName() + ".ID自动赋值发生错误，找不到ID属性。";
            LOGGER.error(error, e);
            throw new ServiceException(ERROR.getCode(), error);
        }
        try
        {

            Date now = new Date(DateUtils.getCurrentTimeMillis());
            Method getCreateTime = clazz.getMethod("getCreateTime");
            Object createDate = getCreateTime.invoke(record);

            if (createDate == null)
            {
                Method setCreateTime = clazz.getMethod("setCreateTime", getCreateTime.getReturnType());
                if ("java.lang.String".equals(getCreateTime.getReturnType().getName()))
                {
                    setCreateTime.invoke(record, DateUtils.dateToString(now));
                }
                else
                {
                    setCreateTime.invoke(record, now);
                }
            }

            // Method getCreateBy = clazz.getMethod("getCreateBy");
            // Object createBy = getCreateBy.invoke(record);
            //
            // if (createBy == null || isEmpty(createBy.toString())) {
            // Method setCreateBy = clazz.getMethod("setCreateBy",
            // getCreateBy.getReturnType());
            // setCreateBy.invoke(record, UserLoginUtils.getCurrentUsername());
            // }
            Method getUpdateTime = clazz.getMethod("getUpdateTime");
            Object lastUpdateDate = getUpdateTime.invoke(record);

            if (lastUpdateDate == null)
            {
                Method setUpdateTime = clazz.getMethod("setUpdateTime", getUpdateTime.getReturnType());
                if ("java.lang.String".equals(getUpdateTime.getReturnType().getName()))
                {
                    setUpdateTime.invoke(record, DateUtils.dateToString(now));
                }
                else
                {
                    setUpdateTime.invoke(record, now);
                }
            }

            // Method getLastUpdateBy = clazz.getMethod("getLastUpdateBy");
            // Object lastUpdateBy = getLastUpdateBy.invoke(record);

            // if (lastUpdateBy == null || isEmpty(lastUpdateBy.toString())) {
            // Method setLastUpdateBy = clazz.getMethod("setLastUpdateBy",
            // getLastUpdateBy.getReturnType());
            // setLastUpdateBy.invoke(record, UserLoginUtils.getCurrentUsername());
            // }
        }
        catch (Exception e)
        {
            LOGGER.error(e.toString());
        }
    }

    private void updateModelInitial(T record)
    {
        try
        {
            Date now = new Date(DateUtils.getCurrentTimeMillis());
            Class<?> clazz = record.getClass();
            Method getUpdateTime = clazz.getMethod("getUpdateTime");
            Method setUpdateTime = clazz.getMethod("setUpdateTime", getUpdateTime.getReturnType());
            setUpdateTime.invoke(record, now);
            // Method getLastUpdateBy = clazz.getMethod("getLastUpdateBy");
            // Method setLastUpdateBy = clazz.getMethod("setLastUpdateBy",
            // getLastUpdateBy.getReturnType());
            // setLastUpdateBy.invoke(record, UserLoginUtils.getCurrentUsername());
        }
        catch (Exception e)
        {
            LOGGER.error(e.toString());
        }
    }
}
