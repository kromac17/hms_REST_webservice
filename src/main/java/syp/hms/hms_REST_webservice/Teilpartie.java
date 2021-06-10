package syp.hms.hms_REST_webservice;

public class Teilpartie {
    private int anzahl;
    private String inhalt;
    private double laenge;
    private double breite;
    private double hoehe;
    private double gewicht;

    public Teilpartie(int anzahl, String inhalt, double laenge, double breite, double hoehe, double gewicht) {
        this.anzahl = anzahl;
        this.inhalt = inhalt;
        this.laenge = laenge;
        this.breite = breite;
        this.hoehe = hoehe;
        this.gewicht = gewicht;
    }

    public Teilpartie() {}

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public double getLaenge() {
        return laenge;
    }

    public void setLaenge(double laenge) {
        this.laenge = laenge;
    }

    public double getBreite() {
        return breite;
    }

    public void setBreite(double breite) {
        this.breite = breite;
    }

    public double getHoehe() {
        return hoehe;
    }

    public void setHoehe(double hoehe) {
        this.hoehe = hoehe;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    @Override
    public String toString() {
        return "<tr>" +
                "<td>Anzahl: "+anzahl+"</td>" +
                "<td>Inhalt: "+inhalt+"</td>" +
                "<td>Laenge: "+laenge+"</td>" +
                "<td>Breite: "+breite+"</td>" +
                "<td>Höhe: "+hoehe+"</td>" +
                "<td>Gewicht: "+gewicht+"</td>" +
                "</tr>";
    }
}
