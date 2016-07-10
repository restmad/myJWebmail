<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
<%@include file="./share/inbox_header.jsp" %>
<%@include file="./share/navigation.jsp" %>
<%@include file="./js/messagelist.js" %> 

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
  <stripes:errors/>       
<table width="90%" cellpadding="0" cellspacing="0" border="0">
    <tr bgcolor="#000000" width="100%">
        <td width="15%" height="35">
            <img src="images/mailbox_small.png" width="20" height="20" />
            <b><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"><fmt:message key="folder.messages"/></font></b>
        </td>
        <td width="12%">
            <stripes:form id='form1' name="form1" beanclass="pm.matthews.webmail.action.FolderAction">  
                <stripes:hidden id="sortOrder" name="sortOrder" value="${actionBean.sort}"/> 
                <stripes:hidden name="folder" value="${actionBean.folder}"/>
                <stripes:button name="select_all"  onclick='selectall()'>
                     <fmt:message key="selectAll"/>        
                </stripes:button >				    
        </td>
        <td width="13%" align="right">
                <font size="-1" face="Arial, Helvetica, sans-serif" color="#ffffff">
                    <fmt:message key="folder.sortedby"/>
                </font>&nbsp;&nbsp;
        </td>
        <td width="20%">
                <stripes:select name="listMessages" id="listMessages" onclick="sort_order()">
                    <core:forEach var="opt" items="${actionBean.options}"  varStatus="counter">
                        <stripes:option value="${counter.count}"><fmt:message key='${opt}'/></stripes:option>
                    </core:forEach>
                </stripes:select> 
            </stripes:form>
        </td>
        <td width="10%" align="right">
            <stripes:form id="form2" name="form2" beanclass="pm.matthews.webmail.action.FolderAction">
                <stripes:hidden name="folder" value="${actionBean.folder}"/>
                <stripes:hidden name="parent" value="${actionBean.parent}"/>
                <stripes:submit name="searchwhat"  onclick="searchwhat()">
                      <fmt:message key="searchWhat"/>        
                </stripes:submit>   
        </td>   
        <td width="30%">  
                &nbsp;
                <stripes:text id="searchkey" name="searchkey" value="" size="20"/> 
            </stripes:form>
        </td>
    </tr> 
 </table>  

<stripes:form name="form3" beanclass="pm.matthews.webmail.action.FolderAction">
<stripes:hidden name="folder" id="folder" value="${actionBean.folder}"/>
<stripes:hidden name="parent" value="${actionBean.parent}"/>
<stripes:hidden name="moveTo" value=""/>
<div id="list">
<table width="90%" cellpadding="1" cellspacing="1" border="0"> 
    <core:if test="${empty(actionBean.messageList)}" >
        <tr>
            <td><p><fmt:message key="folder.nomessages"/></p></td>
        </tr>
    </core:if> 
    <core:if test="${!empty(actionBean.messageList)}" >   
     <tr bgcolor="#dadada">
        <td nowrap width="1%"> 
            <stripes:label for="selected">
                <font face="Arial,Helvetica"><b><fmt:message key="message.number"/></b></font>
            </stripes:label>
        </td>
        <td nowrap width="1%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="folder.flags"/></b></font>
        </td>
        <td nowrap width="1%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="folder.attachment"/></b></font>
        </td>
        <td nowrap width="40%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="folder.who"/></b></font>
        </td>
        <td nowrap width="40%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="message.subject"/></b></font>
        </td>
        <td nowrap width="16%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="message.date"/></b>
        </font></td>
        <td nowrap width="1%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="message.size"/></b></font>
        </td>
    </tr>

    <core:forEach var="array" items="${actionBean.messageList}">
    <tr>   
        <td bgcolor="#eeeeee" width="1%"> 
            <stripes:checkbox name="selected" value="${array[0]}"/>
        </td>
        <td bgcolor="#eeeeee" nowrap width="1%">
            <font face="Arial,Helvetica" size="-1">${array[1]}</font>
        </td>
        <td bgcolor="#eeeeee" nowrap width="1%">
            <font face="Arial,Helvetica" size="+1">${array[2]}</font>
        </td>
        <td bgcolor="#eeeeee" width="40%"> 
            <font face="Arial,Helvetica" size="-1"> ${array[3]}</font>
        </td>
        <td bgcolor="#dddddd" width="40%">
            <stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="displayMessage">
                <stripes:param name="number" value="${array[0]}"/>
                <stripes:param name="folder" value="${actionBean.folder}"/>
                <stripes:param name="parent" value="${actionBean.parent}"/>
                <font face="Arial,Helvetica" size="-1">${array[4]}</font>
            </stripes:link>
        <td bgcolor="#eeeeee" nowrap width="16%">
            <font face="Arial,Helvetica" size="-1">${array[5]}</font>
        </td>
        <td bgcolor="#eeeeee" height="20" width="1%">
            ${array[6]}
        </td>
    </tr>
    </core:forEach>
   </core:if>
  
    <tr> 
      <td  colspan="7" width="100%" height="25" align="left" bgcolor="#000000" nowrap>
        <stripes:submit name="deleteMessage">
            <fmt:message key="delete"/>
        </stripes:submit> 
        &nbsp;&nbsp;
        <stripes:button name="moveMessage" onclick="submitMove(this.form);">
             <fmt:message key="moveto"/>
        </stripes:button>   
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
       </td>
    </tr>
</table>           
</div> 
</stripes:form>    
       
<%@include file="./share/trash_footer.jsp" %>    
</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 


