package com.rainbow.base.config;

import com.rainbow.base.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.rainbow.base.constant.DataConstant.EXCLUDE_PATHS;
import static com.rainbow.base.constant.DataConstant.JWT_HEADER;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private static final PathMatcher pathMatcher = new AntPathMatcher();


  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private JwtConfig jwtConfig;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    try {

      boolean flag = EXCLUDE_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
      logger.debug("#### shouldNotFilter: {} -> {}", path, flag);
      return flag;
    } catch (Exception e) {
      e.printStackTrace();
      logger.debug("#### shouldNotFilter ServletPath: {}", path);
    }
    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {
    String path = request.getServletPath();

    if (shouldNotFilter(request)) {
      chain.doFilter(request, response);
    } else {
      String header = jwtConfig.getHeader();
      final String requestTokenHeader = request.getHeader(header);

      String username = null;
      String jwtToken = null;

      // 从请求头中获取 token
      if (requestTokenHeader != null && requestTokenHeader.startsWith(JWT_HEADER)) {
        jwtToken = requestTokenHeader.substring(7);
        try {
          username = jwtTokenUtil.getUserIdFromToken(jwtToken);

        } catch (Exception e) {
          logger.error("Unable to get JWT Token or JWT Token has expired: {}", e.getMessage());
        }
      } else {
        logger.warn("JWT Token does not begin with Bearer String");
      }

      // 验证 token
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (jwtTokenUtil.validateToken(jwtToken)) {

          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                  username, null, new ArrayList<>());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          logger.warn("Token validation failed for user: {}", username);
        }
      }
      chain.doFilter(request, response);
    }
  }
} 