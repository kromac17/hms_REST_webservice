package syp.hms.hms_REST_webservice;

public class Land {
    private String Code;
    private String Name;

    public Land(String code, String name) {
        this.Code = code;
        this.Name = name;
    }

    public Land() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    @Override
    public String toString() {
        return "Land{" +
                "code='" + Code + '\'' +
                ", name='" + Name + '\'' +
                '}';
    }
}
