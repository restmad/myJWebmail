<%@ page session="true" contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="./share/taglib.jsp" %>
<fmt:requestEncoding value="UTF-8" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="refresh" content="<%= session.getMaxInactiveInterval() %>; url=index.jsp" />
    <title>jwma WebMail</title> 
</head>
<body>
    <table>
        <tr>
            <td><b>Date:</b> ${actionBean.msg[1]}</td>
        </tr>   
            <td><b>From:</b> ${actionBean.msg[2]}</td>
        <tr>     
            <td><b>To:</b> ${actionBean.msg[3]}</td>
        <tr>  
            <td><b>Subject:</b> ${actionBean.msg[4]}</td>  
        </tr>    
            <td>${actionBean.msg[5]}</td>
        </tr>
    </table>
</body>
</html>
