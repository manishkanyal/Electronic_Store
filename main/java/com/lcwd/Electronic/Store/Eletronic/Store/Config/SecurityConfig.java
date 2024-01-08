package com.lcwd.Electronic.Store.Eletronic.Store.Config;

import com.lcwd.Electronic.Store.Eletronic.Store.Security.JwtAuthenticationFilter;
import com.lcwd.Electronic.Store.Eletronic.Store.Security.JwtEntryAuthenticationPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)     //By using this can tell each method what role user can access
public class SecurityConfig {


    //Object will come from CustomUserDetailsService class because this class extends UserDetailsService
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtEntryAuthenticationPoint jwtEntryAuthenticationPoint;

    //You can put URLS here which you want to be publically accessible
    private final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v2/api-docs",
            "/test"
    };

    //Hard coded login credentials , we are creating this because here we can configure a lot of different things regarding user like roles
    //@Bean
    /*public UserDetailsService userDetailsService(){

        //Creating User
        //this User class is from org.springframework.security.core.userdetails.User that is part of springboot security
        //This way is hard coded in memory by manually writing in security config file.
       UserDetails normal = User.builder()
                .username("Manish")
                .password(passwordEncoder().encode("manish@123"))    //It is mandatory in springboot to save password in encoded form
                .roles("Normal")
                .build();

        UserDetails admin = User.builder()
                .username("Anshu")
                .password(passwordEncoder().encode("anshu@123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(normal,admin);

    }*/


    @SuppressWarnings("deprecated")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        //1- This is for Form based login Customisation
        /*http
                .authorizeHttpRequests((authorizeHttpRequests)->
                authorizeHttpRequests.anyRequest()
                        .authenticated())
                .formLogin(formLogin->
                        formLogin.loginPage("login.html")
                                .loginProcessingUrl("/process-url")
                                .defaultSuccessUrl("/dashboard")
                                .failureUrl("/error"))
                .logout(logout->
                        logout.logoutUrl("/logout"));*/


        //2- Basic Authentication
        /*http.csrf(csrf-> csrf.disable())
                .cors(cors->cors.disable())
                .authorizeHttpRequests((authorizeHttpRequests)->
                        authorizeHttpRequests.anyRequest()
                                .authenticated())
                .httpBasic(Customizer.withDefaults());  //When httpBasic() is called, we are telling Spring to authenticate the request using the values passed by the Authorization request header. If the request is not authenticated you will get a returned status of 401  and a error message of Unauthorized
        */

        //Setting up JWT token
        http.csrf(csrf-> csrf.disable())
                .cors(cors->cors.disable())
                .authorizeHttpRequests(auth->auth.requestMatchers("/auth/login").permitAll())   //We need to make /login API Public so users can access it without authentication and send request to get authenticated
                .authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.POST,"/users").permitAll())
                .authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")) //only admin can use HTTPMethod.DELETE
                .authorizeHttpRequests(auth->auth.requestMatchers(PUBLIC_URLS).permitAll())    //Here String List is provided for public APIS
                .authorizeHttpRequests((authorizeHttpRequests)->
                        authorizeHttpRequests.anyRequest()
                                .authenticated())
                .exceptionHandling(exception->exception.authenticationEntryPoint(jwtEntryAuthenticationPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    //This need to be setup for authenticating from database
    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
