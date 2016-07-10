<%@include file="../js/inbox_header.js" %> 
<core:url value="inbox_header.jsp" var="lang">
    <core:param name="locale" value="${actionBean.getUserLanguage()}" />
</core:url>
<fmt:setLocale value="${actionBean.getUserLanguage()}" />
    
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="36" width="80%"> <img src="images/inbox.png"  width="36" height="36" align="middle"/>
        <font face="Arial,Helvetica" size="-1">
          <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="listMessages">
              <stripes:param name="folder" value="INBOX"/>
              <b><fmt:message key="inbox.name"/></b>
          </stripes:link>
          <em> 
              (<label id="unread">${actionBean.unread}</label> 
              <fmt:message key="inbox.status"/> 
              <label id="message">${actionBean.messages}</label>)
          </em>
        </font>
        </td>
        <td height="36" width="20%">
            <img src="images/logo.png" width="195" height="36" align="right" />
            <a name="top"></a>
        </td>
  </tr>
</table>
