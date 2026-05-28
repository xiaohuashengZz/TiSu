package com.tisu.model.vo;

import lombok.Data;

@Data
public class AdminUserInfoVO {

    private Long id;

    private String nickname;

    private String email;

    private String goal;

    private String registerDate;

    private String status;
}
