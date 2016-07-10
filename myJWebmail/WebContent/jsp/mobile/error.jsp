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
<fmt:setLocale value="${actionBean.getUserLanguage()}" />
    
<fmt:setLocale value="${language}" />

<div class="wide black"></div>
<div class="wide"></div>
<p class="wide" >
    <fmt:message key="exception.message"/>
</p>
<div class="wide">
    <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="logout">
        <fmt:message key="exception.startover"/>
    </stripes:link>
</div>
<div class="wide"></div>
<div class="wide black"></div>
</body>
</html>
<%--mobile--%>