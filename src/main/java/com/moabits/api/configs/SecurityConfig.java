package com.moabits.api.configs;

import com.moabits.api.filters.AuthenticationFilter;
import com.moabits.api.filters.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.authorizeRequests().antMatchers("/login/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/register/**").permitAll();
        httpSecurity.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/client/**").hasAnyAuthority("ADMIN");
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/movement/**").hasAnyAuthority("ADMIN");

        httpSecurity.addFilter(new AuthenticationFilter(authenticationManagerBean()));
        httpSecurity.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.cors(request -> new CorsConfiguration().applyPermitDefaultValues());
    }
}
