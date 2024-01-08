package com.lcwd.Electronic.Store.Eletronic.Store.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;


//This is a filter which will process if JWT authentication is valid or not
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger=LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader=request.getHeader("Authorization");
        //requestHeader will be like -> Bearer 23354n3j4b53453453jb43kgre234f
        logger.info("Header: "+requestHeader);
        String username=null;
        String token=null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer"))
        {
            //Token looking good
            token=requestHeader.substring(7);
            try{

                username=jwtHelper.getUsernameFromToken(token);

            }
            catch(IllegalArgumentException ex){
                logger.info("Illegal Arguments While fetching Username");
                ex.printStackTrace();
            }
            catch(ExpiredJwtException ex){
                logger.info("Jwt Token is Expired!!");
                ex.printStackTrace();
            }
            catch(MalformedJwtException ex){
                logger.info("Some changes  had been done in token!! Invalid Token");
                ex.printStackTrace();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else {
            logger.info("Invalid Header Value");
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            //Fetching user detail with the help of username
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken=this.jwtHelper.validateToken(token,userDetails);

            if(validateToken)
            {
                //Setting authentication in Security Context
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            else{
                logger.info("Authentication Failed!!");
            }
        }
        filterChain.doFilter(request,response);
    }
}
