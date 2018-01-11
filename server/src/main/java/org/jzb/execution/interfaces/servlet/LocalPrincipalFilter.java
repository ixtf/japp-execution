package org.jzb.execution.interfaces.servlet;

import org.jboss.resteasy.plugins.server.embedded.SimplePrincipal;
import org.jzb.execution.domain.operator.Login;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */

public class LocalPrincipalFilter implements Filter {
    @Inject
    private OperatorRepository operatorRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public Principal getUserPrincipal() {
                Login login = operatorRepository.findLoginByLoginId("13957100995");
//                Login login = operatorRepository.findLoginByLoginId("13456978427");
                return new SimplePrincipal(login.getId());
            }
        }, response);
    }

    @Override
    public void destroy() {

    }
}
