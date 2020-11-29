package Warehouse;

public class Lots {
    private String lotId;
    private String supplier;
    private String dateRecieve;

    public Lots(String lotId, String supplier, String dateRecieve) {
        this.lotId = lotId;
        this.supplier = supplier;
        this.dateRecieve = dateRecieve;
    }

    public String getLotId() {
        return lotId;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getDateRecieve() {
        return dateRecieve;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }
}
