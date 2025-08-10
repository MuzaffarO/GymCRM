package epam.uz.csvreportlambda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import epam.uz.csvreportlambda.model.TrainerReportDdb;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportLambda implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final String tableName = getenvRequired("TABLE_NAME");
    private final String bucketName = getenvRequired("BUCKET_NAME");
    private final String reportPrefix = System.getenv().getOrDefault("REPORT_PREFIX", "");
    private final ZoneId zone = ZoneId.of(System.getenv().getOrDefault("TIMEZONE", "UTC"));
    private final Region region = Region.of(System.getenv().getOrDefault("AWS_REGION", "us-east-1"));

    private final DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    private final DynamoDbEnhancedClient enhanced = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    private final S3Client s3 = S3Client.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        var logger = context.getLogger();

        LocalDate today = LocalDate.now(zone);
        int year = today.getYear();
        int month = today.getMonthValue();

        DynamoDbTable<TrainerReportDdb> table =
                enhanced.table(tableName, TableSchema.fromBean(TrainerReportDdb.class));

//        PageIterable<TrainerReportDdb> pages = table.scan();
//        List<TrainerReportDdb> items = new ArrayList<>();
//        pages.stream().forEach(p -> items.addAll(p.items()));
        Expression trainersOnly = Expression.builder()
                .expression("attribute_exists(#tu)")
                .putExpressionName("#tu", "trainerUsername")
                .build();

        ScanEnhancedRequest scanReq = ScanEnhancedRequest.builder()
                .filterExpression(trainersOnly)
                .build();

        List<TrainerReportDdb> items = table.scan(scanReq)
                .items()
                .stream()
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("Trainer First Name,Trainer Last Name,")
                .append(String.format("%04d-%02d Hours", year, month))
                .append("\n");

        int rows = 0;
        for (TrainerReportDdb it : items) {
            String first = nullToEmpty(it.getFirstName());
            String last = nullToEmpty(it.getLastName());
            boolean active = Boolean.TRUE.equals(it.getActive());
            double total = currentMonthHours(it, year, month);

            if (active || (!active && total > 0.0d)) {
                sb.append(csvCell(first)).append(",")
                        .append(csvCell(last)).append(",")
                        .append(total).append("\n");
                rows++;
            }
        }

        String key = reportPrefix + String.format("Trainers_Trainings_summary_%04d_%02d.csv", year, month);
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("text/csv")
                .build();

        s3.putObject(put, RequestBody.fromBytes(sb.toString().getBytes(StandardCharsets.UTF_8)));
        logger.log("Uploaded report to s3://" + bucketName + "/" + key + " with " + rows + " rows.");

        return Map.of("status", "OK", "s3Key", key, "rows", rows);
    }

    private static String getenvRequired(String name) {
        String v = System.getenv(name);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return v;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String csvCell(String s) {
        if (s.contains(",") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static double currentMonthHours(TrainerReportDdb item, int year, int month) {
        if (item.getYearlySummaries() == null) return 0.0d;
        for (var y : item.getYearlySummaries()) {
            if (y == null) continue;
            if (y.getYear() == year && y.getMonthlySummaries() != null) {
                for (var m : y.getMonthlySummaries()) {
                    if (m != null && m.getMonth() == month) {
                        return m.getTotalHours();
                    }
                }
            }
        }
        return 0.0d;
    }
}
