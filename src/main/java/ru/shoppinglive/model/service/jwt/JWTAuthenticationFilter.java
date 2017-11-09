package ru.shoppinglive.model.service.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        try {
            Authentication authentication = TokenAuthenticationService
                    .getAuthentication((HttpServletRequest)request);
            if(authentication!=null) {
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
            filterChain.doFilter(request,response);
        }catch (JwtException exception){
            HttpServletResponse webResponse = (HttpServletResponse)response;
            webResponse.sendError(403);
        }
    }
}
