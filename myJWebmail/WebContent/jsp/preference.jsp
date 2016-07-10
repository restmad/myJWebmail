<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<stripes:layout-render name="./share/base.jsp">
<stripes:layout-component name="body">
<%@include file="./share/inbox_header.jsp" %>
<%@include file="./share/navigation.jsp" %>
<%@include file="./js/preference.js" %> 

<stripes:form id="preference" name="preference" beanclass="pm.matthews.webmail.action.PreferenceAction">
    <stripes:hidden id="processor" name="processor" value="${actionBean.getProcess()}"/>
    <stripes:errors/>
    <table border="0" width="90%" cellspacing="1" cellpadding="1">
      <tr>
        <td colspan="4" bgcolor="#000000" align="left">
          <b><font face="Arial, Helvetica, sans-serif" color="#FFFFFF"><fmt:message key="preferences.mailidentities"/></font></b>
        </td>
      </tr>
	  <tr bgcolor="#dadada">
        <td nowrap width="5%">
            <stripes:label for="selected">
              <font face="Arial,Helvetica"><b> #</b></font>
            </stripes:label>
        </td>
        <td nowrap width="45%">
            <stripes:label for="defaultID">
                <font face="Arial,Helvetica"><b><fmt:message key="preferences.mailid.name"/></b></font>
            </stripes:label>
        </td>
        <td nowrap width="45%">
            <stripes:label for="defaultID">
                <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="message.from"/></b></font>
            </stripes:label>
        </td>
        <td nowrap width="5%">
          <font color="#000000" face="Arial,Helvetica"><b><fmt:message key="form.default"/></b></font>
        </td>
      </tr>
      <core:set var="v" value="true"/>
      <core:forEach var="m" items="${actionBean.longMailId}">
        <tr>
          <td width="5%" bgcolor="#eeeeee">
            <stripes:checkbox name="selected" value="${m[1]}"/>
          </td>
          <td width="45%" align="left" valign="top" bgcolor="#eeeeee">
              ${fn:escapeXml(m[0])}
          </td>
          <td width="45%" align="left" valign="top" bgcolor="#eeeeee">
              <em>${m[1]}</em>
          </td>
          <td width="5%" align="left" valign="top" bgcolor="#eeeeee">
              <core:if var="v" test="${v==true}">
                  &nbsp;<stripes:image name="tick" src="images/tick.png"/> 
              </core:if>
              <core:if var="v" test="${v==false}">
                <stripes:checkbox name="defaultID" value="${m[1]}"/> 
              </core:if>  
              <core:set var="v" value="false"/> 
          </td>
        </tr>
      </core:forEach>    
    <tr>
      <td bgcolor="#000000" height="5" width="5%">&nbsp;</td>
      <td bgcolor="#000000" height="5" width="45%">&nbsp;</td>
      <td bgcolor="#000000" height="5" width="45%">&nbsp;</td>
      <td bgcolor="#000000" height="5" width="5%">&nbsp;</td>
    </tr>
  
    <tr>
      <td bgcolor="#eeeeee" width="5%"> <i><fmt:message key="add"/>:</i></td>
      <td bgcolor="#eeeeee" nowrap width="45%">
        <stripes:text name="newName" size="40"/>
      </td>
      <td bgcolor="#eeeeee" nowrap width="45%">
          <stripes:text name="newFrom" size="40"/>
      </td>
      <td nowrap bgcolor="#eeeeee" width="5%">
          <stripes:checkbox name="newDef" value="true"/>
      </td>
    </tr>
    <tr align="right">
      <td colspan="4" bgcolor="#000000">
        <stripes:button name="deleteSort" onclick="deletesort(this.form);">
            <fmt:message key="delete"/>
        </stripes:button>   
        &nbsp;&nbsp;&nbsp;
        <stripes:button name="addSort" onclick="addsort(this.form);">
            <fmt:message key="add"/>
        </stripes:button> 
        
      </td>
    </tr>
  </table>
  <br>
  <stripes:messages/>
  <table border="0" width="90%" cellspacing="1" cellpadding="1">
    <tr>
      <td colspan=2 bgcolor="#000000" align="left">
        <b><font face="Arial, Helvetica, sans-serif" color="#FFFFFF"><fmt:message key="preferences.systemsettings"/></font></b>
      </td>
    </tr>
     <tr>
      <td width="20%" align="left" valign="top" bgcolor="#dddddd">
        <b><fmt:message key="preferences.language"/></b>
      </td>
      <td width="80%" align="left" valign="top" bgcolor="#eeeeee">
          <stripes:select name="language" >  
              <core:forEach var="l" items="${actionBean.lang}">
                  <stripes:option label="${l}"></stripes:option>
              </core:forEach>
          </stripes:select>    
        <font face="Arial, Helvetica, sans-serif"></font><br>*
        <font size="-2" face="Arial, Helvetica, sans-serif">
          <fmt:message key="preferences.language.description"/>
        </font>
      </td>
    </tr>
     <tr>
        <td width="20%" align="left" valign="top" bgcolor="#dddddd">
          <b><fmt:message key="preferences.mailid.autosign"/></b>
        </td>
        <td width="80%" align="left" valign="top" bgcolor="#eeeeee">
          <stripes:checkbox  name="autoSign" checked="${actionBean.pref[5]}"/>
          <font face="Arial, Helvetica, sans-serif"><fmt:message key="form.enable"/></font><br></br>*
          <font size="-2" face="Arial, Helvetica, sans-serif">
            <fmt:message key="preferences.mailid.autosign.description"/>
          </font>
        </td>
      </tr>
     <tr>
      <td width="20%" align="left" valign="top" bgcolor="#dddddd">
        <b><fmt:message key="preferences.autoquote"/></b>
      </td>
      <td width="80%" align="left" valign="top" bgcolor="#eeeeee">
        <stripes:checkbox  name="autoQuote" checked="${actionBean.pref[4]}"/>
        <font face="Arial, Helvetica, sans-serif"><fmt:message key="form.enable"/></font><br></br>*
        <font size="-2" face="Arial, Helvetica, sans-serif">
          <fmt:message key="preferences.autoquote.description"/>
        </font>
      </td>
     </tr>
     <tr>
      <td width="20%" align="left" valign="top" bgcolor="#dddddd">
        <b><fmt:message key="preferences.autoempty"/></b>
      </td>
      <td width="80%" bgcolor="#eeeeee">
          <stripes:checkbox name="autoEmpty" checked="${actionBean.pref[3]}"/>
        <font face="Arial, Helvetica, sans-serif"><fmt:message key="form.enable"/></font><br></br>*
        <font size="-2" face="Arial, Helvetica, sans-serif">
          <fmt:message key="preferences.autoempty.description"/>
        </font>
      </td>
    </tr> 
     <tr>
        <td width="20%" align="left" valign="top" bgcolor="#dddddd">
          <b><fmt:message key="preferences.outbox"/></b>
        </td>
        <td width="80%" bgcolor="#eeeeee">
          <stripes:select name="sentMail" >  
              <core:forEach var="s" items="${actionBean.sent}">
                  <stripes:option label="${s}"></stripes:option>
              </core:forEach>
          </stripes:select> 
          <br></br>
          *<font size="-2" face="Arial, Helvetica, sans-serif">
            <fmt:message key="preferences.outbox.description"/>
          </font>
        </td>
      </tr>
      <tr>
        <td width="20%" align="left" valign="top" bgcolor="#dddddd">
          <b><fmt:message key="preferences.draftfolder"/></b>
        </td>
        <td width="80%" bgcolor="#eeeeee">
          <stripes:select name="draftMail" >  
              <core:forEach var="d" items="${actionBean.draft}">
                  <stripes:option label="${d}"></stripes:option>
              </core:forEach>
          </stripes:select> 
          <br></br>*
          <font size="-2" face="Arial, Helvetica, sans-serif">
            <fmt:message key="preferences.draftfolder.description"/>
          </font>
        </td>
      </tr>
      <tr>
        <td width="20%" align="left" valign="top" bgcolor="#dddddd">
          <b><fmt:message key="preference.processor"/></b>
        </td>
        <td width="80%" bgcolor="#eeeeee">
          <stripes:select name="processorSelect" id="processorSelect">  
              <core:forEach var="p" items="${actionBean.processorOptions}" varStatus="counter">
                  <stripes:option value="${counter.count}"><fmt:message key='${p}'/></stripes:option>
              </core:forEach>
          </stripes:select> 
          <br></br>*
          <font size="-2" face="Arial, Helvetica, sans-serif">
            <fmt:message key="preference.processor.description"/>
          </font>
        </td>
      </tr>
  </table>
  <br>
  <table border="0" width="90%" cellspacing="0" cellpadding="0">
    <tr>
      <td height="15" align="left" valign="top" width="25%">&nbsp; </td>
      <td height="15" align="right" valign="top" width="75%" nowrap>
        <stripes:reset name="undo">
             <fmt:message key="reset"/>
         </stripes:reset> 
        <stripes:submit name="updatePreference"  onclick="processorOrder()">
           <fmt:message key="update"/>
        </stripes:submit>  
      </td>
    </tr>
  </table>
</stripes:form>
 <br>

</stripes:layout-component>
<stripes:layout-component name="footer"></stripes:layout-component>
</stripes:layout-render> 