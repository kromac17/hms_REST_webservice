package syp.hms.hms_REST_webservice;

import java.time.LocalDate;

public class Leistung {
    private boolean transport;
    private boolean messelogistik;
    private String messe;
    private String land;
    private String stadt;
    private LocalDate start;
    private LocalDate ende;
    private String transportArt;

    public Leistung(boolean transport, boolean messelogistik, String messe, String land, String stadt, LocalDate start, LocalDate ende, String transportArt) {
        this.transport = transport;
        this.messelogistik = messelogistik;
        this.messe = messe;
        this.land = land;
        this.stadt = stadt;
        this.start = start;
        this.ende = ende;
        this.transportArt = transportArt;
    }

    public Leistung() {}

    public boolean isTransport() {
        return transport;
    }

    public void setTransport(boolean transport) {
        this.transport = transport;
    }

    public boolean isMesselogistik() {
        return messelogistik;
    }

    public void setMesselogistik(boolean messelogistik) {
        this.messelogistik = messelogistik;
    }

    public String getMesse() {
        return messe;
    }

    public void setMesse(String messe) {
        this.messe = messe;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnde() {
        return ende;
    }

    public void setEnde(LocalDate ende) {
        this.ende = ende;
    }

    public String getTransportArt() {
        return transportArt;
    }

    public void setTransportArt(String transportArt) {
        this.transportArt = transportArt;
    }
}
