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

<stripes:form name="sendform" beanclass="pm.matthews.webmail.action.SendAction">
  <stripes:hidden name="from" value="${actionBean.getMailId()[0]}"/>
  <stripes:errors/>
  <table>
    <tr>
      <td  width="65">
        <font>
          <b><fmt:message key="message.to"/></b>
        </font>
      </td>
      <td>
        <stripes:text name="to" value="${actionBean.msg[0]}"/>    
      </td>
    </tr>
    <tr>
      <td  width="65">
        <font>
          <b><fmt:message key="compose.cc"/></b>
        </font>
      </td>
      <td>
        <stripes:text name="cc" value="${actionBean.msg[1]}"/>
      </td>
    </tr>
    <tr>
      <td  width="65">
        <font>
          <b><fmt:message key="compose.bcc"/></b>
        </font>
      </td>
      <td>
        <stripes:text name="bcc"  value="${actionBean.msg[2]}"/>
      </td>
    </tr>
    <tr>
      <td  width="65">
        <font>
          <b><fmt:message key="message.subject"/></b>
        </font>
      </td>
      <td>
        <stripes:text name="subject"  value="${actionBean.msg[3]}"/>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <font>
          <b><fmt:message key="compose.attachments"/></b>
          <stripes:file id="attachment" name="attachment[0]" size="23"/>
        </font>
      </td>
    </tr>        
    <tr>
      <td colspan="2">
          <div class="wide">
              <font>
                  <stripes:textarea name="body" value="${actionBean.msg[4]}" rows="10"/>
              </font>
          </div>
      </td>
    </tr>
  </table>
        
  <div class="wide black tag">
    &nbsp;
    <stripes:checkbox name="autoSign" id="checkbox-2-1" class="regular-checkbox big-checkbox" />
    <label for="checkbox-2-1"></label>
    <font>
      <fmt:message key="compose.toggle.autosign"/>
    </font>
  </div>
  <div class="wide">&nbsp;</div>

  <div class="wide black">
    &nbsp;
    <%--
      FIXME: java bug? impossible to set action with javascript in this page *only* page 
      see the messy workaround in SendAction.java
      <a class="green" href="#" onclick="sendform.action='Send.jwma?sendMail';sendform.submit();"><fmt:message key="sendmail"/></a>
    --%>
    <a class="green" href="#" onclick="sendform.submit();"><fmt:message key="sendmail"/></a>
  </div>
</stripes:form>
</body>
</html>
<%--mobile--%>