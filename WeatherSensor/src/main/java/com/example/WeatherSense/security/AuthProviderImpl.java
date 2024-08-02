package com.example.WeatherSense.security;


import com.example.WeatherSense.services.PersonDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthProviderImpl(PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder) {
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetails personDetails = personDetailsService.loadUserByUsername(username);

        String password = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(password, personDetails.getPassword())){
            throw new BadCredentialsException("Incorrect password!");
        }
        return new UsernamePasswordAuthenticationToken(personDetails, password, personDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
