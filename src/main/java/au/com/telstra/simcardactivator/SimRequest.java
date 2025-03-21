package au.com.telstra.simcardactivator;

/**
 * Represents the request payload received
 * by the microservice for SIM activation.
 * This class encapsulates the ICCID of the SIM card
 * and the customer's email address.
 */
public class SimRequest {
  private String iccid;
  private String costumerEmail;

  public String getIccid() {
    return iccid;
  }
  public void setIccid(String iccid) {
    this.iccid = iccid;
  }

  public String getCostumerEmail() {
    return costumerEmail;
  }
  public void setCostumerEmail(String cosutmerEmail) {
    this.costumerEmail = cosutmerEmail;
  }

}
