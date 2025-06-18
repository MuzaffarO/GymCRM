package epam.uz.trainerworkloadservice.controller;

import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workload")
@RequiredArgsConstructor
public class TrainerWorkloadController {

    private final TrainerMongoWorkloadService workloadService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + auth);
        return ResponseEntity.ok("You are authenticated");
    }

    @PostMapping
    public ResponseEntity<String> recordWorkload(@RequestBody TrainerWorkloadRequest request,
                                                 @RequestHeader(value = "X-Transaction-Id", required = false) String txnId) {
        workloadService.processWorkload(request); // txnId could be passed into service if needed
        return ResponseEntity.ok("Trainer workload processed successfully.");
    }

    @GetMapping("/{username}/{year}/{month}")
    public ResponseEntity<Double> getMonthlySummary(
            @PathVariable String username,
            @PathVariable int year,
            @PathVariable int month) {
        double hours = workloadService.getMonthlyHours(username, year, month);
        return ResponseEntity.ok(hours);
    }

    @GetMapping(value = "{username}/summary", produces = "application/json")
    public ResponseEntity<TrainerMonthlySummaryDTO> getMonthlySummary(@PathVariable String username) {
        TrainerMonthlySummaryDTO summary = workloadService.getTrainerMonthlySummary(username);
        return ResponseEntity.ok(summary);
    }
}
