package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtRequest {

    private String email;
    private String password;

}
