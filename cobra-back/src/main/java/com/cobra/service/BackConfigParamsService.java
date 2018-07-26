package com.cobra.service;


import com.cobra.pojo.BackConfigParams;

import java.util.List;

public interface BackConfigParamsService
{

    List<BackConfigParams> select(BackConfigParams sysConfig);

    BackConfigParams selectByPrimaryKey(BackConfigParams sysConfig);



}
