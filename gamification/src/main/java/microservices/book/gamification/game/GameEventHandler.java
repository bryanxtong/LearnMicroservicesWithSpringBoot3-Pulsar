package microservices.book.gamification.game;

import microservices.book.event.challenge.ChallengeSolvedEvent;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class GameEventHandler {

    private final GameService gameService;

    @PulsarListener(topics = "attempts.topic",subscriptionName = "gamification", subscriptionType = SubscriptionType.Key_Shared)
    void handleMultiplicationSolved(final ChallengeSolvedEvent event) {
        log.info("Challenge Solved Event received: {}", event.getAttemptId());
        try {
            gameService.newAttemptForUser(event);
        } catch (final Exception e) {
            log.error("Error when trying to process ChallengeSolvedEvent", e);
        }
    }

}