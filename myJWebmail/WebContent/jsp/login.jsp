<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
     
    <fmt:setLocale value="${language}" />
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr align="left" valign="top"> 
            <td height="19"><img src="images/logo.png" width="195" height="36"  align="right"></img></td>
        </tr>
        <tr align="left" valign="top" bgcolor="#000000">
            <td height="19"> 
                &nbsp;<a class="rich" href="ScreenSize.jwma?mobile"><fmt:message key="login.mobile"/></a>          
            </td>
        </tr>
    </table><br></br>
    
    <stripes:form beanclass="pm.matthews.webmail.action.LoginAction">
        <stripes:hidden name="mobile" value="false"/>
        <table border="0" align="center" cellpadding="1" cellspacing="1">
            <tr valign="bottom">
                <td colspan="2" bgcolor="#000000" height="20">
                    <img src="images/login_small.png" width="21" height="20" align="right"></img>
                    <font face="Arial, Helvetica, sans-serif" size="+1" color="FFFFFF"><b><fmt:message key="authenticate"/></b></font>
                </td>
            </tr>
            <tr>
                <td width="22%" bgcolor="#dddddd">
                     <font face="Arial, Helvetica"><fmt:message key="login.username"/>:</font>
                </td>
                <td width="78%" bgcolor="#eeeeee">
                    <stripes:text name="username" value="${actionBean.lastLogin}" size="25"/>
                </td>
            </tr>
            <tr>
                <td width="22%" bgcolor="#dddddd">
                     <font face="Arial, Helvetica"><fmt:message key="login.password"/>:</font>
                </td>
                <td width="78%" bgcolor="#eeeeee">
                     <stripes:password name="password" size="25" />
                </td>
            </tr>
            <tr>
                <td bgcolor="#000000" align="left" height="20">
                    <stripes:checkbox name="remember"/>
                    <font face="Arial, Helvetica, sans-serif" color="#ffffff" size="-1">
                      <fmt:message key="login.remember"/>
                    </font>
                </td>
                <td bgcolor="#000000" align="right" height="20">
                    <stripes:reset name="reset"/>
                    &nbsp;&nbsp;&nbsp;
                    <stripes:submit name="authenticate"/>
                </td>
            </tr>
        </table>
</stripes:form><br></br>
</stripes:layout-component>

<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 
 

