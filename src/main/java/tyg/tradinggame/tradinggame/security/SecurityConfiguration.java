package tyg.tradinggame.tradinggame.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final DataSource dataSource;

    @Autowired
    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/login-form")
                            .loginProcessingUrl("/perform_login")
                            .defaultSuccessUrl("/", true)
                            .failureUrl("/");
                })
                .build();
    }

}
