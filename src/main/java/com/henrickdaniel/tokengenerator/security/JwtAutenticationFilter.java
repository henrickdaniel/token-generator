package com.henrickdaniel.tokengenerator.security;

import com.henrickdaniel.tokengenerator.service.PublicEndpointService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JwtAutenticationFilter extends OncePerRequestFilter {

    private final JwtGenerator tokenGenerator;
    private final CustomUserDetailService customUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(PublicEndpointService.isPublicEndpoint(request)){
            filterChain.doFilter(request, response);
            return;
        }
        String token = getJwtFromRequest(request);
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)){
            log.info("Valid token");
            String userName = tokenGenerator.getUserNameFromJwt(token);
            UserDetails userDetails = customUserDetailService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "USER", userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }else{
            throw new RuntimeException("Invalid token");
        }

    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
