package au.com.telstra.simcardactivator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/sim")

/**
 * REST Controller for handling SIM activation requests.
 * This service receives activation requests
 * and forwards them to an external actuator microservice.
 */
public class SimActivationController {
    private final RestTemplate restTemp = new RestTemplate();

  /**
   * Handles a POST request to activate a SIM card.
   *
   * @param request the incoming request containing the ICCID and customer email.
   * @return A {@link ResponseEntity} containing the activation status.
   */
  @PostMapping("/activate")
  public ResponseEntity<String> activateSim(@RequestBody SimRequest request) {
    String iccid = request.getIccid();

    ActuatorRequest actuatorRequest = new ActuatorRequest(iccid);

    ResponseEntity<ActuatorRequest> response = restTemp.postForEntity(
            "http://localhost:8444/actuate",
            actuatorRequest,
            ActuatorRequest.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      boolean success = response.getBody().isSuccess() && response.getBody() != null;
      if (success) {
        System.out.println("Activation: " + iccid);
        return ResponseEntity.ok(iccid + " Successful");
      } else {
        System.out.println("Failed Activation: " + iccid);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Activation request: " + iccid + " failed");
      }
    } else {
      System.out.println("Error: Actuator service returned " + response.getStatusCode());
      return ResponseEntity.status(response.getStatusCode())
              .body("Failed to activate ICCID " + iccid + ". Actuator service error.");
    }
  }
}
