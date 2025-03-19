package au.com.telstra.simcardactivator;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class representing a SIM card activation record.
 * This class maps to a database table using JPA.
 */
@Entity
@Table(name = "sim_card_activation")
public class SimCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String iccid;
  private String customerEmail;
  private boolean active;

  public SimCard() {}
  public SimCard(String iccid, String customerEmail,
                 boolean active) {
    this.iccid = iccid;
    this.customerEmail = customerEmail;
    this.active = active;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getIccid() {
    return iccid;
  }
  public void setIccid(String iccid) {
    this.iccid = iccid;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }
  public void setCustomerEmail(String costumerEmail) {
    this.customerEmail = costumerEmail;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
