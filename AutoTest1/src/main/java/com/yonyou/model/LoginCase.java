package com.yonyou.model;

import lombok.Data;
import org.mybatis.spring.SqlSessionTemplate;

@Data
public class LoginCase {
    //private int id;
    private String username;
    private String password;
    private String text;
    private String status;


}
