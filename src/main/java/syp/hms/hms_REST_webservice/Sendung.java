package syp.hms.hms_REST_webservice;

import java.util.List;

public class Sendung {
    private boolean komplett_teil;
    private List<Teilpartie> teilpartien;
    private String ladungsArt;
    private int anzahl;
    private boolean versicherung;
    private String eigeneAngabe;

    public Sendung(boolean komplett_teil, List<Teilpartie> teilpartien, String ladungsArt, int anzahl, boolean versicherung, String eigeneAngabe) {
        this.komplett_teil = komplett_teil;
        this.teilpartien = teilpartien;
        this.ladungsArt = ladungsArt;
        this.anzahl = anzahl;
        this.versicherung = versicherung;
        this.eigeneAngabe = eigeneAngabe;
    }

    public Sendung() {}

    public boolean isKomplett_teil() {
        return komplett_teil;
    }

    public void setKomplett_teil(boolean komplett_teil) {
        this.komplett_teil = komplett_teil;
    }

    public List<Teilpartie> getTeilpartien() {
        return teilpartien;
    }

    public void setTeilpartien(List<Teilpartie> teilpartien) {
        this.teilpartien = teilpartien;
    }

    public String getLadungsArt() {
        return ladungsArt;
    }

    public void setLadungsArt(String ladungsArt) {
        this.ladungsArt = ladungsArt;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public boolean isVersicherung() {
        return versicherung;
    }

    public void setVersicherung(boolean versicherung) {
        this.versicherung = versicherung;
    }

    public String getEigeneAngabe() {
        return eigeneAngabe;
    }

    public void setEigeneAngabe(String eigeneAngabe) {
        this.eigeneAngabe = eigeneAngabe;
    }
}
