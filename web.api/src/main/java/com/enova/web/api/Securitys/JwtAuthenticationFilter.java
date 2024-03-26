package com.enova.web.api.Securitys;


import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Repositorys.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt;
        String username = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);


        try {
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                boolean isTokenValid = userRepository.isExistsValidToken(jwt);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            MsgReponseStatus errorDetails = MsgReponseStatus.builder().title("Expired JWT Token").status(ReponseStatus.ERROR).datestamp(new Date()).message(e.getMessage()).build();
            response.setContentType("application/json");
            response.getWriter().write("{\"title\":\"" + errorDetails.getTitle() + "\",\"status\":\"" + errorDetails.getStatus() + "\",\"datestamp\":\"" + errorDetails.getDatestamp().toString() + "\",\"message\":\"" + errorDetails.getMessage() + "\"}");
            return;
        } catch (MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            MsgReponseStatus errorDetails = MsgReponseStatus.builder().title("Expired JWT Token").status(ReponseStatus.ERROR).datestamp(new Date()).message(e.getMessage()).build();
            response.setContentType("application/json");
            response.getWriter().write("{\"title\":\"" + errorDetails.getTitle() + "\",\"status\":\"" + errorDetails.getStatus() + "\",\"datestamp\":\"" + errorDetails.getDatestamp().toString() + "\",\"message\":\"" + errorDetails.getMessage() + "\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
