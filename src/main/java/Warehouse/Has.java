package Warehouse;

public class Has {
    private String L_id, P_id;
    private double P_amount;

    public Has(String l_id, String p_id, double p_amount) {
        L_id = l_id;
        P_id = p_id;
        P_amount = p_amount;
    }

    public String getL_id() {
        return L_id;
    }

    public void setL_id(String l_id) {
        L_id = l_id;
    }

    public String getP_id() {
        return P_id;
    }

    public void setP_id(String p_id) {
        P_id = p_id;
    }

    public double getP_amount() {
        return P_amount;
    }

    public void setP_amount(double p_amount) {
        P_amount = p_amount;
    }
}
