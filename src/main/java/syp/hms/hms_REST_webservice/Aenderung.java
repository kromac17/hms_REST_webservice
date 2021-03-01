package syp.hms.hms_REST_webservice;

import java.time.LocalDateTime;

public class Aenderung {
    public String status;
    public LocalDateTime datum;

    public Aenderung(String status, LocalDateTime aenderung) {
        this.status = status;
        this.datum = aenderung;
    }

}
