package syp.hms.hms_REST_webservice;

import java.time.LocalDateTime;

public class Ladestelle {
    private String land;
    private String stadt;
    private String plz;
    private LocalDateTime datum;

    public Ladestelle(String land, String stadt, String plz, LocalDateTime datum) {
        this.land = land;
        this.stadt = stadt;
        this.plz = plz;
        this.datum = datum;
    }

    public Ladestelle() {}

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

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "Ladestelle{" +
                "land='" + land + '\'' +
                ", stadt='" + stadt + '\'' +
                ", plz='" + plz + '\'' +
                ", datum=" + datum +
                '}';
    }
}
