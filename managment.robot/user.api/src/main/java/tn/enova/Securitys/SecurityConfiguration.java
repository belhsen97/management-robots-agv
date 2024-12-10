package tn.enova.Securitys;

import tn.enova.Enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler; //LogoutService

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors().and().csrf().disable()
            .authorizeRequests()
            //.anyRequest().permitAll()
             //.authenticated()
             //.antMatchers("/management-robot/trackbot-service/robot").authenticated()
            .antMatchers("/management-robot/trackbot-service/trace/**").permitAll()
            //.antMatchers("/management-robot/trackbot-service/robot/**").permitAll()
            .antMatchers("/management-robot/trackbot-service/robot-setting/**").permitAll()
            .antMatchers("/management-robot/notification-service/notification/**").permitAll()



            .antMatchers(HttpMethod.GET,"/management-robot/trackbot-service/robot/**").permitAll()
            .antMatchers(HttpMethod.POST,"/management-robot/trackbot-service/robot").hasAnyAuthority(Roles.ADMIN.name(),Roles.MAINTENANCE.name())
            .antMatchers(HttpMethod.PUT,"/management-robot/trackbot-service/robot/*").hasAnyAuthority(Roles.ADMIN.name(),Roles.MAINTENANCE.name())
            .antMatchers(HttpMethod.DELETE,"/management-robot/trackbot-service/robot/*").hasAuthority(Roles.ADMIN.name())

            .antMatchers(HttpMethod.GET,"/management-robot/trackbot-service/workstation").permitAll()
            .antMatchers(HttpMethod.POST,"/management-robot/trackbot-service/workstation").hasAnyAuthority(Roles.ADMIN.name(),Roles.MAINTENANCE.name())
            .antMatchers(HttpMethod.PUT,"/management-robot/trackbot-service/workstation/*").hasAnyAuthority(Roles.ADMIN.name(),Roles.MAINTENANCE.name())
            .antMatchers(HttpMethod.DELETE,"/management-robot/trackbot-service/workstation/*").hasAuthority(Roles.ADMIN.name())

            .antMatchers(HttpMethod.GET,"/management-robot/trackbot-service/tag").permitAll()
            .antMatchers(HttpMethod.POST,"/management-robot/trackbot-service/tag").hasAnyAuthority(Roles.ADMIN.name(),Roles.MAINTENANCE.name())
            .antMatchers(HttpMethod.PUT,"/management-robot/trackbot-service/tag/*").hasAnyAuthority(Roles.ADMIN.name(),Roles.MAINTENANCE.name())
            .antMatchers(HttpMethod.DELETE,"/management-robot/trackbot-service/tag/*").hasAuthority(Roles.ADMIN.name())

            .antMatchers(HttpMethod.POST,"/management-robot/mail-service/mail").authenticated()
            .antMatchers("/management-robot/mail-service/mail/all-address-mail").permitAll()



            .antMatchers("/management-robot/user-service/authentication/**").permitAll()

            .antMatchers(HttpMethod.GET, "/management-robot/user-service/user").hasAuthority(Roles.ADMIN.name())
            .antMatchers(HttpMethod.GET, "/management-robot/user-service/user/*").authenticated()
            .antMatchers("/management-robot/user-service/user/username/*").authenticated()
            .antMatchers("/management-robot/user-service/user/authenticate").authenticated()
            .antMatchers(HttpMethod.PUT, "/management-robot/user-service/user/*").authenticated()
            .antMatchers(HttpMethod.DELETE, "/management-robot/user-service/user/*").hasAuthority(Roles.ADMIN.name())
            .antMatchers("/management-robot/user-service/user/photo-to-user/*").authenticated()
            .antMatchers("/management-robot/user-service/user/get-photo-by-id/*").permitAll() // ???????????????????????????????,,
            .antMatchers("/management-robot/user-service/user/get-photo-by-username/*").authenticated()
            .antMatchers("/management-robot/user-service/user/mail-code-forgot-password/*/*").permitAll()
            .antMatchers("/management-robot/user-service/user/update-forgot-password/*/*").permitAll()
            .antMatchers("/management-robot/user-service/user/update-password/*/*/*").permitAll()
            .antMatchers("/management-robot/user-service/user/update-role-and-activate/*/*/*").hasAuthority(Roles.ADMIN.name())
            .antMatchers("/management-robot/user-service/user/set-role/*/*").hasAuthority(Roles.ADMIN.name())
            .antMatchers("/management-robot/user-service/user/set-enable/*/*").hasAuthority(Roles.ADMIN.name())



        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/authentication/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());


    return http.build();
  }
}
