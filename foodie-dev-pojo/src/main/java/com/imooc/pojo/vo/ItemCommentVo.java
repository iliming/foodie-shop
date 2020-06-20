package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ItemCommentVo {
    private String userFace;

    private String nickname;

    private Date createdTime;

    private String content;

    private String specName;

    private Integer commentLevel;
}
