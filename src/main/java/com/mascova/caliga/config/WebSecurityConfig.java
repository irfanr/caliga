package com.mascova.caliga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mascova.caliga.security.Http401UnauthorizedEntryPoint;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Autowired
	private Http401UnauthorizedEntryPoint authenticationEntryPoint;	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/bower_components/**")
				.antMatchers("/assets/**").antMatchers("/scripts/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll().and()
				.formLogin()
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler)
				// .failureHandler(authenticationFailureHandler)
				.permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").permitAll();

		http.csrf().disable();

		http.exceptionHandling().authenticationEntryPoint(
				authenticationEntryPoint);

		// BEGIN Feature Page
		http.authorizeRequests()
				.antMatchers("/dashboard/media-reach/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_ANALYST",
						"ROLE_CONTRIBUTOR");
		http.authorizeRequests()
				.antMatchers("/dashboard/buzz-trends/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_ANALYST",
						"ROLE_CONTRIBUTOR");
		http.authorizeRequests()
				.antMatchers("/dashboard/sources/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_ANALYST",
						"ROLE_CONTRIBUTOR");
		http.authorizeRequests().antMatchers("/dashboard/analysis/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_ANALYST");
		// END Feature Page

		// BEGIN ADMIN Page
		http.authorizeRequests().antMatchers("/dashboard/keyword/**")
				.hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers("/dashboard/admin/**")
				.hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers("/dashboard/user/**")
				.hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers("/dashboard/client/**")
				.hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers("/dashboard/contributor/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CONTRIBUTOR");
		http.authorizeRequests()
				.antMatchers("/dashboard/image/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_ANALYST",
						"ROLE_CONTRIBUTOR");
		http.authorizeRequests().antMatchers("/dashboard/settings/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_ANALYST");
		// END ADMIN Page

		// The Rest of Dashboard
		http.authorizeRequests()
				.antMatchers("/dashboard/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_ANALYST",
						"ROLE_CONTRIBUTOR");

		// API
		http.authorizeRequests()
				.antMatchers("/api/**")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_ANALYST",
						"ROLE_CONTRIBUTOR");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(
				passwordEncoder());

		auth.inMemoryAuthentication().withUser("user").password("password")
				.roles("USER");
	}	
}
