package epam.gymcrm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsClientsConfig {

  @Bean
  AwsCredentialsProvider awsCredentialsProvider(@Value("${aws.profile}") String profile) {
    return ProfileCredentialsProvider.create(profile);
  }

  @Bean
  Region awsRegion(@Value("${spring.cloud.aws.region.static}") String region) {
    return Region.of(region);
  }

  @Bean
  SqsClient sqsClient(Region region, AwsCredentialsProvider creds) {
    return SqsClient.builder().region(region).credentialsProvider(creds).build();
  }
}
