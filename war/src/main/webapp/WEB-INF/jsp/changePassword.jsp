<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page import="org.exoplatform.services.resources.ResourceBundleService"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ page language="java" %>
<%
  String contextPath = request.getContextPath() ;

  ResourceBundleService service = (ResourceBundleService) PortalContainer.getCurrentInstance(session.getServletContext())
  														.getComponentInstanceOfType(ResourceBundleService.class);
  ResourceBundle resourceBundle = service.getResourceBundle(service.getSharedResourceBundleNames(), request.getLocale()) ;
  String changePassword = resourceBundle.getString("changePassword.title");
  String newPassword = resourceBundle.getString("changePassword.newPassword");
  String reNewPassword = resourceBundle.getString("changePassword.reNewPassword");
  String send = resourceBundle.getString("changePassword.send");
  String newPasswordError  = resourceBundle.getString("changePassword.newPasswordError");
  String notValidNewPassword = (String) request.getAttribute("notValidNewPassword");
  response.setCharacterEncoding("UTF-8"); 
  response.setContentType("text/html; charset=UTF-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Change password</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="<%=contextPath%>/css/changePassword.css" rel="stylesheet" type="text/css"/>
    </head>
    <body class="change-password">
        <div class="bg-light"><span></span></div>
        <div class="ui-change-password">
            <div class="change-password-container">
                <div class="change-password-header intro-box">
                    <div class="change-password-icon"><%=changePassword%></div>
                </div>
                <div class="change-password-content">
                    <div class="change-password-title">
				        <%
				                if(notValidNewPassword == "true") {
				        %>
                            		<div class="new-password-error"><i class="change-password-icon-error"></i><%=newPasswordError%></div>
                        <%
				                }
                        %> 
                    </div>
                    <div class="center-change-password-content">
                        <form id="changePasswordForm" name="changePasswordForm" action="<%=contextPath%>/changePassword" method="post">
                            <input  id="newPassword" name="newPassword" type="password" placeholder="<%=newPassword%>" onblur="this.placeholder = <%=newPassword%>" onfocus="this.placeholder = ''"/>
                            <input  id="reNewPassword" name="reNewPassword" type="password" placeholder="<%=reNewPassword%>" onblur="this.placeholder = <%=reNewPassword%>" onfocus="this.placeholder = ''"/>
                            <div id="changePasswordFormAction" class="change-password-button" onclick="submit();">
                                <button class="button" href="#"><%=send%></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
