package org.exoplatform.changePassword;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;

/**
 * @author Ayoub Zayati
 */

public class ChangePasswordActionServlet extends HttpServlet {
	private static Log logger = ExoLogger.getLogger(ChangePasswordActionServlet.class);
    private static final long serialVersionUID = 1L;
	private static final String CHANGE_PASSWORD_JSP_RESOURCE = "/WEB-INF/jsp/changePassword.jsp";
	private static final int PASSWORD_EXPIRATION_MONTHS_NUMBER = 6;
	
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String newPassword = httpServletRequest.getParameter("newPassword");
        String reNewPassword = httpServletRequest.getParameter("reNewPassword");
        OrganizationService organizationService = (OrganizationService)PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        String userId = httpServletRequest.getRemoteUser();
        try {
        	RequestLifeCycle.begin(PortalContainer.getInstance());
            User user = organizationService.getUserHandler().findUserByName(userId);
            if (!newPassword.equals(reNewPassword)) {
                httpServletRequest.setAttribute("notValidNewPassword", "true");
                getServletContext().getRequestDispatcher(CHANGE_PASSWORD_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
            } 
            else {
                UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
                UserProfile userProfile = userProfileHandler.findUserProfileByName(userId);
                userProfile.setAttribute("changePassword", "true");
                Calendar calendar = Calendar.getInstance();
                String passwordExpirationMonthsNumber = System.getProperty("password.expiration.months.number");
                calendar.add(Calendar.MONTH, passwordExpirationMonthsNumber != null ? Integer.parseInt(passwordExpirationMonthsNumber) : PASSWORD_EXPIRATION_MONTHS_NUMBER);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
                userProfile.setAttribute("expirePasswordDate", simpleDateFormat.format(calendar.getTime()));
                userProfileHandler.saveUserProfile(userProfile, true);
                user.setPassword(newPassword);
                organizationService.getUserHandler().saveUser(user, true);
                //Redirect to the home page
                String redirectURI = "/portal/";
                httpServletResponse.sendRedirect(redirectURI);
            }
        } 
        catch (Exception exception) {
            logger.error("Password not changed");
        } finally {
            RequestLifeCycle.end();
        }
    }
   
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }
}