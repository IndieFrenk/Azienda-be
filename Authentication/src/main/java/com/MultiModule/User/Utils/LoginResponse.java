package com.MultiModule.User.Utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter @Setter
@RequiredArgsConstructor
public class LoginResponse {

    private String email;
    private long id;

}