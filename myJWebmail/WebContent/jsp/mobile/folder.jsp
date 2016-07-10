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

<div class="wide">&nbsp;</div>
<stripes:form name="folderform"  beanclass="pm.matthews.webmail.action.FolderAction">
<stripes:hidden name="newStore" value="0"/>
<stripes:hidden name="folder" value="${fn:escapeXml(folder.getFullName())}"/>
<stripes:errors/>
<div class="wide">
    <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="listMessages">
        <stripes:param name="folder" value="INBOX"/>
        <font><em><b>INBOX</b></em></font>
    </stripes:link>
</div>
<div class="wide">&nbsp;</div>
<core:forEach var="folder" items="${actionBean.mixedStoreList}">
    <core:if test="${folder.getType() eq 1}">
        <div class="wide">
            <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="listMessages">
                <stripes:param name="folder" value="${fn:escapeXml(folder.getFullName())}"/>
                <font><em><b>${fn:escapeXml(folder.getName())}</b></em></font>
            </stripes:link>
        </div> 
        <div class="wide">&nbsp;</div>
     </core:if>
</core:forEach>    
<div class="wide">
    <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="listMessages">
        <stripes:param name="folder" value="${actionBean.trashName}"/>
        <font><em><b>${actionBean.trashName}</b></em></font>
    </stripes:link>
</div>
<div class="wide">&nbsp;</div>
<div class="wide black">
    &nbsp;   
    <a class="green" href="#" onclick="folderform.action='Folder.jwma?createFolder';folderform.submit();"><fmt:message key="createnew"/></a>
    &nbsp;&nbsp;        
    <stripes:text name="storename"/>
</div>
</stripes:form>                                
</body>
</html>
<%--mobile--%>
