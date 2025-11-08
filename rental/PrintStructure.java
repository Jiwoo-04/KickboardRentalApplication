package rental;

public class PrintStructure {
    int RN;
    String id;
    String type;
    Boolean status;

    public PrintStructure(int RN, String id, String type, Boolean status) {
        this.RN = RN;
        this.id = id;
        this.type = type;
        this.status = status;
    }
    public int getRN() {
        return RN;
    }
    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public Boolean getStatus() {
        return status;
    }
}
