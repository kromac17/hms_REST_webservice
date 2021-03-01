package syp.hms.hms_REST_webservice;

public class Firma {
    private String firmenbezeichnung;
    private String anrede;
    private String vorname;
    private String nachname;
    private String email;
    private String telefon;

    public Firma(String firmenbezeichnung, String anrede, String vorname, String nachname, String email, String telefon) {
        this.firmenbezeichnung = firmenbezeichnung;
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.telefon = telefon;
    }

    public Firma() {}

    public String getFirmenbezeichnung() {
        return firmenbezeichnung;
    }

    public void setFirmenbezeichnung(String firmenbezeichnung) {
        this.firmenbezeichnung = firmenbezeichnung;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
