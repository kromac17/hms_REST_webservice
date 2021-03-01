package syp.hms.hms_REST_webservice;

public class Anfrage {
    private Firma firma;
    private Leistung leistung;
    private Sendung sendung;
    private Ladestelle ladestelle;
    private Ladestelle entladestelle;
    private Rueckverladung rueckverladung;
    private String information;

    public Anfrage(Firma firma, Leistung leistung, Sendung sendung, Ladestelle ladestelle, Ladestelle entladestelle, Rueckverladung rueckverladung, String information) {
        this.firma = firma;
        this.leistung = leistung;
        this.sendung = sendung;
        this.ladestelle = ladestelle;
        this.entladestelle = entladestelle;
        this.rueckverladung = rueckverladung;
        this.information = information;
    }

    public Anfrage() {}

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public Leistung getLeistung() {
        return leistung;
    }

    public void setLeistung(Leistung leistung) {
        this.leistung = leistung;
    }

    public Sendung getSendung() {
        return sendung;
    }

    public void setSendung(Sendung sendung) {
        this.sendung = sendung;
    }

    public Ladestelle getLadestelle() {
        return ladestelle;
    }

    public void setLadestelle(Ladestelle ladestelle) {
        this.ladestelle = ladestelle;
    }

    public Ladestelle getEntladestelle() {
        return entladestelle;
    }

    public void setEntladestelle(Ladestelle entladestelle) {
        this.entladestelle = entladestelle;
    }

    public Rueckverladung getRueckverladung() {
        return rueckverladung;
    }

    public void setRueckverladung(Rueckverladung rueckverladung) {
        this.rueckverladung = rueckverladung;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

}