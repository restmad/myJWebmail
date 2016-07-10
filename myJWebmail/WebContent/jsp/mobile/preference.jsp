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

<stripes:form name="preference" beanclass="pm.matthews.webmail.action.PreferenceAction">
    <stripes:hidden name="draftMail" value="${actionBean.draft}"/>
    <div class="wide dark">
      <font><b><fmt:message key="preferences.mailidentities"/></b></font>
    </div>
    <div class="wide dark">&nbsp;</div> 
    <div class="wide light">
      <font>${fn:escapeXml(actionBean.longMailId[0][0])}</font>
    </div>
    <div class="wide light">
          <font><em>${actionBean.longMailId[0][1]}</em></font>
    </div>
    <div class="wide">
        <stripes:text name="newName" size="15"/>&nbsp;<font><fmt:message key="preferences.mailid.name"/></font>
    </div>
    <div class="wide">
          <stripes:text name="newFrom" size="15"/>&nbsp;<font><fmt:message key="contacts.email"/></font>
    </div>
    <div class="wide black">
        &nbsp;
        <stripes:button name="addSort" onclick="preference.action='Preference.jwma?addMailId';preference.submit();">
            <fmt:message key="add"/>
        </stripes:button> 
        <a class="green" href="#" onclick="preference.action='Preference.jwma?addMailId';preference.submit();"><fmt:message key="add"/></a>
    </div>
        <stripes:messages/> 
    <div colspan="2" class="wide dark">
        <font><b><fmt:message key="preferences.systemsettings"/></b></font>
    </div>
    
    <table>
     <tr>
      <td class="light">
        <font><fmt:message key="preferences.language"/></font>
      </td>
      <td>
       <div class="tag button-holder">
          <core:forEach var="l" items="${actionBean.lang}"  varStatus="counter">
            <div>
               <core:if test="${counter.count eq 1}">
                <input type="radio" name="language" value="${l}" checked="checked" id="radio-2-1"  class="regular-radio big-radio"/>
               </core:if>
                <core:if test="${counter.count ne 1}">
                    <input type="radio" name="language" value="${l}" id="radio-2-1"  class="regular-radio big-radio"/>
               </core:if>
               <label class="radio-1"></label>
               <font>${l}</font>
            </div>   
          </core:forEach> 
       </div>
      </td>
    </tr>
     <tr>
        <td class="light">
          <font><fmt:message key="preferences.mailid.autosign"/></font>
        </td>
        <td>
         <div class="tag">
            <stripes:checkbox name="autoSign" checked="${actionBean.pref[5]}" id="checkbox-2-1" class="regular-checkbox big-checkbox" />
            <label for="checkbox-2-1"></label>
            &nbsp;
            <font>
              <fmt:message key="form.enable"/>
            </font>
         </div>
        </td>
      </tr>
     <tr>
      <td class="light">
        <font><fmt:message key="preferences.autoquote"/></font>
      </td>
      <td>
       <div class="tag">
            <stripes:checkbox name="autoQuote" checked="${actionBean.pref[4]}" id="checkbox-2-2" class="regular-checkbox big-checkbox" />
            <label for="checkbox-2-2"></label>
            &nbsp;
            <font>
              <fmt:message key="form.enable"/>
            </font>
       </div>        
      </td>
     </tr>
     <tr>
      <td class="light">
        <font><fmt:message key="preferences.autoempty"/></font>
      </td>
      <td>
       <div class="tag">
            <stripes:checkbox name="autoEmpty" checked="${actionBean.pref[3]}" id="checkbox-2-3" class="regular-checkbox big-checkbox" />
            <label for="checkbox-2-3"></label>
            &nbsp;
            <font>
              <fmt:message key="form.enable"/>
            </font>
       </div>
      </td>
    </tr> 
     <tr>
        <td class="light">
          <font><fmt:message key="preferences.outbox"/></font>
        </td>
        <td>
          <div class="tag button-holder">
              <core:forEach var="s" items="${actionBean.sent}" varStatus="counter">
                <div>
                    <core:if test="${counter.count eq 1}">
                        <input type="radio" name="sentMail" checked="checked" value="${s}" id="radio-2-2"  class="regular-radio big-radio"/>
                    </core:if>
                    <core:if test="${counter.count ne 1}">
                        <input type="radio" name="sentMail" value="${s}" id="radio-2-2"  class="regular-radio big-radio"/>
                    </core:if>  
                    <label class="radio-1"></label>
                   <font>${s}</font>
                </div>   
              </core:forEach>
           </div>
        </td>
      </tr>
   </table>
        <div class="wide black">
            &nbsp;
            <a class="green" href="#" onclick="preference.action='Preference.jwma?updatePreference';preference.submit();"><fmt:message key="update"/></a>
        </div>
</stripes:form>
</body>
</html>
<%--mobile--%>