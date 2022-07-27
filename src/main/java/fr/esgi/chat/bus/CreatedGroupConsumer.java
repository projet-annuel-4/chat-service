package fr.esgi.chat.bus;

import fr.esgi.chat.dto.GroupEvent;
import fr.esgi.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component("createdGroupConsumer")
public class CreatedGroupConsumer implements Consumer<Message<GroupEvent>> {

     private final GroupService groupService;

    @Override
    public void accept(Message<GroupEvent> message) {
        GroupEvent groupEvent = message.getPayload();
        MessageHeaders messageHeaders = message.getHeaders();
        log.info("Group event with id '{}' and name '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                groupEvent.getId(),
                groupEvent.getName(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));
        var group = groupService.createGroup(groupEvent);
        log.info("Group with id {} saved.", groupEvent.getId());
    }
}