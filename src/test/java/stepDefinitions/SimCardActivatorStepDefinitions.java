package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.SimRequest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.java.ro.Si;
import io.cucumber.spring.CucumberContextConfiguration;

import org.junit.jupiter.api.Assertions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
  private final String BASE_URL = "http://localhost:8080/api/sim";
  private final RestTemplate restTemp = new RestTemplate();

  private String iccid;
  private String customerEmail;

  private ResponseEntity<String> activationResponse;
  private ResponseEntity<String> queryResponse;

  @Given("I have a SIM card with ICCID {string} and customer email {string}")
  public void i_have_a_sim_card_with_iccid_and_customer_email(String iccid, String customerEmail) {
    this.iccid = iccid;
    this.customerEmail = customerEmail;
  }

  @When("I submit an activation request")
  public void i_submit_an_activation_request() {
    SimRequest simRequest = new SimRequest();
    simRequest.setIccid(iccid);
    simRequest.setCostumerEmail(customerEmail);

    activationResponse =
            restTemp.postForEntity(BASE_URL + "/activate", simRequest, String.class);
  }

  @Then("the response should indicate success")
  public void the_response_should_indicate_success() {
    assertEquals(HttpStatus.OK, activationResponse.getStatusCode());
    assertTrue(activationResponse.getBody().contains("successful"));
  }
  @Then("the response should indicate unsuccessful")
  public void the_response_should_indicate_unsuccessful() {
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, activationResponse.getStatusCode());
    assertTrue(activationResponse.getBody().contains("unsuccessful"));
  }


  @And("the database should store the SIM card as active")
  public void database_should_store_active() {
    long simCardId = 1;
    queryResponse = restTemp.getForEntity
            (BASE_URL + "/query?simCardId=" + simCardId, String.class);
    Assertions.assertEquals(HttpStatus.OK, queryResponse.getStatusCode());
    Assertions.assertTrue(queryResponse.getBody().contains("\"active\":true"));
  }
  @And("the database should store the SIM card as inactive")
  public void database_should_store_inactive() {
    long simCardId = 2;
    queryResponse = restTemp.getForEntity
            (BASE_URL + "/query?simCardId=" + simCardId, String.class);
    Assertions.assertEquals(HttpStatus.OK, queryResponse.getStatusCode());
    Assertions.assertTrue(queryResponse.getBody().contains("\"active\":false"));
  }
}