//package epam.gymcrm.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.services.sqs.SqsClient;
//import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
//
//@Component
//@RequiredArgsConstructor
//class QueueDepthLogger {
//  private final SqsClient sqs;
//  @Value("${app.sqs.main-queue-url}") String url;
//
//  @Scheduled(fixedDelay = 5000, initialDelay = 2000)
//  void logDepth() {
//    var a = sqs.getQueueAttributes(b -> b.queueUrl(url)
//        .attributeNamesWithStrings("ApproximateNumberOfMessages","ApproximateNumberOfMessagesNotVisible"));
//    org.slf4j.LoggerFactory.getLogger(getClass())
//      .info("Depth visible={}, inflight={}",
//        a.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES),
//        a.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE));
//  }
//}
