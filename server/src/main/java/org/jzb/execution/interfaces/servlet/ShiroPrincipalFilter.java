package org.jzb.execution.interfaces.servlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jboss.resteasy.plugins.server.embedded.SimplePrincipal;
import org.jzb.exception.JNonAuthenticationError;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */

public class ShiroPrincipalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public Principal getUserPrincipal() {
                Subject subject = SecurityUtils.getSubject();
                if (subject.isAuthenticated()) {
                    return new SimplePrincipal((String) subject.getPrincipal());
                }
                throw new JNonAuthenticationError();
            }
        }, response);
    }

    @Override
    public void destroy() {

    }
}
