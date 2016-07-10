<br></br>
<table width="90%">
  <tr>
    <td width="100%" align="right">
      <core:if test="${actionBean.trashEmpty}">
        <img border="0" src="images/trash_empty.png"/>
      </core:if>
      <core:if test="${!actionBean.trashEmpty}">
        <stripes:link beanclass="pm.matthews.webmail.action.FolderAction" event="getMessageList">
          <stripes:param name="folder" value="${actionBean.trashName}"/> 
          <img border="0" src="images/trash_full.png" />
        </stripes:link>
      </core:if>
    </td>
  </tr>
</table>
<br></br>
