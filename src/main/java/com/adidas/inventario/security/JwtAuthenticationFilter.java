package com.adidas.inventario.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtUtils jwtUtils,
            CustomUserDetailsService userDetailsService) {

        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null
                && header.regionMatches(
                        true, 0, "Bearer ", 0, 7)
                && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            try {
                Claims claims = jwtUtils.getClaimsFromToken(
                        header.substring(7).strip());

                String username = claims.getSubject();
                String idUsu = claims.get("idUsu", String.class);

                if (username == null || username.isBlank()) {
                    throw new UsernameNotFoundException(
                            "Token sin usuario");
                }

                UserDetails usuario =
                        userDetailsService.loadUserByUsernameAndId(
                                username,
                                idUsu);

                var authentication =
                        UsernamePasswordAuthenticationToken.authenticated(
                                usuario,
                                null,
                                usuario.getAuthorities());

                SecurityContext context =
                        SecurityContextHolder.createEmptyContext();

                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);

            } catch (JwtException
                    | IllegalArgumentException
                    | UsernameNotFoundException ex) {

                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}