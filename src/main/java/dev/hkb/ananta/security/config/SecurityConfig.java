package dev.hkb.ananta.security.config;

import dev.hkb.ananta.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception{
        http.authorizeHttpRequests(configurer ->
                        configurer
                                // ---Health---
                                .requestMatchers("/ping").permitAll()

                                // ---Swagger---
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll()
                                // ---Authentication---
                                .requestMatchers("/auth/**").permitAll()

                                // ---User & Profile---
                                .requestMatchers("/users/me").hasRole("CUSTOMER")
                                .requestMatchers("/users").hasRole("ADMIN")

                                // ---Address Book---
                                .requestMatchers("/addresses/**").hasRole("CUSTOMER")

                                // ---Seller Management---
                                .requestMatchers("/seller/applications").hasRole("CUSTOMER")
                                .requestMatchers("/seller/me").hasRole("SELLER")

                                // ---Manufacturer Catalog Management---
                                .requestMatchers(HttpMethod.GET,"/manufacturers/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/manufacturers").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/manufacturers/**").hasRole("ADMIN")

                                // ---Category Management---
                                .requestMatchers(HttpMethod.GET,"/category/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/category").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/category/**").hasRole("ADMIN")

                                // ---Tags Management---
                                .requestMatchers(HttpMethod.POST, "/tags").hasAnyRole("ADMIN","SELLER")
                                .requestMatchers(HttpMethod.GET, "/tags").hasAnyRole("ADMIN","SELLER","CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/tags/**").hasRole("ADMIN")

                                // ---Product Management---
                                .requestMatchers(HttpMethod.GET,"/products/{id}/images").permitAll()
                                .requestMatchers(HttpMethod.POST,"/products").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/products").hasAnyRole("ADMIN","SELLER","MANUFACTURER")
                                .requestMatchers(HttpMethod.POST, "/products/applications").hasRole("SELLER")
                                .requestMatchers("/products/pending").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/products/approve/*","/products/*/images").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/products/{id}/images").hasAnyRole("ADMIN","MANUFACTURER")


                                // ---Review Management---
                                .requestMatchers(HttpMethod.GET,"/products/*/reviews").permitAll()
                                .requestMatchers("/products/*/reviews").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.GET,"/products/reviews").hasRole("CUSTOMER")

                                // ---Seller Product Management---
                                .requestMatchers("/seller-products/browse").permitAll()
                                .requestMatchers("/seller-products/me").hasRole("SELLER")
                                .requestMatchers(HttpMethod.PUT,"/seller-products/**").hasRole("SELLER")
                                .requestMatchers(HttpMethod.DELETE,"/seller-products/**").hasRole("SELLER")
                                .requestMatchers("/seller-products/**").hasAnyRole("SELLER","CUSTOMER","ADMIN")

                                // ---Cart Management---
                                .requestMatchers("/cart/**").hasRole("CUSTOMER")

                                // ---Order Mangement---
                                .requestMatchers("/orders/**").hasRole("CUSTOMER")

                                // ---Payment Management---
                                .requestMatchers("/payments/callback").permitAll()
                                .requestMatchers("/payments/**").hasRole("CUSTOMER")

                                .anyRequest().authenticated()
        )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

}
