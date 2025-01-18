package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import javax.security.auth.Subject;
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
        Subscriber subDB = this.subscriberRepository.findByEmail(subscriber.getEmail());
        if(subDB != null) {
            throw new IdInvalidException("Subscriber already exists");
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




}
