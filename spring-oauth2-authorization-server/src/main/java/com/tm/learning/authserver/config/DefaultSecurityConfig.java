package com.tm.learning.authserver.config;

import com.tm.learning.authserver.security.JpaUserDetailsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author gopang
 */

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsManager jpaUserDetailsManager;

    public DefaultSecurityConfig(PasswordEncoder passwordEncoder, JpaUserDetailsManager jpaUserDetailsManager) {
        this.passwordEncoder = passwordEncoder;
        this.jpaUserDetailsManager = jpaUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
     /*  http.sessionManagement(sm ->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Disable CSRF because of state-less session-management
        http.csrf(csrf -> csrf.disable());*/
        http
                //All requests must be authenticated
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                //All requests must be authenticated
                // Utilize the default login page provided by security
                .formLogin().loginPage("/login").permitAll()  ;
        return http.build();

    }


    @Bean
    public DaoAuthenticationProvider jpaDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(jpaUserDetailsManager);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

}
