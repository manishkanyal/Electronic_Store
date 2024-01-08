package com.lcwd.Electronic.Store.Eletronic.Store;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Roles;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc            //Required by OpenAPI
public class EletronicStoreApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(EletronicStoreApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository repository;

	@Value("${normal.role.id}")
	private String role_normal_id;

	@Value("${admin.role.id}")
	private String role_admin_id;

	@Override
	public void run(String... args) throws Exception {

		System.out.println(passwordEncoder.encode("abcd"));

		try {

			Roles role_admin = Roles.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Roles role_normal = Roles.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			repository.save(role_admin);
			repository.save(role_normal);

		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
