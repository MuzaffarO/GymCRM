package epam.gymcrm.controller;// package: epam.gymcrm.client

import epam.gymcrm.dto.microservice.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WorkloadClient {

    private final RestTemplate restTemplate;

    @Value("${workload.service.url}")
    private String workloadServiceUrl; // e.g. http://localhost:8081/api/workload

    public void sendWorkloadUpdate(TrainerWorkloadRequest request, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<TrainerWorkloadRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                workloadServiceUrl, HttpMethod.POST, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Workload update failed with status: " + response.getStatusCode());
        }
    }
}
