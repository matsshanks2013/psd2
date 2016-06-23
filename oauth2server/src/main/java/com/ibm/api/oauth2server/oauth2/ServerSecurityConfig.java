package com.ibm.api.oauth2server.oauth2;

//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ibm.api.oauth2server.db.UserDetailsServiceImpl;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.WebUtils;

@Configuration
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter
{

	@Autowired
	UserDetailsServiceImpl uds;

	@Bean
	public FilterRegistrationBean corsFilter()
	{
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);

		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(-100);
		return bean;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}
	
	@Bean 
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(uds).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/oauth/**").permitAll();
		http.authorizeRequests().antMatchers("/login", "/webjars/**", "/oauth/check_token", "/admin/**")
				.permitAll().anyRequest().authenticated().and().formLogin().permitAll().and().csrf().disable();
		http.requiresChannel().anyRequest().requiresSecure();
		
		// .and().csrf().csrfTokenRepository(csrfTokenRepository())
		// .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		// .and().exceptionHandling()
		// .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
		// .and().formLogin()
		// .and().logout().logoutSuccessUrl("/login").permitAll()
		// .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	}
	
	// @Bean
	// public UsernamePasswordAuthenticationFilter
	// usernamePasswordAuthenticationFilter()
	// throws Exception
	// {
	// UsernamePasswordAuthenticationFilter filter = new
	// UsernamePasswordAuthenticationFilter();
	// filter.setAuthenticationManager(authenticationManagerBean());
	// AuthenticationFailureHandler failureHandler = new
	// SimpleUrlAuthenticationFailureHandler();
	// filter.setAuthenticationFailureHandler(failureHandler);
	// return filter;
	// }	

//	private Filter csrfHeaderFilter()
//	{
//		return new OncePerRequestFilter()
//		{
//			@Override
//			protected void doFilterInternal(HttpServletRequest request,
//					HttpServletResponse response, FilterChain filterChain)
//					throws ServletException, IOException
//			{
//				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//				if (csrf != null)
//				{
//					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//					String token = csrf.getToken();
//					if (cookie == null || token != null && !token.equals(cookie.getValue()))
//					{
//						cookie = new Cookie("XSRF-TOKEN", token);
//						cookie.setPath("/");
//						response.addCookie(cookie);
//					}
//				}
//				filterChain.doFilter(request, response);
//			}
//		};
//	}
//
//	private CsrfTokenRepository csrfTokenRepository()
//	{
//		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		repository.setHeaderName("X-XSRF-TOKEN");
//		return repository;
//	}

}
