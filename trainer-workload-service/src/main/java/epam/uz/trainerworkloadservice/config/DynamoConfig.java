package epam.uz.trainerworkloadservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoConfig {

  @Bean
  public DynamoDbClient dynamoDbClient(
          Region region,
          AwsCredentialsProvider creds,
          @Value("${aws.dynamodb.endpoint:}") String endpoint) {

    var builder = DynamoDbClient.builder()
            .region(region)
            .credentialsProvider(creds);

    if (endpoint != null && !endpoint.isBlank()) {
      builder.endpointOverride(URI.create(endpoint));
    }

    return builder.build();
  }

  @Bean
  public DynamoDbEnhancedClient enhancedClient(DynamoDbClient client) {
    return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(client)
            .build();
  }
}
