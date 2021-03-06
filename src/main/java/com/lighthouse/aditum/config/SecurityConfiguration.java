package com.lighthouse.aditum.config;

import com.lighthouse.aditum.security.*;
import com.lighthouse.aditum.security.jwt.*;

import io.github.jhipster.security.*;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,
            TokenProvider tokenProvider,
        CorsFilter corsFilter) {

        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure().and()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(http401UnauthorizedEntryPoint())
        .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/admin-infos").permitAll()
            .antMatchers("/api/vehicules/findByCompanyAndPlate/{companyId}/{licensePlate}").permitAll()
            .antMatchers("/api/residents/findByCompanyAndIdentification/{companyId}/{identificationNumber}").permitAll()
            .antMatchers("/api/houses/housesByLoginCode/{loginCode}").permitAll()
            .antMatchers("/api/payments/file/{paymentId}").permitAll()
            .antMatchers("/api/payments/incomeReport/file/{initial_time}/{final_time}/{companyId}/{account}/{paymentMethod}/{houseId}/{category}").permitAll()
            .antMatchers("/api/collections/file/{companyId}/{year}").permitAll()
            .antMatchers("/api/mensualReport/file/{mensualReportObject}").permitAll()
            .antMatchers("/api/anualReport/file/{anualReportObject}").permitAll()
            .antMatchers("/api/egresses/file/{egressObject}").permitAll()
            .antMatchers("/api/egresses/reportEgressToPay/file/{final_time}/byCompany/{companyId}").permitAll()
            .antMatchers("/api/visitants/file/{initial_time}/{final_time}/{companyId}/{houseId}").permitAll()
            .antMatchers("/api/bancos/accountStatus/file/{first_month_day}/{final_capital_date}/{initial_time}/{final_time}/{accountId}").permitAll()
            .antMatchers("/api/accountStatus/file/{accountStatusObject}/{option}").permitAll()
            .antMatchers("/api/charges/chargesToPay/file/{final_time}/{type}/byCompany/{companyId}").permitAll()
            .antMatchers("/api/companies/{id}").permitAll()
            .antMatchers("/api/users").permitAll()
            .antMatchers("/api/users/updateWithPassword").permitAll()
            .antMatchers("/api/vehicules").permitAll()
            .antMatchers("/api/createUserWithoutSendEmail").permitAll()
            .antMatchers("/api/residents").permitAll()
            .antMatchers("/api/regulations").permitAll()
            .antMatchers("/api/houses").permitAll()
            .antMatchers("/api/brands").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/websocket/**").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").permitAll()
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
