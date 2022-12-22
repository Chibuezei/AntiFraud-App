package antifraud.security;

import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = false)
//@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecure extends WebSecurityConfigurerAdapter {

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    UserService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.httpBasic()
//                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
//                .and()
//                .csrf().disable().headers().frameOptions().disable()
//                .and()
//                .sessionManagement()
//               .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }

    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .mvcMatchers("/actuator/shutdown", "/h2-console/*").permitAll() // needs to run test
                .mvcMatchers("/api/antifraud/transaction").hasAnyAuthority("MERCHANT")
                .mvcMatchers("/api/auth/list").hasAnyAuthority("ADMINISTRATOR", "SUPPORT")
                .mvcMatchers("/api/antifraud/suspicious-ip/**").hasAnyAuthority("SUPPORT")
                .mvcMatchers("/api/antifraud/stolencard/**").hasAnyAuthority("SUPPORT")
                .mvcMatchers("/api/auth/user/**", "/api/auth/role", "/api/auth/access").hasAnyAuthority("ADMINISTRATOR")
//                .mvcMatchers("/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
