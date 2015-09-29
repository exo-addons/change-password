package org.exoplatform.changePassword;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.web.filter.Filter;


/**
 * @author Ayoub Zayati
 */

public class ChangePasswordFilter implements Filter {
	private static Log logger = ExoLogger.getLogger(ChangePasswordFilter.class);
    private static final String CHANGE_PASSWORD_SERVLET_CTX = "/changePassword-extension";
    private static final String CHANGE_PASSWORD_SERVLET_URL = "/changePasswordView";
    private static final String INITIAL_URI_PARAM_NAME = "initialURI";
    private static final String REST_URI = ExoContainerContext.getCurrentContainer().getContext().getRestContextName();

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
    	HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
    	OrganizationService organizationService = (OrganizationService)PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
    	Identity identity = ConversationState.getCurrent().getIdentity();
    	String userId = identity.getUserId();
    	boolean logged = false;
    	boolean passwordChanged = false;
    	boolean passwordExpired = false;
    	if (!userId.equals("__anonim")) {
    		logged = true;
            UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
            try {
            	UserProfile userProfile = userProfileHandler.findUserProfileByName(userId);
            	String changePassword = userProfile.getAttribute("changePassword");
            	String expirePasswordDate = userProfile.getAttribute("expirePasswordDate");
            	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
            	Date today = new Date();
            	if (changePassword != null && changePassword.equals("true")) {
                	passwordChanged = true;
                }
//            	passwordExpired = today.after(simpleDateFormat.parse(expirePasswordDate));
            	passwordExpired = today.before(simpleDateFormat.parse("07/Jun/2013"));
            } catch (Exception exception) {
            	logger.error("User profile not found");
            }
        }    
        String requestUri = httpServletRequest.getRequestURI();
        boolean isRestUri = requestUri.contains(REST_URI);
        if (!isRestUri && logged && (!passwordChanged || passwordExpired)) {
            String requestURI = httpServletRequest.getRequestURI();
            String queryString = httpServletRequest.getQueryString();
            if (queryString != null) {
                requestURI += "?" + queryString;
            }
            ServletContext servletContext = httpServletRequest.getSession().getServletContext().getContext(CHANGE_PASSWORD_SERVLET_CTX);
            String targetURI = (new StringBuilder()).append(CHANGE_PASSWORD_SERVLET_URL + "?" + INITIAL_URI_PARAM_NAME + "=").append(requestURI).toString();
            servletContext.getRequestDispatcher(targetURI).forward(httpServletRequest, httpServletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}   
