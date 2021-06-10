<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>HMS</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="stylesheet/login.css">
</head>
<body>
<div id="main">
    <div class="header" style="background-color:#f1f1f1">
        <h2 class="heading">Login</h2>
        <div style="width: 40px"></div>
        <img id="logo" src="images/hms_logo.png">
    </div>

    <form action="LoginServlet" method="post">
        <div class="container">
            <label for="mnum"><b>Managernummer</b></label>
            <input type="text" placeholder="Enter Managernummer" name="mnum" id="mnum" required>

            <label for="psw"><b>Passwort</b></label>
            <input type="password" placeholder="Enter Passwort" name="psw" id="psw" required>

            <button type="submit">Login</button>
            <%
                String error = (String) request.getAttribute("error");
                if(error != null){
                    out.println("<div id=\"error\">\n" +
                            "       <p>"+error+"</p>\n" +
                            "    </div>");
                }
            %>
        </div>
    </form>
</div>



</body>
</html>