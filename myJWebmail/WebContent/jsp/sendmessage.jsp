<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
<%@include file="./share/inbox_header.jsp" %>
<%@include file="./share/navigation.jsp" %>
<%@include file="./js/sendmessage.js" %> 

<stripes:form id="sendform" name="sendform" beanclass="pm.matthews.webmail.action.SendAction">
  <stripes:hidden name="save" id="save" value="0"/>
  <stripes:hidden name="richText" id="richText" value="false"/>
  <stripes:errors/>
  <table width="90%" border="0" cellspacing="1" cellpadding="0">
    <tr>
      <td colspan="2" bgcolor="#000000" height="20">
        <stripes:image name="compose" src="images/compose_small.png?"/>
        <font face="Arial, Helvetica, sans-serif" color="#FFFFFF">
          <b><fmt:message key="compose.header"/></b>
        </font>&nbsp;&nbsp;&nbsp;
        <span id="rich">
            <a class="rich" href="#" onClick="richtext();"><fmt:message key="compose.text"/></a>
        </span>
      </td>
    </tr>
    <tr>
      <td width="17%" bgcolor="#DDDDDD">
        <font face="Arial, Helvetica, sans-serif">
          <b><fmt:message key="message.to"/></b>
        </font>
      </td>
      <td width="82%" bgcolor="#EEEEEE">
        <stripes:text name="to" size="50" value="${actionBean.msg[0]}"/>    
      </td>
    </tr>
    <tr>
      <td width="17%" bgcolor="#DDDDDD">
        <font face="Arial, Helvetica, sans-serif">
          <b><fmt:message key="compose.cc"/></b>
        </font>
      </td>
      <td width="82%" bgcolor="#EEEEEE">
        <stripes:text name="cc" size="50" value="${actionBean.msg[1]}"/>
      </td>
    </tr>
    <tr>
      <td width="17%" bgcolor="#DDDDDD">
        <font face="Arial, Helvetica, sans-serif">
          <b><fmt:message key="compose.bcc"/></b>
        </font>
      </td>
      <td width="82%" bgcolor="#EEEEEE">
        <stripes:text name="bcc" size="50" value="${actionBean.msg[2]}"/>
      </td>
    </tr>
    <tr>
      <td width="17%" bgcolor="#DDDDDD">
        <font face="Arial, Helvetica, sans-serif">
          <b><fmt:message key="compose.attachments"/></b>
        </font>
      </td>
      <td width="82%" bgcolor="#EEEEEE">
          <stripes:file id="attachment" name="attachment[0]" size="36"/>
          <div id="attachment1">
            <stripes:file name="attachment[1]" size="36"/></br>
            <stripes:file name="attachment[2]" size="36"/>
          </div>
      </td>
    </tr>
    <tr>
      <td width="17%" bgcolor="#DDDDDD">
        <font face="Arial, Helvetica, sans-serif">
          <b><fmt:message key="message.subject"/></b>
        </font>
      </td>
      <td width="82%" bgcolor="#EEEEEE">
        <stripes:text name="subject" size="50" value="${actionBean.msg[3]}"/>
      </td>
    </tr>
    <tr>
      <td colspan="2" bgcolor="#EEEEEE"><br>
          <stripes:textarea id="txtbody" name="body" value="${actionBean.msg[4]}" cols="80" rows="25"></stripes:textarea>
      </td>
    </tr>
    <tr>
      <td colspan="2" bgcolor="#DDDDDD">
      </td>
    </tr>
    <tr>
      <td colspan="2" bgcolor="#000000" height="20">
        <font color="#FFFFFF" face="Arial, Helvetica, sans-serif" size="-1">
          <fmt:message key="compose.mailidentity"/>
        </font>
        <stripes:select name="from">
            <core:forEach var="id" items="${actionBean.getMailId()}">
                <stripes:option label='${id}'></stripes:option>
            </core:forEach>
        </stripes:select> 
      </td>
    </tr>
    <tr>
      <td colspan="2" bgcolor="#000000" height="20">
        <stripes:checkbox name="autoSign"/>
        <font face="Arial, Helvetica, sans-serif" color="#ffffff" size="-1">
          <fmt:message key="compose.toggle.autosign"/>
        </font>
      </td>
    </tr>
    <tr align="right">
      <td colspan="2" bgcolor="#000000" height="20">
        <stripes:reset name="canned" onclick="$('#attachment1').hide();">
            <fmt:message key="reset"/>
        </stripes:reset>&nbsp;&nbsp;
        <stripes:button  id="saveDraft" name="saveDraft" onclick="submitsave();">
            <fmt:message key="savedraft"/>
        </stripes:button>&nbsp;&nbsp;
        <stripes:submit name="sendMail">
            <fmt:message key="sendmail"/>
        </stripes:submit>
      </td>
    </tr>
  </table>
</stripes:form>
<p>&nbsp;</p>
   
</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 