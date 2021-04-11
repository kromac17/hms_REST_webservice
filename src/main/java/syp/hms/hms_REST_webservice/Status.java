package syp.hms.hms_REST_webservice;

public class Status {
    public String titel;
    public String status;

    public Status(String titel, String status) {
        this.titel = titel;
        this.status = status;
    }

    public Status() {
    }

    @Override
    public String toString() {
        return "Status{" +
                "titel='" + titel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
