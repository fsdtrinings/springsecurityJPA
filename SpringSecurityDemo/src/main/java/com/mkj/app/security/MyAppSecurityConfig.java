package com.mkj.app.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/*
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
*/
import com.mkj.app.service.AppUserServiceImpl;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;


@EnableWebSecurity
public class MyAppSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 * 1st Run :- execute app through csrf disable() 
		 * csrf() disable does not validate the "post request" form unknown browser
		 * 
		 * 2nd time run :- comment csrf disable() and uncomment crsf() token code
		 * 
		 * our server validate all post requests coming from unknown source
		 * in order to know the source Server save the token in the cookie
		 * 
		 * so make request along with header values which we can copy from 
		 * cookie itself
		 * 
		 * copy value of XSRF-TOKEN which in my case is 62e83a19-3786-4a2f-8b70-7ae36bc6fb77
		 *  save the value in header in key-value pair 
		 *  
		 *  key :- X-XSRF-TOKEN
		 *  value :- 62e83a19-3786-4a2f-8b70-7ae36bc6fb77
		 * 
		 * 
		 * */
		
		 http.
		 	csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		 	.and().
		 	//csrf().disable().cors().disable().
			authorizeRequests().

			antMatchers("/public/**").permitAll().
			antMatchers("/admin/**").hasAuthority("admin").
			antMatchers("/employee/**").hasAnyAuthority("admin","employee").
			
			anyRequest().authenticated().and().httpBasic();
		
		/*
		 http.
		 	csrf().disable().cors().disable().
			authorizeRequests().

			antMatchers("/public/**").permitAll().
			antMatchers("/admin/**").hasRole("admin").
			antMatchers("/employee/**").hasAnyRole("admin","employee").
			
			
		*/	
			
			/* http.authorizeRequests()
	            .anyRequest().authenticated().and().formLogin();
		*/
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("=============>> inside config method auth "+userDetailService);
		
		auth.authenticationProvider(authenticationProvider());
		
	}

	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new AppUserServiceImpl();
    }
	
	 @Bean
	 public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());
	         
	        return authProvider;
	 }
	
	
	
	
	
}//end class
