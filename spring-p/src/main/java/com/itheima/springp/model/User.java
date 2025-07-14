package com.itheima.springp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class User{
    @Id
    private String openid;
    private String nickname;
}