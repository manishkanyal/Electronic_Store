package com.lcwd.Electronic.Store.Eletronic.Store.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    private String roleId;

    private String roleName;

}
