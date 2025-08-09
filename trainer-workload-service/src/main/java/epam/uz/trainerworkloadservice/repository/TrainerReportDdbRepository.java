package epam.uz.trainerworkloadservice.repository;

import epam.uz.trainerworkloadservice.model.TrainerReportDdb;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainerReportDdbRepository {

    private final DynamoDbEnhancedClient enhanced;
    @Value("${app.dynamodb.table}")
    private String tableName;

    private DynamoDbTable<TrainerReportDdb> table() {
        return enhanced.table(tableName, TableSchema.fromBean(TrainerReportDdb.class));
    }

    public Optional<TrainerReportDdb> get(String trainerUsername, boolean active) {
        String sk = active ? "Active" : "Inactive";
        Key key = Key.builder().partitionValue(trainerUsername).sortValue(sk).build();
        return Optional.ofNullable(table().getItem(r -> r.key(key)));
    }

    public void save(TrainerReportDdb item) {
        table().putItem(item);
    }

    public List<TrainerReportDdb> findByName(String firstName, String lastNamePrefix) {
        DynamoDbIndex<TrainerReportDdb> idx = table().index("TrainerName-Index");

        QueryConditional qc = QueryConditional.sortBeginsWith(
                Key.builder().partitionValue(firstName).sortValue(lastNamePrefix).build()
        );

        List<TrainerReportDdb> out = new ArrayList<>();
        idx.query(r -> r.queryConditional(qc))
                .forEach(page -> out.addAll(page.items()));
        return out;
    }

}
