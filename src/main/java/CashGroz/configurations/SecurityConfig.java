package CashGroz.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //TODO: Improve CSRF filtering
        http.csrf(csrf -> csrf.disable());

        /* 
        http
            .authorizeHttpRequests(authorize -> {
                authorize
                .requestMatchers("/api/login", "/api/signup").permitAll()
                .anyRequest().authenticated();

            })
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());
        */
        return http.build();
    }
}
