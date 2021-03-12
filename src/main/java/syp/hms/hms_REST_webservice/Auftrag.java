package syp.hms.hms_REST_webservice;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Auftrag {
    private int id;
    private Anfrage anfrage;
    private String manager;
    private String status;
    private List<Aenderung> aenderungen;

    //löschen
    public Auftrag(int id, Anfrage anfrage) {
        this.id = id;
        this.anfrage = anfrage;
        this.manager = "";
        this.status = "";
        this.aenderungen = new LinkedList<>();
    }

    public Auftrag(int id, Anfrage anfrage, String manager, String status, List<Aenderung> aenderungen) {
        this.id = id;
        this.anfrage = anfrage;
        this.manager = manager;
        this.status = status;
        this.aenderungen = aenderungen;
    }

    public Auftrag() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        aenderungen.add(new Aenderung(status, LocalDateTime.now()));
    }

    public List<Aenderung> getAenderungen() {
        return aenderungen;
    }

    public void setAenderungen(List<Aenderung> aenderungen) {
        this.aenderungen = aenderungen;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
        aenderungen.add(new Aenderung("Manager = " + manager, LocalDateTime.now()));
    }

    public Anfrage getAnfrage() {
        return anfrage;
    }

    public void setAnfrage(Anfrage anfrage) {
        this.anfrage = anfrage;
    }
}