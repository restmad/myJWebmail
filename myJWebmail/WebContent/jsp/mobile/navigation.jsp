<%--mobile--%>
<table class="nav">
    <tr>
        <td class="nav">
            <stripes:link beanclass="pm.matthews.webmail.action.BaseAction" event="main">
                <img src="images/main_small.png"/>
            </stripes:link>
        </td>
        <td class="nav">
            <stripes:link beanclass="pm.matthews.webmail.action.BaseAction" event="compose">
                <img src="images/compose_small.png"/>
            </stripes:link>
        </td>
        <td class="nav">
            <stripes:link beanclass="pm.matthews.webmail.action.ContactAction" event="displayContacts">
                <img src="images/addresses_small.png"/>
            </stripes:link>
        </td>
        <td class="nav">
            <stripes:link beanclass="pm.matthews.webmail.action.PreferenceAction" event="displayPreference">
                <img src="images/settings_small.png"/>
            </stripes:link>
        </td>
        <td class="nav">
            <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="logout">
                <img src="images/logout_small.png"/>
            </stripes:link>
        </td>
    </tr> 
</table>
<%--mobile--%>