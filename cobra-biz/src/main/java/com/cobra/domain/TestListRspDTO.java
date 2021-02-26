package com.cobra.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @date 2021/1/14 15:28
 * @desc
 */
@Data
public class TestListRspDTO implements Serializable{

    private static final long serialVersionUID = 8198067810566685700L;
    private List<TmpRspDTO> list;
}
