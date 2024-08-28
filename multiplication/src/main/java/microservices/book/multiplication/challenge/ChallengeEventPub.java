package microservices.book.multiplication.challenge;

import microservices.book.event.challenge.ChallengeSolvedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChallengeEventPub {
    private final PulsarTemplate<ChallengeSolvedEvent> pulsarTemplate;
    private final String topic;

    public ChallengeEventPub(final PulsarTemplate<ChallengeSolvedEvent> pulsarTemplate, @Value("${pulsar.attempts.topic}") final String topic) {
        this.pulsarTemplate = pulsarTemplate;
        this.topic = topic;
    }

    public void challengeSolved(final ChallengeAttempt challengeAttempt) {
        ChallengeSolvedEvent event = buildEvent(challengeAttempt);
        pulsarTemplate.newMessage(event)
                .withTopic(topic)
                .withMessageCustomizer((mb)-> mb.key(String.valueOf(event.getAttemptId())))
                .send();
    }

    private ChallengeSolvedEvent buildEvent(final ChallengeAttempt attempt) {
        return new ChallengeSolvedEvent(attempt.getId(),
                attempt.isCorrect(), attempt.getFactorA(),
                attempt.getFactorB(), attempt.getUser().getId(),
                attempt.getUser().getAlias());
    }
}