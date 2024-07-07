package com.ehizman.auth_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class AppSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .successHandler(myAuthenticationSuccessHandler())
                        .usernameParameter("username")
                        .failureUrl("/login?loginError=true"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logoutSuccess=true")
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

}