
<stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="cycleMessages">
  <img src="images/next_small.png" width="20" height="15" align="right" border="0" vspace="3">
  <stripes:param name="folder" value="${actionBean.folder}"/>  
  <stripes:param name="parent" value="${actionBean.parent}"/> 
  <stripes:param name="messageNumber" value="${actionBean.msg[0]}"/>
  <stripes:param name="next" value="next"/>           
</stripes:link>
<stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="printMessage">
   <stripes:param name="folder" value="${actionBean.folder}"/>         
   <stripes:param name="number" value="${actionBean.msg[0]}"/>
   <stripes:param name="parent" value="${actionBean.parent}"/>
   <img src="images/printer_small.png" width="20" height="20" align="right" hspace="5"  border="0">
</stripes:link>
<stripes:link beanclass="pm.matthews.webmail.action.MessageAction" event="cycleMessages">
  <img src="images/previous_small.png" width="20" height="15" align="right" border="0" vspace="3">
  <stripes:param name="folder" value="${actionBean.folder}"/> 
  <stripes:param name="parent" value="${actionBean.parent}"/>
  <stripes:param name="messageNumber" value="${actionBean.msg[0]}"/>
  <stripes:param name="next" value="prev"/>           
</stripes:link>
