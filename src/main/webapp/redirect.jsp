<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Redirect page</title>
</head>
<body>
<%

    if (request.getSession().getAttribute("USER") == null) {
        response.sendRedirect("/main");
    } else {
        request.getRequestDispatcher("/main").forward(request, response);
    }

%>
</body>
</html>
