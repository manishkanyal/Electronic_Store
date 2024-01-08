package com.lcwd.Electronic.Store.Eletronic.Store.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name="users")

//UserDetails interface has been implemented here for authentication and authorisation using database
public class User implements UserDetails
{

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private String userId;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(name="password",length = 500)
    private String password;
    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name="user_image_name")
    private String imageName;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order> orders=new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Roles> roles=new HashSet<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private Cart cart;


    //This is for form based login
    //Authenticating using database

    //THis method is important for setting roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities =this.roles.stream().map(role->new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
