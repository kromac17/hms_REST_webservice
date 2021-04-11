package syp.hms.hms_REST_webservice;

import java.util.List;

public class Sendung {
    private boolean asTeilpartien;
    private List<Teilpartie> teilpartien;
    private String ladungsArt;
    private int anzahl;
    private boolean versicherung;
    private String eigeneAngabe;

    public Sendung(boolean asTeilpartien, List<Teilpartie> teilpartien, String ladungsArt, int anzahl, boolean versicherung, String eigeneAngabe) {
        this.asTeilpartien = asTeilpartien;
        this.teilpartien = teilpartien;
        this.ladungsArt = ladungsArt;
        this.anzahl = anzahl;
        this.versicherung = versicherung;
        this.eigeneAngabe = eigeneAngabe;
    }

    public Sendung() {}

    public boolean isAsTeilpartien() {
        return asTeilpartien;
    }

    public void setAsTeilpartien(boolean asTeilpartien) {
        this.asTeilpartien = asTeilpartien;
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

    @Override
    public String toString() {
        return "Sendung{" +
                "asTeilpartien=" + asTeilpartien +
                ", teilpartien=" + teilpartien +
                ", ladungsArt='" + ladungsArt + '\'' +
                ", anzahl=" + anzahl +
                ", versicherung=" + versicherung +
                ", eigeneAngabe='" + eigeneAngabe + '\'' +
                '}';
    }
}
