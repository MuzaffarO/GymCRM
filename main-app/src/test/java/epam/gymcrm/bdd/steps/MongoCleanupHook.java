package epam.gymcrm.bdd.steps;

import epam.gymcrm.repository.TraineeRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoCleanupHook {
    @Autowired
    private TraineeRepository repository;

    @Before
    public void cleanDatabase() {
        repository.deleteAll();
    }
}
