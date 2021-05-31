package de.hpm.registration.config.spring;

import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {

    public static final String SPRING_ROLE_PREFIX = "ROLE_";

    private static final String ROLE_DECLARATIONS = "groups";
    private static final String CLAIMS = "claims";

    @SuppressWarnings("unchecked")
    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        final Collection<String> clientAuthorities = getClientAuthorities(jwt);

        return clientAuthorities.stream()
                .map(s -> SPRING_ROLE_PREFIX + s)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    public static List<String> getClientAuthorities(ClaimAccessor jwt) {
        final List<String> clientAuthorities = new ArrayList<>();
        Map<String, Object> clientClaims = jwt.getClaims();
        clientAuthorities.addAll(jwt.getClaimAsStringList("groups"));
        return clientAuthorities;
    }

    static List<String> extractRoles(String client, List<String> clientObject) {
        final List<String> clientRoles = clientObject;
        if (clientRoles != null) {
            return clientRoles;
        } else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    static List<String> extractRoles(String client, JSONObject clientObject) {
        final Collection<String> clientRoles = (Collection<String>) clientObject.get(ROLE_DECLARATIONS);
        if (clientRoles != null) {
            return clientRoles
                    .stream()
                    .map(role -> role + "")
                    .collect(toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static List<String> extractRoles(Principal principal) {
        if (principal instanceof Authentication) {
            return ((Authentication) principal).getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> StringUtils.removeStart(role, SPRING_ROLE_PREFIX))
                    .collect(toList());
        }
        return emptyList();
    }
}
