package com.tisu.model.vo;

import lombok.Data;

@Data
public class AdminLoginVO {

    private String token;

    private Long adminId;

    private String nickname;

    private String role;
}
