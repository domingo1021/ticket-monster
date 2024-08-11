package cart.ticket.ticketservice.application.service;

import cart.ticket.ticketservice.application.exception.MessageSendFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

  private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
  public static final String DLQ_SUFFIX = ".dlq";
  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000))
  public void sendMessage(String topic, String message) {
    try {
      kafkaTemplate
          .send(topic, message)
          .whenComplete(
              (result, ex) -> {
                if (ex != null) {
                  logger.error("Unable to send message: {}", ex.getMessage());
                } else {
                  logger.info("Message sent successfully");
                }
              });
    } catch (Exception e) {
      logger.error("Failed to send Kafka message {}", e.getMessage(), e);
      throw e;
    }
  }

  @Recover
  public void recover(Exception _ex, String topic, String message) {
    String dlqTopic = topic + DLQ_SUFFIX;
    try {
      kafkaTemplate.send(dlqTopic, message);
      logger.info("Message sent to DLQ {} after retry failure", dlqTopic);
    } catch (Exception e) {
      logger.error("Failed to send message to DLQ {}", e.getMessage(), e);
    }

    // re-throw the exception to trigger failover mechanism in client code.
    throw new MessageSendFailureException("Failed to send message after retries, sent to DLQ");
  }
}
