package syp.hms.hms_REST_webservice;

import java.time.LocalDateTime;

public class Rueckverladung {
    private boolean inkludiert;
    private LocalDateTime datum;

    public Rueckverladung(boolean inkludiert, LocalDateTime datum) {
        this.inkludiert = inkludiert;
        this.datum = datum;
    }

    public Rueckverladung() {}

    public boolean isInkludiert() {
        return inkludiert;
    }

    public void setInkludiert(boolean inkludiert) {
        this.inkludiert = inkludiert;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "Rueckverladung{" +
                "inkludiert=" + inkludiert +
                ", datum=" + datum +
                '}';
    }
}
