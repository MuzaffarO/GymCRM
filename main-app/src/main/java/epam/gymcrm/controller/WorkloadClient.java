//package epam.gymcrm.controller;// package: epam.gymcrm.client
//
//import epam.gymcrm.dto.microservice.TrainerWorkloadRequest;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//@RequiredArgsConstructor
//public class WorkloadClient {
//
//    private final RestTemplate restTemplate;
//
//    @Value("${workload.service.url:http://trainer-workload-service/api/workload}")
//    private String workloadServiceUrl;
//
//    @CircuitBreaker(name = "workloadService", fallbackMethod = "fallbackSendWorkload")
//    public void sendWorkloadUpdate(TrainerWorkloadRequest request, String jwtToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(jwtToken);
//
//        HttpEntity<TrainerWorkloadRequest> entity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<Void> response = restTemplate.exchange(
//                workloadServiceUrl, HttpMethod.POST, entity, Void.class);
//
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException("Workload update failed with status: " + response.getStatusCode());
//        }
//    }
//
//    public void fallbackSendWorkload(TrainerWorkloadRequest request, String jwtToken, Throwable t) {
//        System.err.println("❗ Circuit Breaker triggered: Workload service unavailable. Reason: " + t.getMessage());
//    }
//}
