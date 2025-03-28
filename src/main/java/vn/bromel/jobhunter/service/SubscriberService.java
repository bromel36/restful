package vn.bromel.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.bromel.jobhunter.domain.Skill;
import vn.bromel.jobhunter.domain.Subscriber;
import vn.bromel.jobhunter.repository.SubscriberRepository;
import vn.bromel.jobhunter.util.error.IdInvalidException;

import java.util.List;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillService skillService) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
    }

    public Subscriber handleCreateSubscriber(Subscriber subscriber) {
        if (isExistByEmail(subscriber.getEmail())) {
            throw new IdInvalidException("Email already exists");
        }

        List<Long> skillRequestId = subscriber.getSkills().stream()
                .map(Skill::getId)
                .toList();

        List<Skill> skillDB = skillService.handleFindSkills(skillRequestId);

        subscriber.setSkills(skillDB);

        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriber) {
        Subscriber subDB = this.subscriberRepository.findById(subscriber.getId())
                .orElseThrow(() -> new IdInvalidException("Subscriber does not exist"));

        List<Long> skillRequestId = subscriber.getSkills().stream()
                .map(Skill::getId)
                .toList();

        List<Skill> skillDB = skillService.handleFindSkills(skillRequestId);

        subDB.setSkills(skillDB);
        return this.subscriberRepository.save(subDB);
    }

    public boolean isExistByEmail(String email) {
        return subscriberRepository.existsByEmail(email);
    }

}
