package au.com.telstra.simcardactivator;

/**
 * Represents the request payload sent to
 * the actuator service for SIM activation.
 * This class encapsulates the ICCID of the SIM card to be activated.
 */
public class ActuatorRequest {
  private String iccid;
  private boolean success;

  /**
   * Constructs a new ActuatorRequest
   * with the specified ICCID.
   *
   * @param iccid The unique identifier of the SIM card
   */
  public ActuatorRequest(String iccid) {
    this.iccid = iccid;
  }

  public String getIccid() {
    return iccid;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
