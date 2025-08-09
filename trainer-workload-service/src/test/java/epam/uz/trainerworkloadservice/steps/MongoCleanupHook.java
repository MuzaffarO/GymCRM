//package epam.uz.trainerworkloadservice.steps;
//
//import epam.uz.trainerworkloadservice.repository.TrainerTrainingSummaryRepository;
//import io.cucumber.java.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class MongoCleanupHook {
//    @Autowired
//    private TrainerTrainingSummaryRepository repository;
//
//    @Before
//    public void cleanDatabase() {
//        repository.deleteAll();
//    }
//}
