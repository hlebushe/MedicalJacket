package org.isf.config;

import org.isf.component.OhWebAccessDeniedHandler;
import org.isf.component.OhWebUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final OhWebAccessDeniedHandler accessDeniedHandler;
	private final OhWebUserDetailsService userDetailsService;
	private final BCryptPasswordEncoder passwordEncoder;

	public WebSecurityConfiguration(BCryptPasswordEncoder passwordEncoder, OhWebAccessDeniedHandler accessDeniedHandler, OhWebUserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.accessDeniedHandler = accessDeniedHandler;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String loginPage = "/login";
		String logoutPage = "/logout";
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login", "/docs", "/api/**", "/register", "/swagger-resources/**", "/v1/api/**", "/v2/api-docs", "/webjars/**", "/test").permitAll()
			.antMatchers("/home").hasAnyAuthority("ADMIN", "GUEST")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage(loginPage)
			.failureUrl("/login?error=true")
			.defaultSuccessUrl("/home")
			.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
			.logoutSuccessUrl(loginPage)
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/status/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
}
