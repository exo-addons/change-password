package org.exoplatform.changePassword;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ayoub Zayati
 */

public class ChangePasswordViewServlet extends HttpServlet {
    private static final String CHANGE_PASSWORD_JSP_RESOURCE = "/WEB-INF/jsp/changePassword.jsp";
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doPost(httpServletRequest, httpServletResponse);
    }
    
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(CHANGE_PASSWORD_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
    }
}
