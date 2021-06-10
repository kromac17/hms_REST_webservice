<%@ page import="syp.hms.hms_REST_webservice.Auftrag" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HMS</title>
    <link rel="stylesheet" href="stylesheet/page.css">
</head>
<body>
<h1>Secret</h1>
<%
    boolean loggedIn = (boolean) request.getAttribute("loggedIn");

    if(loggedIn){
%>
    <div class="container">
        <table>
            <thead>
            <th>Anfragenummer</th>
            <th>Manager</th>
            <th>Information</th>
            <th>Unternehmen</th>
            <th>Transport</th>
            <th>Ladung</th>
            <th></th>
            </thead>
            <%
                List<Auftrag> anfragen = (List<Auftrag>) request.getAttribute("anfragen");
                Collections.sort(anfragen);

                for(int i=0;i<anfragen.size();i++){
                    out.println("<tr>\n");
                    out.println("<td>"+anfragen.get(i).getId()+"</td>\n");
                    try{
                        out.println("<td>"+anfragen.get(i).getManager().getVorname()+" "+anfragen.get(i).getManager().getNachname()+"</td>\n");
                    }catch (NullPointerException ex){
                        out.println("<td>---</td>");
                    }
                    try{
                        out.println("<td>"+anfragen.get(i).getAnfrage().getInformation()+"</td>\n");
                    }catch (NullPointerException ex){
                        out.println("<td>---</td>");
                    }

                    out.println("</tr>");
                }
            %>
        </table>
    </div>
<%}%>

    <div class="footer">
        <form action="ContentServlet" method="get">
            <button type="submit" >Refresh</button>
        </form>
    </div>
</body>
</html>