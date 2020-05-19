package com.kpi.securityservice.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
class SecurityCredentialsConfig: WebSecurityConfigurerAdapter() {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtConfig: JwtConfig

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint { _, rsp, _ ->  rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)}
        .and()
                // Add a filter to validate user credentials and add token in the response header

                // What's the authenticationManager()?
                // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
                // The filter needs this auth manager to authenticate the user.
                .addFilter(JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
        .authorizeRequests()
                // allow all POST requests
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                // any other requests must be authenticated
                .anyRequest().authenticated()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }


    @Bean
    fun jwtConfig(): JwtConfig? {
        return JwtConfig()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}