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
<%@include file="navigation.jsp" %>     
<fmt:setLocale value="${actionBean.getUserLanguage()}" />

<stripes:form name="folderform" beanclass="pm.matthews.webmail.action.FolderAction">
<stripes:hidden name="selection" value="${actionBean.folder}"/>
    <div class="wide dark"><font><fmt:message key="folder.folder"/>:&nbsp;<b>${actionBean.folder}</b></font></div>
    <div class="wide dark">&nbsp;</div>
    <core:if test="${empty(actionBean.messageList)}" >
        <p class="wide"><font><fmt:message key="folder.nomessages"/></font></p>
    </core:if> 
    <core:if test="${!empty(actionBean.messageList)}" >  
        <core:forEach var="array" items="${actionBean.messageList}">       
            <div class="wide light"> 
                <font> ${array[3]}</font>
            </div>
            <div class="wide"> 
                <stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="displayMessage">
                    <stripes:param name="number" value="${array[0]}"/>
                    <stripes:param name="folder" value="${actionBean.folder}"/>
                    <font><b><em>${array[4]}</em></b></font>
                </stripes:link>
            </div>
            <div class="wide">&nbsp;</div>
        </core:forEach>
   </core:if>
   
  <div class="wide black">&nbsp;
   <core:if test="${empty(actionBean.messageList) && actionBean.folder != 'INBOX'}" >   
       <a class="green" href="#" onclick="folderform.action='Folder.jwma?deleteFolder';folderform.submit();"><fmt:message key="delete"/></a>
   </core:if>
  </div>
</stripes:form>
</body>
</html>
<%--mobile--%>