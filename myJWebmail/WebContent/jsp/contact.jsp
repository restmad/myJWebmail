<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
<%@include file="./share/inbox_header.jsp" %>
<%@include file="./share/navigation.jsp" %>
<%@include file="./js/contact.js" %>

<stripes:form id="contactform" name="contactform" beanclass="pm.matthews.webmail.action.ContactAction">
    <stripes:hidden name="emailDel"  value=""/>
    <stripes:errors/>
    <table width="95%" cellpadding="1" cellspacing="1" border="0">
        <tr valign="bottom">
            <td bgcolor="#000000" colspan="2">
               <stripes:image src="images/address_small.png" name="address_small"/>
               <font face="Arial,Helvetica" color="#FFFFFF"><b><fmt:message key="contacts.contacts"/></b></font>
            </td>
            <td align="right" valign="bottom" bgcolor="#000000" nowrap>
              <font face="Arial,Helvetica" color="#ffffff" size="-1">
              <!--<b>filter would go here</b>-->
              </font>
              &nbsp;&nbsp;             
            </td>
        </tr>
        <tr bgcolor="#dadada">
            <td nowrap width="5%">
                <stripes:label for="selected">
                    <font face="Arial,Helvetica"><b>#</b></font>
                </stripes:label>
            </td>
            <td nowrap width="50%">
              <font color="#000000" face="Arial,Helvetica">
                <b><fmt:message key="contacts.fullname"/></b>
              </font>
            </td>
            <td nowrap width="45%">
              <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="contacts.email"/></b></font>
            </td>
        </tr>
        <core:if test="${actionBean.contacts.isEmpty()}" >
        <tr>   
            <td bgcolor="#eeeeee" colspan="3">
              <i><fmt:message key="contacts.nocontact"/></i>
            </td>
        </tr>    
        </core:if>
            <core:if test="${!actionBean.contacts.isEmpty()}" >
                <core:forEach var="c" items="${actionBean.contacts}">
                    <tr> 
                        <td bgcolor="#eeeeee" width="5%">
                            <stripes:checkbox name="selected" value="${c[2]}"/>
                        </td>
                        <td bgcolor="#eeeeee" nowrap width="50%">
                          <font face="Arial,Helvetica" size="-1">
                            <b> ${fn:escapeXml(c[0])}&nbsp;${fn:escapeXml(c[1])}</b>
                          </font>
                        </td>
                        <td bgcolor="#eeeeee" nowrap width="45%">
                            <stripes:link beanclass="pm.matthews.webmail.action.BaseAction" event="compose">
                                <stripes:param name="to" value="${c[2]}"/>
                                <font face="Arial,Helvetica" size="-1"><b><em>${fn:escapeXml(c[2])}</em></b></font>
                            </stripes:link>
                        </td>
                    </tr> 
                </core:forEach>
            </core:if>
        </tr>

        <tr> 
            <td bgcolor="#000000" height="3" width="5%">&nbsp;</td>
            <td bgcolor="#000000" height="3" width="50%">&nbsp;</td>
            <td bgcolor="#000000" height="3" width="45%">&nbsp;</td>
        </tr>
        <tr>
            <td bgcolor="#eeeeee" width="5%"><i><fmt:message key="add"/></i></td>
            <td bgcolor="#eeeeee" nowrap width="50%">
                <stripes:text name="firstname" size="20"/>
                &nbsp;
                <stripes:text name="surname" size="20"/>
            </td>
            <td bgcolor="#eeeeee" nowrap width="45%">
                <stripes:text name="email" size="35"/>
            </td>
        </tr>
        <tr align="right">
            <td colspan="3" bgcolor="#000000">
                <stripes:button name="deleteContacts" onclick="contactDelete();">
                    <fmt:message key="deletecontacts"/>
                </stripes:button>
                <stripes:submit name="createContact">
                    <fmt:message key="createcontact"/>
                </stripes:submit>
            </td>
        </tr>
    </table>
</stripes:form>
 <br>
 
</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 