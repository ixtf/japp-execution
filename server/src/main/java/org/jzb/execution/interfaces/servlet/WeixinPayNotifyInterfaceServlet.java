package org.jzb.execution.interfaces.servlet;

import org.jzb.execution.application.EnlistFeedbackService;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.pay.sl.PaySlNotifyResult;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.jzb.social.core.Constant.XML_MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@WebServlet(urlPatterns = "/WeixinPayNotifyInterfaceServlet")
public class WeixinPayNotifyInterfaceServlet extends HttpServlet {
    @Inject
    private Logger log;
    @Inject
    private MpClient mpClient;
    @Inject
    private EnlistFeedbackService enlistFeedbackService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            final PaySlNotifyResult result = enlistFeedbackService.weixinPaySlNotify(sb.toString());
            out.write(XML_MAPPER.writeValueAsString(result));
        } catch (Exception ex) {
            log.error("", ex);
            throw new RuntimeException(ex);
        }
    }
}

