package SupOrder;

public class OrderSupplier {
    private String lotId;
    private String supplier;
    private String dateRecieve;

    public OrderSupplier(String lotId, String supplier, String dateRecieve) {
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
