package syp.hms.hms_REST_webservice;

import org.postgresql.util.PSQLException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DAL {
    public boolean login(int username, String password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT password FROM manager WHERE managerid=?");
        ps.setInt(1,username);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String hashFromDB = rs.getString("password");

        MessageDigest alg = MessageDigest.getInstance("SHA-256");
        alg.update(password.getBytes());

        byte[] hash = alg.digest();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<hash.length; i++)
        {
            sb.append(String.format("%02X", hash[i]&0xFF));
        }
        String hashFromPW = sb.toString();

        if(hashFromDB.toUpperCase().equals(hashFromPW))
            return true;

        return false;
    }

    public void insertLands(List<Land> landList) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM land";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.execute();

        sql = "INSERT INTO land (kuerzel, bezeichnung) " +
                "VALUES (?,?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        for (int i=0; i<landList.size(); i++){
            ps.setString(1, landList.get(i).getCode());
            ps.setString(2, landList.get(i).getName());
            System.out.println(landList.get(i).getCode()+", "+landList.get(i).getName());
            ps.execute();
        }
    }

    public List<Land> getAllLands() throws SQLException, ClassNotFoundException {
        LinkedList<Land> list = new LinkedList<>();

        String sql = "SELECT * " +
                "FROM land";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            list.add(new Land(rs.getString("kuerzel"), rs.getString("bezeichnung")));
        }

        return list;
    }

    public List<Auftrag> getAll() throws SQLException, ClassNotFoundException {
        LinkedList<Auftrag> list = new LinkedList<>();

        String sql = "SELECT anfrageId " +
                     "FROM sofortanfrage";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            list.add(getAuftrag(rs.getInt("anfrageId")));
        }

        return list;
    }

    public Manager getManager(int id) throws SQLException, ClassNotFoundException {
        String sql =   "SELECT managerId " +
                "FROM sofortanfrage " +
                "WHERE anfrageId = ?";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int managerId = rs.getInt("managerId");

        sql =   "SELECT vorname, nachname, position, telefon, email " +
                "FROM manager " +
                "WHERE managerId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, managerId);
        rs = ps.executeQuery();
        rs.next();
        Manager manager = new Manager(managerId, rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"), rs.getString("telefon"), rs.getString("email"));

        return manager;
    }

    public Aenderung getStatus(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * " +
                "FROM aenderung " +
                "WHERE anfrageId = ? " +
                "AND datumUhrzeit = (SELECT MAX(datumUhrzeit) " +
                                    "FROM aenderung " +
                                    "WHERE anfrageId = ?)";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ps.setInt(2, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        String datum = rs.getString("datumuhrzeit");
        String[] tokens = datum.split(" ");
        datum = tokens[0] + "T"+ tokens[1];
        Aenderung status = new Aenderung(new Status(rs.getString("titel"), rs.getString("bezeichnung")), LocalDateTime.parse(datum));

        return status;
    }

    public List<Aenderung> getAenderungen(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * " +
                "FROM aenderung " +
                "WHERE anfrageid = ?";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        List<Aenderung> aenderungList = new LinkedList<>();
        while (rs.next()){
            String datum = rs.getString("datumuhrzeit");
            String[] tokens = datum.split(" ");
            datum = tokens[0] + "T"+ tokens[1];
            aenderungList.add(new Aenderung(new Status(rs.getString("titel"), rs.getString("bezeichnung")), LocalDateTime.parse(datum)));
        }

        return aenderungList;
    }

    public List<Manager> getAllManager() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * " +
                     "FROM manager";

        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();

        LinkedList<Manager> managerList = new LinkedList<>();
        while (rs.next()){
            managerList.add(new Manager(rs.getInt("managerId"),rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"), rs.getString("telefon"), rs.getString("email")));
        }

        return managerList;
    }

    public Auftrag getAuftrag(int id) throws SQLException, ClassNotFoundException {
        //--Aenderungen
        List<Aenderung> aenderungList = getAenderungen(id);

        //--Informationen
        String sql =   "SELECT informationen " +
                "FROM sofortanfrage " +
                "WHERE anfrageId = ?";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String informationen = rs.getString("informationen");

        Manager manager;
        //--Manager
        try {
            manager = getManager(id);
        }catch (PSQLException e){
            manager = null;
        }

        //--Unternehmen
        sql =   "SELECT unternehmenId " +
                "FROM sofortanfrage " +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        int unternehmenId = rs.getInt("unternehmenId");

        sql =   "SELECT bezeichnung, vorname, nachname, firmenbezeichnung, telefon, email " +
                "FROM unternehmen u INNER JOIN anrede a ON u.anredeId = a.anredeId " +
                "WHERE unternehmenId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, unternehmenId);
        rs = ps.executeQuery();
        rs.next();
        Firma firma = new Firma(rs.getString("firmenbezeichnung"), rs.getString("bezeichnung"),
                rs.getString("vorname"), rs.getString("nachname"), rs.getString("email"), rs.getString("telefon"));

        //--Leistung
        boolean messelogistik;
        boolean transport;
        String messe = null;
        String land = null;
        String stadt = null;
        LocalDate start = null;
        LocalDate ende = null;
        String transportArt = null;

        sql =   "SELECT messeId " +
                "FROM sofortanfrage " +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();

        try {
            int messeId = rs.getInt("messeId");
            messelogistik = true;
            sql =   "SELECT messe, stadt, land, startdate, enddate " +
                    "FROM messe " +
                    "WHERE messeId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, messeId);
            rs = ps.executeQuery();
            rs.next();
            messe = rs.getString("messe");
            stadt = rs.getString("stadt");
            land = rs.getString("land");
            start = LocalDate.parse(rs.getString("startdate"));
            ende = LocalDate.parse(rs.getString("enddate"));
        }catch (SQLException e){
            messelogistik = false;
        }

        sql =   "SELECT transportId " +
                "FROM sofortanfrage " +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();

        try {
            int transportId = rs.getInt("transportId");
            transport = true;
            sql =   "SELECT transportArt " +
                    "FROM transport " +
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
        sql =   "SELECT land, stadt, plz, datumuhrzeit " +
                "FROM ladestelle " +
                "WHERE ladestelleId = (SELECT ladestelleId " +
                                      "FROM sofortanfrage " +
                                      "WHERE anfrageId = ?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        String datum = rs.getString("datumuhrzeit");
        String[] tokens = datum.split(" ");
        datum = tokens[0] + "T"+ tokens[1];
        Ladestelle ladestelle = new Ladestelle(rs.getString("land"), rs.getString("stadt"),
                rs.getString("plz"), LocalDateTime.parse(datum));

        sql =   "SELECT land, stadt, plz, datumuhrzeit " +
                "FROM ladestelle " +
                "WHERE ladestelleId = (SELECT entladestelleId " +
                                      "FROM sofortanfrage " +
                                      "WHERE anfrageId = ?)";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();
        datum = rs.getString("datumuhrzeit");
        tokens = datum.split(" ");
        datum = tokens[0] + "T"+ tokens[1];
        Ladestelle entladestelle = new Ladestelle(rs.getString("land"), rs.getString("stadt"),
                rs.getString("plz"), LocalDateTime.parse(datum));

        sql =   "SELECT ruecktransportId " +
                "FROM sofortanfrage " +
                "WHERE anfrageId = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        rs.next();

        Rueckverladung rueckverladung;
        try {
            int ruecktransportId = rs.getInt("ruecktransportId");
            sql =   "SELECT datumUhrzeit " +
                    "FROM ruecktransport " +
                    "WHERE ruecktransportId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, ruecktransportId);
            rs = ps.executeQuery();
            rs.next();

            rueckverladung = new Rueckverladung(true, LocalDateTime.parse(rs.getString("datumUhrzeit")));
        }catch (SQLException e){
            rueckverladung = new Rueckverladung(false, null);
        }

        //--sendung
        boolean asTeilpartien;
        int komplettladungid = 0;
        try {
            sql =   "SELECT komplettladungId " +
                    "FROM sofortanfrage " +
                    "WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            komplettladungid = rs.getInt("komplettladungId");
            asTeilpartien = false;
        } catch (SQLException e){
            asTeilpartien = true;
        }

        String ladungsart = null;
        int anzahl = 0;
        boolean versicherung = false;
        String angabe = null;
        List<Teilpartie> teilpartien = null;

        if (!asTeilpartien){
            sql =   "SELECT bezeichnung, anzahl, versicherung, angabe " +
                    "FROM komplettladung k INNER JOIN ladungsart l ON k.ladungsartId = l.ladungsartId " +
                    "WHERE komplettladungId = (SELECT komplettladungId " +
                    "FROM sofortanfrage " +
                    "WHERE anfrageId = ?)";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();

            ladungsart = rs.getString("bezeichnung");
            anzahl = rs.getInt("anzahl");
            versicherung = rs.getBoolean("versicherung");
            angabe =  rs.getString("angabe");
        }else {
            teilpartien = new LinkedList<>();
            try {
                sql =   "SELECT anzahl, inhalt, laenge, breite, hoehe, gewicht " +
                        "FROM anfrageteilpartie at INNER JOIN teilpartie t ON at.teilpartieId = t.teilpartieId " +
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
        }

        Sendung sendung = new Sendung(asTeilpartien, teilpartien, ladungsart, anzahl, versicherung, angabe);

        Anfrage anfrage = new Anfrage(firma, leistung, sendung, ladestelle, entladestelle, rueckverladung, informationen);
        Auftrag auftrag = new Auftrag(id,anfrage,manager,aenderungList);

        rs.close();
        ps.close();
        return auftrag;
    }

    public void newStatus(int id, Status status) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO aenderung (anfrageid, datumuhrzeit, titel, bezeichnung) " +
                "VALUES (?,?,?,?)";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(3, status.titel);
        ps.setString(4, status.status);
        ps.execute();
    }

    public void changeManagerData(Manager manager) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE manager SET vorname = ?, nachname = ?, position = ?, telefon = ?, email = ? WHERE managerId = ?";
        System.out.println(sql);

        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, manager.getVorname());
        ps.setString(2, manager.getNachname());
        ps.setString(3, manager.getPosition());
        ps.setString(4, manager.getTelefon());
        ps.setString(5, manager.getEmail());
        ps.setInt(6, manager.getId());
        ps.execute();
    }

    public String changeManager(int id, int managerId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE sofortanfrage SET managerId = ? WHERE anfrageId = ?";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, managerId);
        ps.setInt(2, id);
        ps.execute();

        Status status = new Status("Angenommen","Die Anfrage wurde Angenommen");
        newStatus(id, status);

        Manager manager = getManager(id);
        return manager.getVorname()+" "+manager.getNachname();
    }

    public int newAuftrag(Anfrage anfrage, Status status) throws SQLException, ClassNotFoundException {
        int id = -1;
        //--ladestelle
        String sql = "INSERT INTO ladestelle (land,stadt,plz,datumUhrzeit) " +
                     "VALUES (?,?,?,?) RETURNING ladestelleid";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, anfrage.getLadestelle().getLand());
        ps.setString(2, anfrage.getLadestelle().getStadt());
        ps.setString(3, anfrage.getLadestelle().getPlz());
        ps.setTimestamp(4, Timestamp.valueOf(anfrage.getLadestelle().getDatum()));
        ResultSet rs = ps.executeQuery();
        rs.next();
        int ladestelleId = rs.getInt(1);

        //--entladestelle
        sql = "INSERT INTO ladestelle (land,stadt,plz,datumUhrzeit) " +
                "VALUES (?,?,?,?) RETURNING ladestelleid";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, anfrage.getEntladestelle().getLand());
        ps.setString(2, anfrage.getEntladestelle().getStadt());
        ps.setString(3, anfrage.getEntladestelle().getPlz());
        ps.setTimestamp(4, Timestamp.valueOf(anfrage.getEntladestelle().getDatum()));
        rs = ps.executeQuery();
        rs.next();
        int entladestelleId = rs.getInt(1);

        //--unternehmen
        int anredeId;
        sql = "SELECT anredeId FROM anrede WHERE bezeichnung = ?";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setString(1, anfrage.getFirma().getAnrede());
        rs = ps.executeQuery();
        rs.next();
        anredeId = rs.getInt("anredeId");

        sql = "INSERT INTO unternehmen (anredeId,vorname,nachname,firmenbezeichnung, telefon, email) " +
                "VALUES (?,?,?,?,?,?) RETURNING unternehmenid";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, anredeId);
        ps.setString(2, anfrage.getFirma().getVorname());
        ps.setString(3, anfrage.getFirma().getNachname());
        ps.setString(4, anfrage.getFirma().getFirmenbezeichnung());
        ps.setString(5, anfrage.getFirma().getTelefon());
        ps.setString(6, anfrage.getFirma().getEmail());
        rs = ps.executeQuery();
        rs.next();
        int unternehmenId = rs.getInt(1);

        //--sofortanfrage
        sql = "INSERT INTO sofortanfrage (unternehmenId, ladestelleId, entladestelleId, informationen) " +
                "VALUES (?,?,?,?) RETURNING anfrageid";
        ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, unternehmenId);
        ps.setInt(2, ladestelleId);
        ps.setInt(3, entladestelleId);
        ps.setString(4, anfrage.getInformation());
        rs = ps.executeQuery();
        rs.next();
        id = rs.getInt(1);

        //--transport
        if(anfrage.getLeistung().isTransport()){
            sql = "SELECT transportId FROM transport WHERE transportArt = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getLeistung().getTransportArt());
            rs = ps.executeQuery();
            rs.next();
            int transportId = rs.getInt("transportId");

            sql = "UPDATE sofortanfrage SET transportId = ? WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, transportId);
            ps.setInt(2, id);
            ps.execute();
        }

        //--messe
        if(anfrage.getLeistung().isMesselogistik()){
            sql = "INSERT INTO messe (messe, stadt, land, startdate, enddate) VALUES (?,?,?,?,?) RETURNING messeid";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getLeistung().getMesse());
            ps.setString(2, anfrage.getLeistung().getStadt());
            ps.setString(3, anfrage.getLeistung().getLand());
            ps.setDate(4, Date.valueOf(anfrage.getLeistung().getStart()));
            ps.setDate(5, Date.valueOf(anfrage.getLeistung().getEnde()));
            rs = ps.executeQuery();
            rs.next();
            int messeId = rs.getInt(1);

            sql = "UPDATE sofortanfrage SET messeId = ? WHERE anfrageId = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, messeId);
            ps.setInt(2, id);
            ps.execute();
        }

        //--komplettladung
        if(!anfrage.getSendung().isAsTeilpartien()){
            sql = "SELECT ladungsartId FROM ladungsart WHERE bezeichnung = ?";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setString(1, anfrage.getSendung().getLadungsArt());
            rs = ps.executeQuery();
            rs.next();
            int ladungsartId = rs.getInt("ladungsartId");

            sql = "INSERT INTO komplettladung (ladungsartId, anzahl, versicherung, angabe) VALUES (?,?,?,?) RETURNING komplettladungid";
            ps = Database.getInstance().getPreparedStatement(sql);
            ps.setInt(1, ladungsartId);
            ps.setInt(2, anfrage.getSendung().getAnzahl());
            ps.setBoolean(3, anfrage.getSendung().isVersicherung());
            ps.setString(4, anfrage.getSendung().getEigeneAngabe());
            rs = ps.executeQuery();
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
            sql = "INSERT INTO teilpartie (anzahl, inhalt, laenge, breite, hoehe, gewicht) VALUES (?,?,?,?,?,?) RETURNING teilpartieid";
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
                rs = ps.executeQuery();
                rs.next();
                int teilpartieId = rs.getInt(1);

                update.setInt(1, teilpartieId);
                update.setInt(2, id);
                update.execute();
            }
        }

        //--initial Aenderung
        newStatus(id,status);

        return id;
    }

    public void deleteAuftrag(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM sofortanfrage WHERE anfrageId = ?";
        PreparedStatement ps = Database.getInstance().getPreparedStatement(sql);
        ps.setInt(1, id);
        ps.execute();
    }
}
