package com.MultiModule.User.DTO;


import lombok.Data;

@Data
public class ChangePassDTO {

    private String email;

    private String password;

    private String newPassword;

    private String resetToken;
}
