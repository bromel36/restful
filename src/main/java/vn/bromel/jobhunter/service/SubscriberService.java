package vn.bromel.jobhunter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.bromel.jobhunter.domain.Job;
import vn.bromel.jobhunter.domain.Skill;
import vn.bromel.jobhunter.domain.Subscriber;
import vn.bromel.jobhunter.domain.response.MailResponseDTO;
import vn.bromel.jobhunter.repository.JobRepository;
import vn.bromel.jobhunter.repository.SubscriberRepository;
import vn.bromel.jobhunter.util.error.IdInvalidException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;
    private final MailService mailService;
    private final JobRepository jobRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillService skillService, MailService mailService, JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
        this.mailService = mailService;
        this.jobRepository = jobRepository;
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

//    @Scheduled(cron = "0/30 * * * * ?")
//    public void testCron(){
//        log.info("TEST");
//    }

    public void sendMailToSubscriber() {
        List<Subscriber> subs = getSubscribers();
        if (subs != null) {
            for (Subscriber sub : subs) {
                List<Skill> skills = sub.getSkills();
                if (skills != null && !skills.isEmpty()) {
                    List<Job> jobs = new ArrayList<>(this.jobRepository.findAllBySkillsIn(skills));
                    if (!jobs.isEmpty()) {
                        MailResponseDTO dto = convertToMailResponseDTO(jobs, sub);
                        this.mailService.sendEmailFromTemplateSync(
                                dto.getMailTo(),
                                dto.getSubject(),
                                dto.getJobs(),
                                dto.getReceiverName(),
                                "job"
                        );
                    }
                }
            }
        }
    }

    public List<Subscriber> getSubscribers() {
        return subscriberRepository.findAll();
    }

    public MailResponseDTO convertToMailResponseDTO(List<Job> jobs, Subscriber sub) {
        MailResponseDTO dto = new MailResponseDTO();
        List<MailResponseDTO.MailJob> mailJobs = new ArrayList<>();
        jobs.forEach(j -> {
            MailResponseDTO.MailJob mailJob = MailResponseDTO.MailJob.builder()
                    .name(j.getName())
                    .companyName(j.getCompany().getName())
                    .salary(j.getSalary())
                    .build();

            List<String> skills = j.getSkills().stream().map(Skill::getName).toList();
            mailJob.setSkills(skills);
            mailJobs.add(mailJob);
        });
        dto.setJobs(mailJobs);
        dto.setMailTo(sub.getEmail());
        dto.setSubject("Job Opportunities Are Waiting – Explore Now!");
        dto.setReceiverName(sub.getName());
        return dto;
    }

}
