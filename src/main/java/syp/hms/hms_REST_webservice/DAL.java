package syp.hms.hms_REST_webservice;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DAL {
    public Auftrag getAuftrag(int id) throws SQLException, ClassNotFoundException {
        //--Aenderungen
        String sql =    "SELECT *" +
                        "FROM aenderung" +
                        "WHERE anfrageId = ?";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        List<Aenderung> aenderungList = new LinkedList<>();
        while (rs.next()){
            aenderungList.add(new Aenderung(rs.getString("bezeichnung"), LocalDateTime.parse(rs.getString("datum"))));
        }

        //--Manager-Status-informationen
        sql =   "SELECT manager, status, informationen" +
                "FROM sofortanfrage" +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        String manager = rs.getString("manager");
        String status = rs.getString("status");
        String informationen = rs.getString("informationen");

        //--Unternehmen
        sql =   "SELECT bezeichnung, vorname, nachnahme, firmenbezeichnung, telefon, email" +
                "FROM unternehmen u INNER JOIN anrede a ON u.anredeId = a.anredeId" +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        Firma firma = new Firma(rs.getString("firmenbezeichnung"), rs.getString("bezeichnung"),
                rs.getString("vorname"), rs.getString("nachnahme"), rs.getString("email"), rs.getString("telefon"));

        //--Leistung
        boolean messelogistik;
        boolean transport;
        String messe = null;
        String land = null;
        String stadt = null;
        LocalDate start = null;
        LocalDate ende = null;
        String transportArt = null;

        sql =   "SELECT messeId" +
                "FROM sofortanfrage" +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();

        try {
            int messeId = rs.getInt("messeId");
            messelogistik = true;
            sql =   "SELECT messe, stadt, land, startdate, enddate" +
                    "FROM messe" +
                    "WHERE messeId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, messeId);
            rs = ps.executeQuery();
            rs.next();
            messe = rs.getString("messe");
            stadt = rs.getString("stadt");
            land = rs.getString("land");
            start = LocalDate.parse(rs.getString("startdate"));
            start = LocalDate.parse(rs.getString("enddate"));
        }catch (SQLException e){
            messelogistik = false;
        }

        sql =   "SELECT transportId" +
                "FROM sofortanfrage" +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();

        try {
            int transportId = rs.getInt("transportId");
            transport = true;
            sql =   "SELECT transportArt" +
                    "FROM transport" +
                    "WHERE transportId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, transportId);
            rs = ps.executeQuery();
            rs.next();
            transportArt = rs.getString("transportArt");
        }catch (SQLException e){
            transport = false;
        }

        Leistung leistung = new Leistung(transport, messelogistik, messe, land, stadt, start, ende, transportArt);

        //--ladestelle
        sql =   "SELECT land, stadt, plz, datum" +
                "FROM ladestelle" +
                "WHERE ladestelleId = (SELECT ladestelleId" +
                                      "FROM sofortanfrage" +
                                      "WHERE anfrageId = ?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        Ladestelle ladestelle = new Ladestelle(rs.getString("land"), rs.getString("stadt"),
                rs.getString("plz"), LocalDateTime.parse(rs.getString("datum")));

        sql =   "SELECT land, stadt, plz, datum" +
                "FROM ladestelle" +
                "WHERE entladestelleId = (SELECT ladestelleId" +
                                         "FROM sofortanfrage" +
                                         "WHERE anfrageId = ?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        Ladestelle entladestelle = new Ladestelle(rs.getString("land"), rs.getString("stadt"),
                rs.getString("plz"), LocalDateTime.parse(rs.getString("datum")));

        sql =   "SELECT ruecktransportId" +
                "FROM sofortanfrage" +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();

        Rueckverladung rueckverladung;
        try {
            int ruecktransportId = rs.getInt("ruecktransportId");
            sql =   "SELECT datumUhrzeit" +
                    "FROM ruecktransport" +
                    "WHERE ruecktransportId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, ruecktransportId);
            rs = ps.executeQuery();
            rs.next();

            rueckverladung = new Rueckverladung(true, LocalDateTime.parse(rs.getString("datumUhrzeit")));
        }catch (SQLException e){
            rueckverladung = new Rueckverladung(false, null);
        }

        //--teilpartien
        boolean asTeilpartien;
        List<Teilpartie> teilpartien = new LinkedList<>();
        try {
            sql =   "SELECT anzahl, inhalt, laenge, breite, hoehe, gewicht" +
                    "FROM anfrageteilpartie at INNER JOIN teilpartie t ON at.teilpartieId = t.teilpartieId" +
                    "WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while(rs.next()){
                teilpartien.add(new Teilpartie(rs.getInt("anzahl"), rs.getString("inhalt"), rs.getDouble("laenge"),
                        rs.getDouble("breite"), rs.getDouble("hoehe"), rs.getDouble("gewicht")));
            }
            asTeilpartien = true;
        }catch (SQLException e){
            teilpartien = null;
            asTeilpartien = false;
        }

        //--komplettpartie
        String ladungsart = null;
        int anzahl = 0;
        boolean versicherung = false;
        String angabe = null;
        if(!asTeilpartien){
            sql =   "SELECT bezeichnung, anzahl, versicherung, angabe" +
                    "FROM komplettladung k INNER JOIN ladungsart l ON k.ladungsartId = l.ladungsartId" +
                    "WHERE komplettladungId = (SELECT komplettladungId" +
                                              "FROM sofortanfrage" +
                                              "WHERE anfrageId = ?)";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();

            ladungsart = rs.getString("bezeichnung");
            anzahl = rs.getInt("anzahl");
            versicherung = rs.getBoolean("versicherung");
            angabe =  rs.getString("angabe");
        }
        Sendung sendung = new Sendung(asTeilpartien, teilpartien, ladungsart, anzahl, versicherung, angabe);

        Anfrage anfrage = new Anfrage(firma, leistung, sendung, ladestelle, entladestelle, rueckverladung, informationen);
        Auftrag auftrag = new Auftrag(id,anfrage,manager,status,aenderungList);

        rs.close();
        ps.close();
        return auftrag;
    }

    public int newAuftrag(Anfrage anfrage) throws SQLException, ClassNotFoundException {
        int id = -1;
        //--ladestelle
        String sql = "INSERT INTO ladestelle (land,stadt,plz,datumUhrzeit)" +
                     "VALUES (?,?,?,?)";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, anfrage.getLadestelle().getLand());
        ps.setString(2, anfrage.getLadestelle().getStadt());
        ps.setString(3, anfrage.getLadestelle().getPlz());
        ps.setTimestamp(4, Timestamp.valueOf(anfrage.getLadestelle().getDatum().toString()));
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int ladestelleId = rs.getInt(1);

        //--entladestelle
        sql = "INSERT INTO ladestelle (land,stadt,plz,datumUhrzeit)" +
                "VALUES (?,?,?,?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, anfrage.getLadestelle().getLand());
        ps.setString(2, anfrage.getLadestelle().getStadt());
        ps.setString(3, anfrage.getLadestelle().getPlz());
        ps.setTimestamp(4, Timestamp.valueOf(anfrage.getLadestelle().getDatum().toString()));
        ps.execute();
        rs = ps.getGeneratedKeys();
        rs.next();
        int entladestelleId = rs.getInt(1);

        //--unternehmen
        int anredeId;
        sql = "SELECT anredeId FROM anrede WHERE bezeichnung = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, anfrage.getFirma().getAnrede());
        rs = ps.executeQuery();
        anredeId = rs.getInt("anredeId");

        sql = "INSERT INTO unternhemen (anredeId,vorname,nachname,firmenbeziehung, telefon, email)" +
                "VALUES (?,?,?,?,?,?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, anredeId);
        ps.setString(2, anfrage.getFirma().getVorname());
        ps.setString(3, anfrage.getFirma().getNachname());
        ps.setString(4, anfrage.getFirma().getFirmenbezeichnung());
        ps.setString(5, anfrage.getFirma().getTelefon());
        ps.setString(6, anfrage.getFirma().getEmail());
        ps.execute();
        rs = ps.getGeneratedKeys();
        rs.next();
        int unternehmenId = rs.getInt(1);

        //--sofortanfrage
        sql = "INSERT INTO sofortanfrage (unternehmenId, ladestelleId, entladestelleId, status, informationen)" +
                "VALUES (?,?,?,?,?,?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, unternehmenId);
        ps.setInt(2, ladestelleId);
        ps.setInt(3, entladestelleId);
        ps.setString(4, "angefragt");
        ps.setString(5, anfrage.getInformation());
        ps.execute();
        rs = ps.getGeneratedKeys();
        rs.next();
        id = rs.getInt(1);

        //--transport
        if(anfrage.getLeistung().isTransport()){
            sql = "SELECT transportId FROM transport WHERE transportArt = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getLeistung().getTransportArt());
            rs = ps.executeQuery();
            int transportId = rs.getInt("transportId");

            sql = "UPDATE sofortanfrage SET transportId = ? WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, transportId);
            ps.setInt(2, id);
            ps.execute();
        }

        //--messe
        if(anfrage.getLeistung().isMesselogistik()){
            sql = "INSERT INTO messe (messe, stadt, land, startdate, enddate) VALUES (?,?,?,?,?)";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getLeistung().getMesse());
            ps.setString(2, anfrage.getLeistung().getStadt());
            ps.setString(3, anfrage.getLeistung().getLand());
            ps.setDate(4, Date.valueOf(anfrage.getLeistung().getStart()));
            ps.setDate(5, Date.valueOf(anfrage.getLeistung().getEnde()));
            ps.execute();
            rs = ps.getGeneratedKeys();
            rs.next();
            int messeId = rs.getInt(1);

            sql = "UPDATE sofortanfrage SET messeId = ? WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, messeId);
            ps.setInt(2, id);
            ps.execute();
        }

        //--komplettladung
        if(!anfrage.getSendung().asTeilpartien()){
            sql = "SELECT ladungsartId FROM ladungsart WHERE bezeichnung = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getSendung().getLadungsArt());
            rs = ps.executeQuery();
            int ladungsartId = rs.getInt("ladungsartId");

            sql = "INSERT INTO komplettladung (ladungsartId, anzahl, versicherung, angabe) VALUES (?,?,?,?)";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getLeistung().getMesse());
            ps.setString(2, anfrage.getLeistung().getStadt());
            ps.setString(3, anfrage.getLeistung().getLand());
            ps.setDate(4, Date.valueOf(anfrage.getLeistung().getStart()));
            ps.setDate(5, Date.valueOf(anfrage.getLeistung().getEnde()));
            ps.execute();
            rs = ps.getGeneratedKeys();
            rs.next();
            int komplettladungId = rs.getInt(1);

            sql = "UPDATE sofortanfrage SET komplettladungId = ? WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, komplettladungId);
            ps.setInt(2, id);
            ps.execute();
        }
        else {
            List<Teilpartie> teilpartien = anfrage.getSendung().getTeilpartien();
            sql = "INSERT INTO teilpartie (anzahl, inhalt, laenge, breite, hoehe, gewicht) VALUES (?,?,?,?,?,?)";
            PreparedStatement insert = Database.getInstance().getPreparedStatement(sql);
            sql = "INSERT INTO anfrageteilpartie (teilpartieId, anfrageId) VALUES (?,?)";
            PreparedStatement update = Database.getInstance().getPreparedStatement(sql);

            for(int i = 0; i < teilpartien.size(); i++){
                insert.setInt(1, teilpartien.get(i).getAnzahl());
                insert.setString(2, teilpartien.get(i).getInhalt());
                insert.setDouble(3, teilpartien.get(i).getLaenge());
                insert.setDouble(4, teilpartien.get(i).getBreite());
                insert.setDouble(5, teilpartien.get(i).getHoehe());
                insert.setDouble(6, teilpartien.get(i).getGewicht());
                insert.execute();
                rs = insert.getGeneratedKeys();
                rs.next();
                int teilpartieId = rs.getInt(1);

                update.setInt(1, teilpartieId);
                update.setInt(2, id);
                update.execute();
            }
        }

        //--Aenderungen
        sql = "INSERT INTO aenderung VALUES (?,?,?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(3, "angefragt");
        ps.execute();

        return id;
    }
}