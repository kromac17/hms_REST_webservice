package syp.hms.hms_REST_webservice;

import java.time.LocalDateTime;

public class Aenderung {
    public Status status;
    public LocalDateTime datum;

    public Aenderung(Status status, LocalDateTime aenderung) {
        this.status = status;
        this.datum = aenderung;
    }

    public Aenderung() {
    }

    @Override
    public String toString() {
        return "Aenderung{" +
                "status=" + status +
                ", datum=" + datum +
                '}';
    }
}
