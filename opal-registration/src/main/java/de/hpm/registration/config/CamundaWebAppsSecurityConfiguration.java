package de.hpm.registration.config;

import de.hpm.registration.config.camunda.OAuthContainerBasedAuthenticationProvider;
import de.hpm.registration.config.camunda.RestExceptionHandler;
import de.hpm.registration.config.spring.GrantedAuthoritiesExtractor;
import de.hpm.registration.config.spring.TokenParsingOAuth2UserService;
import de.hpm.registration.config.spring.TokenParsingOidcUserService;
import org.camunda.bpm.engine.rest.impl.CamundaRestResources;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.singletonMap;

/**
 * A separate {@code WebSecurityConfigurerAdapter} that applies only to the camunda webapps. It configures the SSO role required to access the webapps,
 * integrates the webapp security with spring security and adds an OAuth2 login so that unauthenticated users are redirected to the SSO login page.
 * This works together with the {@code spring.security.oauth2.client.registration.*}
 * and {@code spring.security.oauth2.client.provider.*} configuration properties.
 */
@Configuration
@Order(90)
public class CamundaWebAppsSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // The paths used by camunda webapps. These are the paths that our HttpSecurity applies to
    private static final String[] CAMUNDA_APP_PATHS = {"/login/**", "/app/**", "/api/**", "/lib/**", "/login/**"};

    private final TokenParsingOAuth2UserService oAuth2UserService;

    public CamundaWebAppsSecurityConfiguration(GrantedAuthoritiesExtractor grantedAuthoritiesExtractor) {
        this.oAuth2UserService = new TokenParsingOAuth2UserService(grantedAuthoritiesExtractor);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .requestMatchers().antMatchers(CAMUNDA_APP_PATHS).and()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .hasRole("USERS")
                .and().oauth2Client()
                .and().oauth2Login()
                .authorizationEndpoint()
                .baseUri("/app" + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .oidcUserService(new TokenParsingOidcUserService(oAuth2UserService));

    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<ContainerBasedAuthenticationFilter> containerBasedAuthenticationFilterRegistrationBean() {
        FilterRegistrationBean<ContainerBasedAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(new ContainerBasedAuthenticationFilter());
        registrationBean.setInitParameters(singletonMap(ProcessEngineAuthenticationFilter.AUTHENTICATION_PROVIDER_PARAM, OAuthContainerBasedAuthenticationProvider.class.getName()));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST));
        return registrationBean;
    }

    @PostConstruct
    public void replaceRestExceptionHandler() {
        Set<Class<?>> configurationClasses = CamundaRestResources.getConfigurationClasses();
        configurationClasses.remove(org.camunda.bpm.engine.rest.exception.RestExceptionHandler.class);
        configurationClasses.add(RestExceptionHandler.class);
    }
}
