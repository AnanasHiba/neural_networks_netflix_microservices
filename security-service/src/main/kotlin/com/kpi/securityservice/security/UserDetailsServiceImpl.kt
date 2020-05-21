package com.kpi.securityservice.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserDetailsServiceImpl: UserDetailsService {

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val users: List<AppUser> = Arrays.asList(
                AppUser(1, "user", encoder.encode("user"), "USER"),
                AppUser(2, "admin", encoder.encode("admin"), "ADMIN")
        )


        for (appUser in users) {
            if (appUser.username.equals(username)) {

                // Remember that Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
                // So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN")).
                val grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList("ROLE_" + appUser.role)

                // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
                // And used by auth manager to verify and check user authentication.
                return User(appUser.username, appUser.password, grantedAuthorities)
            }
        }

        // If user not found. Throw this exception.
        throw UsernameNotFoundException("Username: $username not found")
    }

    private class AppUser(var id: Int, var username: String, var password: String, var role: String)
}