package de.hpm.registration.config;

import de.hpm.registration.config.spring.GrantedAuthoritiesExtractor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@EnableWebSecurity
public class MainSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] UNPROTECTED_PATHS = {
            "/actuator/**",
            "/error",
            "/public",
    };

    private static final String CAMUNDA_JERSEY_PATH = "/rest/**";

    private final GrantedAuthoritiesExtractor grantedAuthoritiesExtractor;

    public MainSecurityConfiguration(GrantedAuthoritiesExtractor grantedAuthoritiesExtractor) {
        this.grantedAuthoritiesExtractor = grantedAuthoritiesExtractor;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringAntMatchers(UNPROTECTED_PATHS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(UNPROTECTED_PATHS).permitAll()
                .antMatchers("/rest/**").authenticated()
                .antMatchers(CAMUNDA_JERSEY_PATH).authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(grantedAuthoritiesExtractor);
    }
}
