/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.interfaces.servlet;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jzb.execution.shiro.WeixinAuthorizeCodeToken;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jzb
 */
@WebFilter(urlPatterns = "/oauth/weixin")
public class WeixinOauthFilter implements Filter {
    @Inject
    private Logger log;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        Subject subject = SecurityUtils.getSubject();
        String next = req.getParameter("next");
        next = StringUtils.isBlank(next) ? "/execution" : next;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (subject.isAuthenticated()) {
            response.sendRedirect(next);
            return;
        }

        try {
            String oauth = req.getParameter("oauth");
            String code = req.getParameter("code");
            String state = req.getParameter("state");
            if (Objects.equal("WX_OPEN", oauth)) {
                subject.login(new WeixinAuthorizeCodeToken(WeixinAuthorizeCodeToken.Type.OPEN, code, state));
            } else {
                subject.login(new WeixinAuthorizeCodeToken(code, state));
            }
            response.sendRedirect(next);
        } catch (Exception e) {
            log.error("", e);
            response.sendRedirect("login");
        }
    }

    @Override
    public void destroy() {
    }

}
