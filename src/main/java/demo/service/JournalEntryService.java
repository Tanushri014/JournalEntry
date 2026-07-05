package demo.service;

import demo.dto.QuoteResponse;
import demo.entity.JournalEntry;
import demo.entity.User;
import demo.enums.Sentiment;
import demo.exception.JournalEntryNotFoundException;
import demo.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    /**
     * Get all journal entries of the logged-in user.
     */
    public List<JournalEntry> getAllEntries(String userEmail) {

        return journalEntryRepository.findByUserUserEmail(userEmail);
    }

    /**
     * Get a specific journal entry.
     * Only if it belongs to the logged-in user.
     */
    public JournalEntry getEntryById(Long id, String userEmail) {
        return journalEntryRepository
                .findByIdAndUserUserEmail(id, userEmail)
                .orElseThrow(() ->
                        new JournalEntryNotFoundException("Journal entry not found."));

    }

    /**
     * Create a new journal entry.
     */
    @Transactional
    public JournalEntry createEntry(JournalEntry journalEntry,
                                    String userEmail) {

        User user = userService.findByUserEmail(userEmail);

        journalEntry.setDate(LocalDateTime.now());
        // ⭐ This line is missing
        journalEntry.setUser(user);
        JournalEntry savedEntry =
                journalEntryRepository.save(journalEntry);

        user.getJournalEntries().add(savedEntry);

        return savedEntry;
    }

    /**
     * Update an existing journal entry.
     */
    @Transactional
    public JournalEntry updateEntry(Long id,
                                    JournalEntry updatedEntry,
                                    String userEmail) {

        JournalEntry existingEntry =
                getEntryById(id, userEmail);

        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setContent(updatedEntry.getContent());

        return journalEntryRepository.save(existingEntry);
    }

    /**
     * Delete a journal entry.
     */
    @Transactional
    public void deleteEntry(Long id,
                            String userEmail) {

        User user = userService.findByUserEmail(userEmail);

        JournalEntry journalEntry =
                getEntryById(id, userEmail);

        user.getJournalEntries()
                .removeIf(entry -> entry.getId().equals(id));

        userService.saveUser(user);

        journalEntryRepository.delete(journalEntry);
    }


    public List<JournalEntry> searchByDate(
            LocalDate date,
            String userEmail) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        return journalEntryRepository
                .findByUserUserEmailAndDateBetween(
                        userEmail,
                        start,
                        end
                );
    }

    public List<JournalEntry> getByMood(Sentiment mood, String userEmail) {
        return journalEntryRepository.findByUserUserEmailAndMood(userEmail, mood);
    }














}