package epam.uz.trainerworkloadservice.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.security.Key;

@Configuration
public class AwsClientsConfig {

    @Bean
    AwsCredentialsProvider awsCredentialsProvider(
            @org.springframework.beans.factory.annotation.Value("${aws.profile:}") String profile) {
        return (profile == null || profile.isBlank())
                ? DefaultCredentialsProvider.create()          // EC2/ECS/Env/System props/IMDS
                : ProfileCredentialsProvider.create(profile);  // local dev
    }

    @Bean
    Region awsRegion(@Value("${spring.cloud.aws.region.static}") String region) {
        return Region.of(region);
    }

    // For @SqsListener
    @Bean
    SqsAsyncClient sqsAsyncClient(Region region, AwsCredentialsProvider creds) {
        return SqsAsyncClient.builder().region(region).credentialsProvider(creds).build();
    }

    // For your manual controller retry endpoint
    @Bean
    SqsClient sqsClient(Region region, AwsCredentialsProvider creds) {
        return SqsClient.builder().region(region).credentialsProvider(creds).build();
    }
}
