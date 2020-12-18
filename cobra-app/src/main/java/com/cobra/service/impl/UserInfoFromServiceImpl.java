package com.cobra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cobra.domain.entity.QueryUserInfoDTO;
import com.cobra.domain.entity.TUserInfoDO;
import com.cobra.domain.entity.UpdateUserInfoDTO;
import com.cobra.mapper.TUserInfoMapper;
import com.cobra.service.UserInfoFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * QueryWrapper 查询
 * UpdateWrapper 更新
 * // TODO 待验证
 */
@Service
public class UserInfoFromServiceImpl implements UserInfoFromService {
    @Autowired
    private TUserInfoMapper userInfoMapper;

    public TUserInfoDO selectByPrimaryKey(Integer id){
        return userInfoMapper.selectById(1);
    }

    public List<TUserInfoDO> query(QueryUserInfoDTO queryUserInfoDTO){
        return userInfoMapper.selectList(
                        new QueryWrapper<TUserInfoDO>()
                                        .eq("userName", queryUserInfoDTO.getUserName())// 等值
                                        .in(queryUserInfoDTO.getUserId() != null, "id", Arrays.asList("1", "2"))// 批量
                                        .orderByDesc("createdAt")// 排序
                                        .groupBy("id")// 分组

        );
    }

    public Boolean update(UpdateUserInfoDTO updateUserInfoDTO){
        return userInfoMapper.update(new TUserInfoDO(), new UpdateWrapper<TUserInfoDO>()
                        .set("id", updateUserInfoDTO.getUserId())) > 0;
    }
}
