package com.sub.app.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.sub.app.handler.CustomAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private  UserAuthenticationProvider userAuthenticationProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
		auth.authenticationProvider(userAuthenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
		.and()
		.authorizeRequests()
			.anyRequest().authenticated()
			.and()
		.formLogin()
        .failureHandler(customAuthenticationFailureHandler());
		
		http.logout()
		.logoutUrl("/logout")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID").logoutSuccessUrl("/");
		
		http.sessionManagement().sessionFixation().newSession();
		http.sessionManagement().enableSessionUrlRewriting(false);
	}
	
	@Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
	
}
