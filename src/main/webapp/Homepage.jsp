<%@ page import="syp.hms.hms_REST_webservice.Auftrag" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.DateTimeFormatterBuilder" %>
<%@ page import="syp.hms.hms_REST_webservice.Teilpartie" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="javascript/page.js"></script>
<html lang="de">
<title>HMS</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="stylesheet/page.css">
<body>

<!-- Navbar (sit on top) -->
<div class="w3-top">
    <div class="navbar-nav navbar-expand-xl w3-bar w3-white w3-wide w3-padding w3-card" style="align-items: center; width: 100%">
        <a href="#home" class="w3-bar-item w3-button"><img src="images/img_1.png"></a>
        <!-- Float links to the right. Hide them on small screens -->
        <div class="navbar-nav ml-auto w3-right w3-hide-small">
            <%
                try{
                    boolean loggedIn = (boolean) request.getAttribute("loggedIn");
                    if(loggedIn){
            %>
            <div class="nav-item">
                <a href="#table" class="w3-bar-item w3-button" style="color: #8c3a4a; font-weight: bold" >Sofortanfragen</a>
            </div>
           <%       }
                }catch(NullPointerException ex){
                    ex.printStackTrace();
                }
           %>
            <div class="nav-item">
                <a href="#projects" class="w3-bar-item w3-button">Unternehmen</a>
            </div>
            <div class="nav-item">
                <a href="#about" class="w3-bar-item w3-button">Logistik</a>
            </div>
            <div class="nav-item">
                <a href="Contact.html" class="w3-bar-item w3-button">Kontakt</a>
            </div>
            <div class="nav-item">
                <a href="#contact" class="w3-bar-item w3-button">Downloads</a>
            </div>
            <div class="nav-item">
                <a href="#contact" class="w3-bar-item w3-button">Sprache</a>
            </div>
        </div>
    </div>
</div>

<!-- Header -->
<header class="w3-display-container w3-content w3-wide" style="max-width:1800px;" id="home">
    <img class="w3-image" src="images/team_graz_banner.jpg" alt="Architecture" width="450" height="125">
    <div class="w3-display-middle w3-margin-top w3-center">
        <h1 class="w3-xxlarge w3-text-white"><span class="w3-padding w3-black w3-opacity-min"><b>HMS</b></span> <span
                class="w3-hide-small w3-text-white">Messelogistik</span></h1>
    </div>
</header>

<!-- Page content -->
<div class="w3-content w3-padding" style="max-width:1564px">
    <%
        try{
            boolean loggedIn = (boolean) request.getAttribute("loggedIn");
            if(loggedIn){
                int manager_id = (int) request.getAttribute("manager");
    %>
    <!-- Anfrage Section -->
    <div class="w3-container w3-padding-32" id="table">
        <h3 class="w3-border-bottom w3-border-light-grey w3-padding-16">Sofortanfrage</h3>
    </div>

    <div>
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
                DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                List<Auftrag> anfragen = (List<Auftrag>) request.getAttribute("anfragen");
                Collections.sort(anfragen);

                for(int i=0;i<anfragen.size();i++){
                    out.println("<tr>\n");
                    out.println("<td class=\"num\">"+anfragen.get(i).getId()+"</td>\n");
                    try{
                        out.println("<td class=\"manager\">"+anfragen.get(i).getManager().getVorname()+" "+anfragen.get(i).getManager().getNachname()+"</td>\n");
                    }catch (NullPointerException ex){
                        out.println("<td class=\"manager\">---</td>");
                    }
                    try{
                        out.println("<td class=\"info\">"+anfragen.get(i).getAnfrage().getInformation()+"</td>\n");
                    }catch (NullPointerException ex){
                        out.println("<td class=\"info\">---</td>");
                    }

                    out.println(
                            "<td class=\"dropdown\">\n" +
                            "<p class=\"dropbtn\">"+anfragen.get(i).getAnfrage().getFirma().getFirmenbezeichnung()+"</p> \n" +
                            "   <div class=\"dropdown-content\">\n" +
                            "       <p>Name: "+anfragen.get(i).getAnfrage().getFirma().getAnrede()+" "+anfragen.get(i).getAnfrage().getFirma().getVorname()+" "+anfragen.get(i).getAnfrage().getFirma().getNachname()+"</p>\n" +
                            "       <p>Tel: <a href=\"tel:"+anfragen.get(i).getAnfrage().getFirma().getTelefon()+"\">"+anfragen.get(i).getAnfrage().getFirma().getTelefon()+"</a></p>\n" +
                            "       <p>Email: <a href=\"mailto:"+anfragen.get(i).getAnfrage().getFirma().getEmail()+"\">"+anfragen.get(i).getAnfrage().getFirma().getEmail()+"</a></p>\n" +
                            "   </div>\n" +
                            "</td>"
                    );
                    String transport = "<td class=\"dropdown\">\n";
                    if (anfragen.get(i).getAnfrage().getLeistung().isTransport() && anfragen.get(i).getAnfrage().getLeistung().isMesselogistik()) {
                        transport += "<p class=\"dropbtn\">" + anfragen.get(i).getAnfrage().getLeistung().getTransportArt() + "</p> \n" +
                                "   <div class=\"dropdown-content\">\n" +
                                "       <p>Messe: " + anfragen.get(i).getAnfrage().getLeistung().getMesse() + ", " + anfragen.get(i).getAnfrage().getLeistung().getLand() +" "+ anfragen.get(i).getAnfrage().getLeistung().getStadt() + "\n" + anfragen.get(i).getAnfrage().getLeistung().getStart().format(dt) + " - " + anfragen.get(i).getAnfrage().getLeistung().getEnde().format(dt) + "</p>\n" +
                                "       <p>Ladestelle: " + anfragen.get(i).getAnfrage().getLadestelle().toString() + "</p>\n" +
                                "       <p>Entladestelle: " + anfragen.get(i).getAnfrage().getEntladestelle().toString() + "</p>\n";
                    } else if(anfragen.get(i).getAnfrage().getLeistung().isTransport()){
                        transport += "<p class=\"dropbtn\">" + anfragen.get(i).getAnfrage().getLeistung().getTransportArt() + "</p> \n" +
                                "   <div class=\"dropdown-content\">\n" +
                                "       <p>Ladestelle: " + anfragen.get(i).getAnfrage().getLadestelle().toString() + "</p>\n" +
                                "       <p>Entladestelle: " + anfragen.get(i).getAnfrage().getEntladestelle().toString() + "</p>\n";
                    } else {
                        transport += "<p class=\"dropbtn\">"+anfragen.get(i).getAnfrage().getLeistung().getMesse()+"</p> \n" +
                                "   <div class=\"dropdown-content\">\n" +
                                "       <p>Start: "+ anfragen.get(i).getAnfrage().getLeistung().getStart().format(dt) +"</p>\n" +
                                "       <p>Ende: "+ anfragen.get(i).getAnfrage().getLeistung().getEnde().format(dt) + "</p>\n" +
                                "       <p>Ort: "+ anfragen.get(i).getAnfrage().getLeistung().getLand() +", "+ anfragen.get(i).getAnfrage().getLeistung().getStadt() + "</p>\n";
                    }

                    if(anfragen.get(i).getAnfrage().getRueckverladung().isInkludiert()){
                        transport += "       <p>Rücktransport: Inkludiert</p>\n";
                    } else{
                        transport += "       <p>Rücktransport: Nicht Inkludiert</p>\n";
                    }

                    transport += "   </div>\n"+
                                 "</td>";

                    out.println(transport);

                    if(anfragen.get(i).getAnfrage().getSendung().isAsTeilpartien()) {
                        String teilartien =  "<td class=\"dropdown\">\n" +
                                                "<p class=\"dropbtn\">Teilpartien</p> \n" +
                                                "   <div class=\"dropdown-content\">\n" +
                                                "   <table>";

                        List<Teilpartie> teilList = anfragen.get(i).getAnfrage().getSendung().getTeilpartien();
                        for(int y=0; y < teilList.size(); y++){
                            teilartien += teilList.get(y).toString();
                        }

                        teilartien += "   </table>" +
                                      "   </div>\n" +
                                      "</td>";
                        out.println(teilartien);
                    } else {
                        out.println(
                                "<td class=\"dropdown\">\n" +
                                        "<p class=\"dropbtn\">" + anfragen.get(i).getAnfrage().getSendung().getLadungsArt() + "</p> \n" +
                                        "   <div class=\"dropdown-content\">\n" +
                                        "       <p>Anzahl: " + anfragen.get(i).getAnfrage().getSendung().getAnzahl() + "</p>\n"
                        );
                        if(anfragen.get(i).getAnfrage().getSendung().getEigeneAngabe().equals("") || anfragen.get(i).getAnfrage().getSendung().getEigeneAngabe().equals(" ") || anfragen.get(i).getAnfrage().getSendung().getEigeneAngabe() == null){
                            out.println("       <p>Angabe: " + anfragen.get(i).getAnfrage().getSendung().getEigeneAngabe() + "</p>\n");
                        }
                        if(anfragen.get(i).getAnfrage().getSendung().isVersicherung()){
                            out.println("       <p>Versicherung: Inkludiert</p>\n");
                        }else{
                            out.println("       <p>Versicherung: Nicht Inkludiert</p>\n");
                        }

                        out.println(
                                "   </div>\n" +
                                        "</td>"
                        );
                    }
                    if(anfragen.get(i).getManager() == null)
                        out.println("<td class=\"check\" id=\"ck_"+anfragen.get(i).getId()+"\" onclick=\"checkbox("+anfragen.get(i).getId()+", "+manager_id+")\"><p class=\"check-text\">Angenommen</p></td>");
                    else
                        out.println("<td class=\"check\" id=\"ck_"+anfragen.get(i).getId()+"\" onclick=\"checkbox("+anfragen.get(i).getId()+", "+manager_id+")\"><p class=\"check-text\" style=\"background-color: green\">Angenommen</p></td>");

                    out.println("</tr>");
                }
            %>

        </table>
    </div>
    <%       }
            else {
    %>
            <!-- About Section -->
            <div class="w3-container w3-padding-32" id="about">
                <h3 class="w3-border-bottom w3-border-light-grey w3-padding-16">Kontakte in Graz</h3>
            </div>

            <div class="w3-row-padding">
                <div class="w3-col l3 m6 w3-margin-bottom">
                    <img src="images/johann_ambros.jpg" alt="johann_ambros" style="width:100%">
                    <h3>Johann Ambros</h3>
                    <p class="w3-opacity">Branch Manager</p>
                    <p>Sprachen: Deutsch, Englisch</p>
                    <p><a>Telefon: </a><a href="tel:+43 316 231211 10">+43 316 231211 10</a></p>
                    <a href="mailto:johann.ambros@hansa-messe-speed.com"><i style="font-size:24px" class="fa">&#xf003;</i></a>
                    </button></p>
                </div>
                <div class="w3-col l3 m6 w3-margin-bottom">
                    <img src="images/angelika_meister-1-600x909.jpg" alt="Jane" style="width:89%">
                    <h3>Angelika Meister</h3>
                    <p class="w3-opacity">Project Assistant</p>
                    <p>Sprachen: Deutsch, Englisch, Französisch</p>
                    <p><a>Telefon: </a><a href="tel:+43 316 231211 11">+43 316 231211 11</a></p>
                    <a href="mailto:angelika.meister@hansa-messe-speed.com"><i style="font-size:24px" class="fa">&#xf003;</i></a>
                </div>
            </div>
            <div style="height: 60px">

            </div>
    <%
            }
    }catch(NullPointerException ex){
    %>
        <!-- About Section -->
        <div class="w3-container w3-padding-32" id="about">
            <h3 class="w3-border-bottom w3-border-light-grey w3-padding-16">Kontakte in Graz</h3>
        </div>

        <div class="w3-row-padding">
            <div class="w3-col l3 m6 w3-margin-bottom">
                <img src="images/johann_ambros.jpg" alt="johann_ambros" style="width:100%">
                <h3>Johann Ambros</h3>
                <p class="w3-opacity">Branch Manager</p>
                <p>Sprachen: Deutsch, Englisch</p>
                <p><a>Telefon: </a><a href="tel:+43 316 231211 10">+43 316 231211 10</a></p>
                <a href="mailto:johann.ambros@hansa-messe-speed.com"><i style="font-size:24px" class="fa">&#xf003;</i></a>
                </button></p>
            </div>
            <div class="w3-col l3 m6 w3-margin-bottom">
                <img src="images/angelika_meister-1-600x909.jpg" alt="Jane" style="width:89%">
                <h3>Angelika Meister</h3>
                <p class="w3-opacity">Project Assistant</p>
                <p>Sprachen: Deutsch, Englisch, Französisch</p>
                <p><a>Telefon: </a><a href="tel:+43 316 231211 11">+43 316 231211 11</a></p>
                <a href="mailto:angelika.meister@hansa-messe-speed.com"><i style="font-size:24px" class="fa">&#xf003;</i></a>
            </div>
        </div>
        <div style="height: 60px">

        </div>
    <%
    }
    %>
</div>

<!--Footer-->
<footer class="w3-center w3-black" style="background-color: black; color: white">
    <div class="footer">
        <p>HMS - Hansa Messe Speed </p>
        <%
            try{
                boolean loggedIn = (boolean) request.getAttribute("loggedIn");
                if(loggedIn){
        %>
                <a>
                    <form action="LoginServlet" method="get">
                        <button class="button">Logout</button>
                    </form>
                </a>
        <%
                }
                else {
        %>
                    <a href="login.jsp"><button class="button">Intranet Login</button></a>
        <%
                }
            }catch(NullPointerException ex){
        %>
                 <a href="login.jsp"><button class="button">Intranet Login</button></a>
        <%
            }
        %>
    </div>
</footer>

</body>
</html>
