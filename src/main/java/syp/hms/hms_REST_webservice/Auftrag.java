package syp.hms.hms_REST_webservice;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Auftrag implements Comparable{
    private int id;
    private Anfrage anfrage;
    private Manager manager;
    private List<Aenderung> aenderungen;

    public Auftrag(int id, Anfrage anfrage, Manager manager, List<Aenderung> aenderungen) {
        this.id = id;
        this.anfrage = anfrage;
        this.manager = manager;
        this.aenderungen = aenderungen;
    }

    public Auftrag() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String titel, String status) {
        aenderungen.add(new Aenderung(new Status(titel, status), LocalDateTime.now()));
    }

    public List<Aenderung> getAenderungen() {
        return aenderungen;
    }

    public void setAenderungen(List<Aenderung> aenderungen) {
        this.aenderungen = aenderungen;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
        aenderungen.add(new Aenderung(new Status("Angenommen",manager + " hat die Anfragte angenommen"), LocalDateTime.now()));
    }

    public Anfrage getAnfrage() {
        return anfrage;
    }

    public void setAnfrage(Anfrage anfrage) {
        this.anfrage = anfrage;
    }

    @Override
    public int compareTo(Object other) {
        Auftrag a = (Auftrag) other;
        if(this.getId() > a.getId())
            return 1;
        else
            return -1;
    }
}