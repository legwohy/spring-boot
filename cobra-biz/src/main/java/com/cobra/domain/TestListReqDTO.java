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
public class TestListReqDTO implements Serializable {
    private static final long serialVersionUID = 6526408915528809261L;
    private String req;

    private List<TmpReqDTO> list;
}
