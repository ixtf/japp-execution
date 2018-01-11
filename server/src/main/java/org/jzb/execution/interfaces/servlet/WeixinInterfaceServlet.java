package org.jzb.execution.interfaces.servlet;

import org.jzb.execution.application.ApplicationEvents;
import org.jzb.weixin.mp.MpClient;
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

/**
 * Created by jzb on 17-4-15.
 */
@WebServlet(urlPatterns = "/WeixinInterfaceServlet")
public class WeixinInterfaceServlet extends HttpServlet {
    @Inject
    private Logger log;
    @Inject
    private MpClient mpClient;
    @Inject
    private ApplicationEvents applicationEvents;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            String encrypt_type = req.getParameter("encrypt_type");
            String msg_signature = req.getParameter("msg_signature");
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce");

            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String postData = sb.toString();
            String xml = "aes".equalsIgnoreCase(encrypt_type) ? mpClient.decryptMsg(msg_signature, timestamp, nonce, postData) : postData;
            applicationEvents.fireWeixinMsgPush(xml);
            out.write("");
        } catch (Exception ex) {
            log.error("", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            String signature = req.getParameter("signature");
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce");
            String echostr = req.getParameter("echostr");
            String s = mpClient.verifyUrl(signature, timestamp, nonce, echostr);
            out.write(s);
        } catch (Exception ex) {
            log.error("", ex);
            throw new RuntimeException(ex);
        }
    }
}

