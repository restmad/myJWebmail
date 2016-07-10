<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
    
<fmt:setLocale value="${language}" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr align="left" valign="top"> 
        <td height="19"><img src="images/logo.png" width="195" height="36" align="right"></img></td>
    </tr>
    <tr align="left" valign="top" bgcolor="#000000"> 
        <td height="19">&nbsp;</td>
    </tr>
</table><br></br>

<p>
    <fmt:message key="exception.message"/>
</p>
<stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="logout">
    <fmt:message key="exception.startover"/>
</stripes:link>
<br><br>

</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 