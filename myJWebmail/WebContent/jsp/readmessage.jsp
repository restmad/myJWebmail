<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
<%@include file="./share/inbox_header.jsp" %>
<%@include file="./share/navigation.jsp" %>
<%@include file="./js/readmessage.js" %> 

<table width="100%">
    <tr bgcolor="#FFFFFF"> 
        <td> 
            <core:set var="link" value="${actionBean.folder}"/>
            <font color="#000000" face="Arial,Helvetica"><img src="images/folder.png" width="36" height="36"  align="middle">
            <core:if test="${actionBean.parent != null}">
                <core:set var="link" value="${fn:substringAfter(actionBean.folder, '/')}"/>
                <stripes:link beanclass="pm.matthews.webmail.action.SubFolderAction" event="list">
                 <font color="#000000"><b>${actionBean.parent}</b></font>
                <stripes:param name="folder" value="${actionBean.parent}"/>
            </stripes:link> 
            <font color="#000000"><b>/</b></font> 
            </core:if>
            <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="list">           
                <font color="#000000"><b>${link}</b></font>
                <stripes:param name="folder" value="${actionBean.folder}"/>
                <stripes:param name="parent" value="${actionBean.parent}"/>
            </stripes:link>
            </font>
        </td>
    </tr>
</table>     
	         
  <table border="0" width="90%" cellspacing="1" cellpadding="1">
    <tr>
      <td colspan="3" align="left" bgcolor="#000000">
        <font color="#FFFFFF" face="Arial, Helvetica, sans-serif">
          <b>
            <%@include file="./share/next_message.jsp" %>
            <img src="images/message_small.png" width="20" height="20" >
            &nbsp;#${actionBean.msg[0]}&nbsp;<fmt:message key="message.date"/>:${actionBean.msg[1]}
          </b>
        </font>
        </font>
        <core:if test="${actionBean.processor < 2}">
            &nbsp;&nbsp;&nbsp;
            <core:set var="event" value="switchToHtml"/>
            <core:if test="${actionBean.processor == -1}">
                <core:set var="event" value="displayMessage"/>
            </core:if>
            <stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="${event}" class="rich" >
                <stripes:param name="folder" value="${actionBean.folder}"/>
                <stripes:param name="parent" value="${actionBean.parent}"/>
                <stripes:param name="number" value="${actionBean.msg[0]}"/>
                <fmt:message key="preference.processor.toggle"/>
            </stripes:link>
        </core:if>
      </td>
    </tr>
    <tr>
      <td align="left" valign="top" width="143" bgcolor="#dddddd">
        <b><fmt:message key="message.from"/>:</b>
      </td>
      <td colspan="2" align="left" valign="top" bgcolor="#eeeeee">
        ${actionBean.msg[2]}
      </td>
    </tr>
    <tr>
      <td align="left" valign=top width="143" bgcolor="#dddddd">
        <b><fmt:message key="message.to"/>:</b>
      </td>
      <td colspan="2" align="left" valign=top bgcolor="#eeeeee">
        ${actionBean.msg[3]}
      </td>
    </tr>
    <tr>
      <td  align=left valign=top width="143" bgcolor="#dddddd" height="22">
        <b><fmt:message key="message.subject"/>:</b>
      </td>
      <td align=left valign=top width="587" bgcolor="#eeeeee" height="22">
        <em>${actionBean.msg[4]}</em>
      </td>
      <td  align=left valign=top width="21" bgcolor="#eeeeee" height="22">
          <form name="headerform">
            <image name="headers" onClick="headertoggle();" src="images/headers_small.png"/>          
          </form>  
        </a>
      </td>
    </tr>
    <tr>
      <td align="left" valign="top" colspan="3" bgcolor="#eeeeee">
          <div id="messageheaders">
              ${actionBean.msg[6]}
          </div>
      </td>
    </tr>
    <tr>  
      <td id="msgbody" align="left" valign="top" colspan="3" bgcolor="#eeeeee">
          ${actionBean.msg[5]}
      </td>
    </tr>
    <tr>
  </table>
  <table border="0" width="90%" cellspacing="0" cellpadding="1">
     <tr colspan="3" bgcolor="#000000" height="24">      
      <td> 
        <stripes:form id="form1" name="form1" beanclass="pm.matthews.webmail.action.FolderAction">
            <stripes:hidden name="selected" value="${actionBean.msg[0]}"/>
            <stripes:hidden name="folder" value="${actionBean.folder}"/>
            <stripes:hidden name="parent" value="${actionBean.parent}"/>  
            <stripes:hidden name="moveTo" value=""/>
            <stripes:submit name="deleteMessage">
                <fmt:message key="delete"/>
            </stripes:submit>  
            &nbsp;&nbsp;
            <stripes:submit name="moveMessage">
                <fmt:message key="moveto"/>
            </stripes:submit>
            <stripes:select name="destination">
                <core:forEach var="f" items="${actionBean.mixedStoreList}">
                    <core:if test="${f.getType() eq 1 && f.getName() ne actionBean.folder}">
                        <stripes:option label="${f.getName()}"></stripes:option>
                    </core:if>
                    <core:if test="${f.getType() ne 1}">
                        <core:forEach var="f" items="${f.list()}">
                            <core:if test="${f.getType() eq 1 && f.getName() ne actionBean.folder}">
                                <stripes:option label="${f.getFullName()}"></stripes:option>
                            </core:if>
                        </core:forEach>
                    </core:if>   
                </core:forEach>
            </stripes:select>   
        </stripes:form>
      </td>
      <td align="right" >
        <%@include file="./share/next_message.jsp" %>
      </td> 
    </tr>
  </table>
  <table width="90%" cellpadding="0" cellspacing="0" border="1">    
    <tr>
      <stripes:form id="form2" name="form2" beanclass="pm.matthews.webmail.action.MessageAction">      
      <td colspan="3" align="left" bgcolor="#000000" height="24">
        <stripes:hidden name="number" value="${actionBean.msg[0]}"/>
        <stripes:hidden name="folder" value="${actionBean.folder}"/>
        <stripes:button name="reply" onclick="replybody()">
            <fmt:message key="replyto"/>
        </stripes:button>
        <stripes:checkbox name="all"/>
        <font face="Arial, Helvetica, sans-serif" color="#ffffff" size="-1">
          <fmt:message key="message.toggle.toall"/>
        </font>
        <stripes:checkbox name="quote"/>
        <font face="Arial, Helvetica, sans-serif" color="#ffffff" size="-1">
          <fmt:message key="message.toggle.autoquote"/>
        </font>
      </td> 
    </tr>
    <tr>
      <td colspan="3" align="left" bgcolor="#000000" height="24" nowrap>
        <stripes:submit name="forward">
            <fmt:message key="forwardto"/>
        </stripes:submit>
        <stripes:text name="fwdaddress" size="30"/>
        &nbsp;&nbsp;
        <stripes:checkbox name="attachment"/>
        <font face="Arial, Helvetica, sans-serif" color="#ffffff" size="-1">
          <fmt:message key="message.toggle.withattachments"/>
        </font>
      </td>
    </stripes:form>  
    </tr>
  </table>
  <p>&nbsp;</p>
</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 