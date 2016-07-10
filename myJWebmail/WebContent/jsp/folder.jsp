<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
<%@include file="./share/inbox_header.jsp" %>
<%@include file="./share/navigation.jsp" %>
<%@include file="./js/folder.js" %> 

<core:if test='${actionBean.type2()}'>
<table width="100%">
    <tr bgcolor="#FFFFFF"> 
        <td> 
            <stripes:link beanclass="pm.matthews.webmail.action.SubFolderAction" event="list">
                <font color="#000000" face="Arial,Helvetica"><img src="images/folder.png" width="36" height="36"  align="middle"/>
                    <b>${actionBean.folder}</b>
                </font>
                <stripes:param name="folder" value="${actionBean.folder}"/>
            </stripes:link>  
        </td>
    </tr>
</table>
</core:if>
    <table width="90%" cellpadding="1" cellspacing="1" border="0">
        <tr>
            <td bgcolor="#000000" colspan="2">
              <img align="left" src="images/folder_small.png"  width="20" height="20">
              &nbsp;&nbsp;
            </td>
	</tr>
               
	<tr>
            <stripes:form name="folderform" id="folderform" beanclass="pm.matthews.webmail.action.FolderAction">
                <stripes:hidden name="targetFolder" id="targetFolder" value=""/>
                <stripes:hidden name="newStore" id="newStore" value=""/>
                <stripes:hidden name="folder" value="${fn:escapeXml(folder.getFullName())}"/>
                <stripes:errors/>
                <td width="50%" height="100%" bgcolor="#eeeeee" align="left" valign="top">
		<table cellpadding="0" cellspacing="0" border="0" width="100%" align="left">
                    <tr>
                        <td height="25" width="100%" bgcolor="#dddddd">
                            <stripes:label for="targetFolder">     
                                <font face="Arial,Helvetica"><b><fmt:message key="folder.folders"/></b></font>
                            </stripes:label>
                        </td>
                    </tr>
                                        
                    <core:forEach var="folder" items="${actionBean.mixedStoreList}">
                        <core:if test="${folder.getType() ne 1}">
                            <tr>
                                <td width="100%" height="25" align="left" valign="top" bgcolor="#eeeeee">
                                    <stripes:checkbox name="selection" value="${fn:escapeXml(folder.getFullName())}"/>
                                    <stripes:link beanclass="pm.matthews.webmail.action.SubFolderAction" event="list">
                                        <stripes:param name="folder" value="${fn:escapeXml(folder.getFullName())}"/>
                                        <font face="Arial,Helvetica" size=-1><b>${fn:escapeXml(folder.getName())}</b></font>
                                    </stripes:link> 
                                </td>     
                            </tr>
                        </core:if>
                    </core:forEach>
                    
                </table>
            </td>            
            <td width="50%" height="100%" bgcolor="#eeeeee" align="left" valign="top">
                <table cellpadding="0" cellspacing="0" border="0" width="100%" align="left">
                    <tr>
                        <td width="100%" height="25" bgcolor="#dddddd">
                           <stripes:label for="selection">     
                             <font face="Arial,Helvetica"><b><fmt:message key="folder.mailboxes"/></b></font>
                           </stripes:label>
                        </td>
                    </tr>
                 
                       <core:forEach var="folder" items="${actionBean.mixedStoreList}">
                        <core:if test="${folder.getType() eq 1}">
                            <tr>
                                <td width="100%" height="25" align="left" valign="top" bgcolor="#eeeeee">
                                    <stripes:checkbox name="selection" value="${fn:escapeXml(folder.getFullName())}"/>
                                    <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="listMessages">
                                        <stripes:param name="folder" value="${fn:escapeXml(folder.getFullName())}"/>
                                        <stripes:param name="parent" value="${actionBean.parent}"/>
                                        <font face="Arial,Helvetica" size=-1><b>${fn:escapeXml(folder.getName())}</b></font>
                                    </stripes:link>
                                </td>     
                            </tr>
                        </core:if>
                        </core:forEach>
                        
                 </table>
            </td>          
         </tr>
    	 <tr>
            <td colspan="2" width="100%" height="25" align="left" bgcolor="#000000" nowrap>
                <stripes:submit name="deleteFolder">
                    <fmt:message key="delete"/>
                </stripes:submit> 
                &nbsp;&nbsp;
                
                <stripes:button name="movefld" onclick="moveFolder()">
                    <fmt:message key="moveto"/>
                </stripes:button>
                <stripes:select id="targetselect" name="targetselect">
                        <core:forEach var="folder" items="${actionBean.mixedStoreList}">
                            <core:if test="${folder.getType() ne 1}" >
                             <stripes:option value="${fn:escapeXml(folder.getFullName())}" label="${fn:escapeXml(folder.getName())}"/>
                            </core:if>
                        </core:forEach>
                </stripes:select>
                &nbsp;&nbsp;             
                <stripes:submit name="createFolder" onclick="newFolder()">
                    <fmt:message key="createnew"/>
                </stripes:submit>
                <stripes:select name="storetype" id="storetype">
                    <stripes:option><fmt:message key="folder.mailbox"/></stripes:option>
                    <stripes:option><fmt:message key="folder.folder"/></stripes:option>                  
                </stripes:select>
                <stripes:text name="storename" size="20"/>
            </td>
        </tr>
        </stripes:form>       
    </table>                        
 <stripes:messages/>   
<%@include file="./share/trash_footer.jsp" %>    
</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 


