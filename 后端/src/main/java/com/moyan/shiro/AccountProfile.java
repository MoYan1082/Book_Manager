package com.moyan.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {

    private Long userId;

    private String username;

    private Boolean admine;

}
