<%@ page session="true" language="java"%>
<%--mobile--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML Basic 1.1//EN"
"http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0;" />
    <meta name="HandheldFriendly" content="True" />
    <link rel="stylesheet" type="text/css" href="jsp/mobile/jwma.css"/>
    <title>jwma WebMail</title>
</head>
<body>
<%@include file="../share/taglib.jsp" %>
<fmt:setLocale value="${language}" />

    <stripes:form name="login" beanclass="pm.matthews.webmail.action.LoginAction">
        <stripes:hidden name="mobile" value="true"/>
        <br/><br/>
        <table>
            <tr>
                <td class="black" colspan="2">
                    <font class="large"><b><fmt:message key="authenticate"/></b></font>
                    <a class="small" href="ScreenSize.jwma?desktop"><fmt:message key="login.desktop"/></a>
                </td>
            </tr>
            <tr>
                <td class="dark">
                     <font><fmt:message key="login.username"/>:</font>
                </td>
                <td class="light">
                    <stripes:text name="username" value="${actionBean.lastLogin}"/>
                </td>
            </tr>
            <tr>
                <td class="dark">
                     <font face="Arial, Helvetica"><fmt:message key="login.password"/>:</font>
                </td>
                <td class="light">
                     <stripes:password name="password"/>
                </td>
            </tr>
            <tr>
                <td class="black" colspan="2">
                    <stripes:submit name="authenticate"/>&nbsp;
                    <a class="green" href="#" onclick="login.submit();"><fmt:message key="authenticate"/></a>
                </td>
            </tr>
        </table>
</stripes:form>
</body>
</html>
<%--mobile--%>
