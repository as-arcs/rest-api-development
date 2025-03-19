package au.com.telstra.simcardactivator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/sim")

public class SimActivationController {
  private final RestTemplate restTemp = new RestTemplate();
  private final SimCardRepository simCardRepository;

  public SimActivationController(SimCardRepository simCardRepository) {
    this.simCardRepository = simCardRepository;
  }

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
      boolean success = response.getBody() != null && response.getBody().isSuccess();
      SimCard simCard = new SimCard(iccid, request.getCostumerEmail(), success);
      simCardRepository.save(simCard);
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

  @GetMapping("/query")
  public ResponseEntity<?> getSimActivationRecord(@RequestParam Long simCardId) {
    Optional<SimCard> simCard = simCardRepository.findById(simCardId);
    if (simCard.isPresent()) {
      return ResponseEntity.ok(simCard.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body("No record found for SIM Card ID: " + simCardId);
    }
  }

}
