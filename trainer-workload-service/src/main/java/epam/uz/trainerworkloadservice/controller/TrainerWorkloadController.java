package epam.uz.trainerworkloadservice.controller;

import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.model.MonthlySummary;
import epam.uz.trainerworkloadservice.model.TrainerSummary;
import epam.uz.trainerworkloadservice.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workload")
@RequiredArgsConstructor
public class TrainerWorkloadController {

    private final TrainerWorkloadService workloadService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + auth);
        return ResponseEntity.ok("You are authenticated");
    }


    @PostMapping
    public ResponseEntity<String> recordWorkload(@RequestBody TrainerWorkloadRequest request) {
        workloadService.processWorkload(request);
        return ResponseEntity.ok("Trainer workload processed successfully.");
    }

    @GetMapping("/{username}/{year}/{month}")
    public ResponseEntity<Double> getMonthlySummary(
            @PathVariable String username,
            @PathVariable int year,
            @PathVariable int month) {

        TrainerSummary trainer = workloadService
                .getTrainerByUsername(username);

        MonthlySummary summary = workloadService
                .getMonthlySummary(trainer, year, month);

        return ResponseEntity.ok(summary.getTotalHours());
    }

    @GetMapping(value = "{username}/summary", produces = "application/json")
    public TrainerMonthlySummaryDTO getMonthlySummary(@PathVariable String username) {
        return workloadService.getTrainerMonthlySummary(username);
    }
}
