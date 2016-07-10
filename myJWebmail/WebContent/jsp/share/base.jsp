<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="taglib.jsp" %>

<stripes:layout-definition>
<fmt:requestEncoding value="UTF-8" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>    
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/> 
    <meta http-equiv="Pragma" content="no-cache"/>
    <script type='text/javascript' src='jsp/js/jquery.js'></script>
    <link rel="stylesheet" type="text/css" href="jsp/share/jwma.css"/>
    <title>jwma WebMail</title>
</head>
<body bgcolor="#FFFFFF" link="#666666" vlink="#666666" alink="#FFFFFF">
    
    <div id="body">
        <stripes:layout-component name="body"/>
    </div>

    <div id="footer">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr bgcolor="#000000" valign="bottom"> 
                <td height="20"> <a href="#top"><img src="images/up_small.png" width="15" height="15" align="right" border="0" ></img></a>
                    <font size="-2" face="Arial, Helvetica, sans-serif" color="#FFFFFF">&copy; 
                    2000-2015 jwma team</font> 
                </td>
            </tr>
        </table>
    </div>
</body>
</html>
</stripes:layout-definition>