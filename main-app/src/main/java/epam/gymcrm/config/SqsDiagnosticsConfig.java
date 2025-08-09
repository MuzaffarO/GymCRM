//package epam.gymcrm.config;
//
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.sts.StsClient;
//
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.util.HexFormat;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class SqsDiagnosticsConfig {
//
//    @Value("${spring.cloud.aws.region.static}")
//    private String region;
//
//    @Value("${aws.sqs.queueUrl:}")
//    private String prodUrl;
//
//    @Value("${app.sqs.main-queue-url:}")
//    private String consUrl;
//
//    @PostConstruct
//    void logSqsConfig() throws Exception {
//        log.info("=== SQS DIAGNOSTICS ===");
//        log.info("AWS Region: {}", region);
//        log.info("ProducerURL='{}' len={} md5={}",
//                prodUrl, prodUrl.length(), md5(prodUrl));
//        log.info("ConsumerURL='{}' len={} md5={}",
//                consUrl, consUrl.length(), md5(consUrl));
//    }
//
//    @Bean
//    ApplicationRunner whoAmI(AwsCredentialsProvider credentialsProvider, Region r) {
//        return args -> {
//            try (StsClient sts = StsClient.builder()
//                    .region(r)
//                    .credentialsProvider(credentialsProvider)
//                    .build()) {
//                var id = sts.getCallerIdentity();
//                log.info("AWS caller ARN: {}", id.arn());
//                log.info("AWS account: {}", id.account());
//                log.info("AWS userId: {}", id.userId());
//            }
//        };
//    }
//
//    private String md5(String value) throws Exception {
//        return HexFormat.of().formatHex(
//                MessageDigest.getInstance("MD5")
//                        .digest(value.getBytes(StandardCharsets.UTF_8)));
//    }
//}
