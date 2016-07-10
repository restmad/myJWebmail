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
<script type='text/javascript' src='jsp/mobile/fittext.js'></script>
<fmt:setLocale value="${actionBean.getUserLanguage()}" />

<div class="wide">&nbsp;</div> 	         
<div class="wide">
    <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="list">           
        <font><b><em>${actionBean.folder}</em></b></font>
        <stripes:param name="folder" value="${actionBean.folder}"/>
    </stripes:link>
</div>
<div class="wide">&nbsp;</div>
<div class="wide black">
    <font><b>&nbsp;${actionBean.msg[1]}</b></font>
    &nbsp;&nbsp;&nbsp;
    <core:set var="event" value="switchToHtml"/>
    <core:if test="${actionBean.processor == -1}">
        <core:set var="event" value="displayMessage"/>
    </core:if>
    <stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="${event}" class="green" >
        <stripes:param name="folder" value="${actionBean.folder}"/>
        <stripes:param name="number" value="${actionBean.msg[0]}"/>
        <fmt:message key="preference.processor.toggle"/>
    </stripes:link>
</div>
<div class="wide dark">
    <font><fmt:message key="message.from"/>:&nbsp;<b>${actionBean.msg[2]}</b></font>
</div>
<div  class="wide light">
    <font><fmt:message key="message.subject"/>:&nbsp;<b>${actionBean.msg[4]}</b></font>
</div>

<div class="wide">&nbsp;</div>
<div class="wide" id="fit">
    <font> ${actionBean.msg[5]}</font>
</div>    

<stripes:form name="form2" beanclass="pm.matthews.webmail.action.MessageAction">      
<stripes:hidden name="number" value="${actionBean.msg[0]}"/>
<stripes:hidden name="folder" value="${actionBean.folder}"/>
    <div class="wide black tag">
        &nbsp;<a class="green" href="#" onclick="form2.action='Message.jwma?reply';form2.submit();"><fmt:message key="replyto"/></a>
        &nbsp;&nbsp;
        <stripes:checkbox name="quote" id="checkbox-2-1" class="regular-checkbox big-checkbox" />
        <label for="checkbox-2-1"></label>
        &nbsp;
        <font>
          <fmt:message key="message.toggle.autoquote"/>
        </font>
    </div>
</stripes:form>
<div class="wide">&nbsp;</div>
<stripes:form id="form3" name="form3" beanclass="pm.matthews.webmail.action.MessageAction">
    <stripes:hidden name="number" value="${actionBean.msg[0]}"/>
    <stripes:hidden name="folder" value="${actionBean.folder}"/>
    <stripes:hidden name="attachment" value="true"/>
    <div class="wide black">
        &nbsp;<a class="green" href="#" onclick="form3.action='Message.jwma?forward';form3.submit();"><fmt:message key="forwardto"/></a>
        <stripes:text name="fwdaddress" size="15"/>
    </div>
</stripes:form>  
<div class="wide">&nbsp;</div> 
<stripes:form id="form1" name="form1" beanclass="pm.matthews.webmail.action.FolderAction">
    <stripes:hidden name="selected" value="${actionBean.msg[0]}"/>
    <stripes:hidden name="folder" value="${actionBean.folder}"/>
    <div class="wide black">
        &nbsp;<a class="green" href="#" onclick="form1.action='Folder.jwma?deleteMessage';form1.submit();"><fmt:message key="delete"/></a>
    </div>
    <div class="wide">&nbsp;</div> 
    <div class="wide black tag button-holder">
        &nbsp;<a class="green" href="#" onclick="form1.action='Folder.jwma?moveMessage';form1.submit();"><fmt:message key="moveto"/></a>
        <core:forEach var="f" items="${actionBean.mixedStoreList}">
            <core:if test="${f.getType() eq 1 && f.getName() ne actionBean.folder}">
                <div>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <stripes:radio name="destination" value="${f.getName()}" id="radio-2-1"  class="regular-radio big-radio"/>
                    <label class="radio-1"></label>
                    <font>${f.getName()}</font>
                </div>
            </core:if>   
        </core:forEach>
    </div>        
</stripes:form>
  
</body>
</html>
<%--mobile--%>