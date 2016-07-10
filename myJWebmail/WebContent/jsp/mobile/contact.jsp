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

<stripes:form name="contactform" beanclass="pm.matthews.webmail.action.ContactAction">
    <stripes:errors/>
        <core:if test="${actionBean.contacts.isEmpty()}" >    
              <div class="wide"><font><fmt:message key="contacts.nocontact"/></font></div>  
        </core:if>
        <core:if test="${!actionBean.contacts.isEmpty()}" >
            <core:forEach var="c" items="${actionBean.contacts}">
                <div class="light wide">
                  <font>
                    ${fn:escapeXml(c[0])}&nbsp;${fn:escapeXml(c[1])}
                  </font>
                </div>
                <div class="wide">
                    <stripes:link beanclass="pm.matthews.webmail.action.BaseAction" event="compose">
                        <stripes:param name="to" value="${c[2]}"/>
                        <font><b><em>${fn:escapeXml(c[2])}</em></b></font>
                    </stripes:link>
                </div>
                <div class="wide">&nbsp;</div>
            </core:forEach>
        </core:if>       
        <div  class="wide black">&nbsp;</div>
        <div  class="wide">
            <stripes:text name="firstname" />&nbsp;<font><fmt:message key="contact.firstname"/></font>
        </div>
        <div  class="wide">
           <stripes:text name="surname" />&nbsp;<font><fmt:message key="contact.lastname"/></font>
        </div>  
        <div  class="wide">
            <stripes:text  name="email" />&nbsp;<font><fmt:message key="contacts.email"/></font>
        </div>

        <div class="wide black">
            &nbsp;<a class="green" href="#" onclick="contactform.action='Contact.jwma?createContact';contactform.submit();"><fmt:message key="createcontact"/></a>
        </div>
</stripes:form>
</body>
</html>
<%--mobile--%>