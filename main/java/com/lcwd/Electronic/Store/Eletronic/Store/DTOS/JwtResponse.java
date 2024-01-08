package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtResponse {

    private String jwtToken;
    private UserDTO user;

}
