package com.pregnancy.edu.pregnancy;

import com.pregnancy.edu.client.ai.chat.ChatClient;
import com.pregnancy.edu.client.ai.chat.dto.ChatRequest;
import com.pregnancy.edu.client.ai.chat.dto.ChatResponse;
import com.pregnancy.edu.client.ai.chat.dto.Message;
import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusRepository;
import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PregnancyService implements BaseCrudService<Pregnancy, Long> {

    private final PregnancyRepository pregnancyRepository;
    private final FetusRepository fetusRepository;
    private final ChatClient chatClient;

    private static final String STATUS_ONGOING = "ONGOING";

    public PregnancyService(PregnancyRepository pregnancyRepository,
                            FetusRepository fetusRepository, ChatClient chatClient) {
        this.pregnancyRepository = pregnancyRepository;
        this.fetusRepository = fetusRepository;
        this.chatClient = chatClient;
    }

    @Override
    public List<Pregnancy> findAll() {
        return pregnancyRepository.findAll();
    }

    @Override
    public Pregnancy findById(Long pregnancyId) {
        return pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new ObjectNotFoundException("pregnancy", pregnancyId));
    }

    @Override
    public Pregnancy save(Pregnancy pregnancy) {
        if (STATUS_ONGOING.equals(pregnancy.getStatus()) &&
                pregnancy.getUser() != null &&
                hasOngoingPregnancy(pregnancy.getUser().getId(), pregnancy.getId())) {
            throw new IllegalStateException("User already has an ongoing pregnancy");
        }
        return pregnancyRepository.save(pregnancy);
    }

    @Override
    public Pregnancy update(Long pregnancyId, Pregnancy pregnancy) {
        return pregnancyRepository.findById(pregnancyId)
                .map(existingPregnancy -> {
                    if (STATUS_ONGOING.equals(pregnancy.getStatus()) &&
                            !STATUS_ONGOING.equals(existingPregnancy.getStatus()) &&
                            pregnancy.getUser() != null &&
                            hasOngoingPregnancy(pregnancy.getUser().getId(), pregnancyId)) {
                        throw new IllegalStateException("User already has an ongoing pregnancy");
                    }

                    existingPregnancy.setMaternalAge(pregnancy.getMaternalAge());
                    existingPregnancy.setPregnancyStartDate(pregnancy.getPregnancyStartDate());
                    existingPregnancy.setEstimatedDueDate(pregnancy.getEstimatedDueDate());
                    existingPregnancy.setDeliveryDate(pregnancy.getDeliveryDate());
                    existingPregnancy.setStatus(pregnancy.getStatus());
                    existingPregnancy.setUser(pregnancy.getUser());

                    return pregnancyRepository.save(existingPregnancy);
                })
                .orElseThrow(() -> new ObjectNotFoundException("pregnancy", pregnancyId));
    }

    @Override
    public void delete(Long pregnancyId) {
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new ObjectNotFoundException("pregnancy", pregnancyId));

        // Handle fetus relationships before deleting
        List<Fetus> fetuses = pregnancy.getFetuses();
        if (fetuses != null && !fetuses.isEmpty()) {
            for (Fetus fetus : fetuses) {
                fetus.setPregnancy(null);
                fetusRepository.save(fetus);
            }
        }

        pregnancyRepository.deleteById(pregnancyId);
    }

    public Page<Pregnancy> findAll(Pageable pageable) {
        return pregnancyRepository.findAll(pageable);
    }

    public List<Pregnancy> findByUserId(Long userId) {
        return pregnancyRepository.findByUserId(userId);
    }

    public Page<Pregnancy> findByUserId(Long userId, Pageable pageable) {
        return pregnancyRepository.findByUserId(userId, pageable);
    }

    public List<Pregnancy> findByUserIdAndStatus(Long userId, String status) {
        return pregnancyRepository.findByUserIdAndStatus(userId, status);
    }

    public Pregnancy findOngoingPregnancyByUserId(Long userId) {
        List<Pregnancy> ongoingPregnancies = pregnancyRepository.findByUserIdAndStatus(userId, STATUS_ONGOING);
        if (ongoingPregnancies.isEmpty()) {
            return null;
        }
        return ongoingPregnancies.get(0);
    }

    public String getOnGoingWeekInsight(Long userId) {
        Pregnancy pregnancy = findOngoingPregnancyByUserId(userId);
        if (pregnancy == null) {
            return "No ongoing pregnancy found";
        }
        int currentGestationalWeek = pregnancy.getCurrentWeek();

        List<Message> messages = List.of(
                new Message("system", "Based on the provided gestational week information, give a concise 20-30 word insight in this period." +
                        " If measurements are mentioned (like weight, length, etc.)," +
                        " please convert them to Vietnamese units (grams to \"gram\", centimeters to \"cm\", inches to \"cm\", pounds to \"kg\")." +
                        " Keep the main response in English but include Vietnamese unit names. Don't include English unit names."),
                new Message("user", "Week" + currentGestationalWeek)
        );

        ChatRequest chatRequest = new ChatRequest("openai-community/gpt2", messages);
        chatRequest.setMaxTokens(500);
        chatRequest.setStream(false);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest);

        return chatResponse.choices().get(0).message().content();
    }

    public Pregnancy addFetusToPregnancy(Long pregnancyId, Fetus fetus) {
        Pregnancy pregnancy = findById(pregnancyId);
        fetus.setPregnancy(pregnancy);
        fetusRepository.save(fetus);
        pregnancy.getFetuses().add(fetus);
        return pregnancyRepository.save(pregnancy);
    }

    public Pregnancy removeFetusFromPregnancy(Long pregnancyId, Long fetusId) {
        Pregnancy pregnancy = findById(pregnancyId);
        Fetus fetus = fetusRepository.findById(fetusId)
                .orElseThrow(() -> new ObjectNotFoundException("fetus", fetusId));

        if (fetus.getPregnancy() != null && fetus.getPregnancy().getId().equals(pregnancyId)) {
            pregnancy.getFetuses().remove(fetus);
            fetus.setPregnancy(null);
            fetusRepository.save(fetus);
            return pregnancyRepository.save(pregnancy);
        } else {
            throw new RuntimeException("Fetus is not associated with this pregnancy");
        }
    }

    private boolean hasOngoingPregnancy(Long userId, Long currentPregnancyId) {
        List<Pregnancy> ongoingPregnancies = pregnancyRepository.findByUserIdAndStatus(userId, STATUS_ONGOING);

        if (ongoingPregnancies.isEmpty()) {
            return false;
        }

        if (currentPregnancyId != null) {
            return ongoingPregnancies.stream()
                    .anyMatch(pregnancy -> !pregnancy.getId().equals(currentPregnancyId));
        }

        return true;

    }
}