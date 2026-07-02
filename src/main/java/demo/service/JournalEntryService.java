package demo.service;

import demo.dto.QuoteResponse;
import demo.entity.JournalEntry;
import demo.entity.User;
import demo.exception.JournalEntryNotFoundException;
import demo.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<JournalEntry> getAllEntries(String username) {

        User user = userService.findByUserName(username);

        return user.getJournalEntries();
    }

    /**
     * Get a specific journal entry.
     * Only if it belongs to the logged-in user.
     */
    public JournalEntry getEntryById(Long id, String username) {

        User user = userService.findByUserName(username);

        return user.getJournalEntries()
                .stream()
                .filter(entry -> entry.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new JournalEntryNotFoundException("Journal entry not found."));
    }

    /**
     * Create a new journal entry.
     */
    @Transactional
    public JournalEntry createEntry(JournalEntry journalEntry,
                                    String username) {

        User user = userService.findByUserName(username);

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
                                    String username) {

        JournalEntry existingEntry =
                getEntryById(id, username);

        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setContent(updatedEntry.getContent());

        return journalEntryRepository.save(existingEntry);
    }

    /**
     * Delete a journal entry.
     */
    @Transactional
    public void deleteEntry(Long id,
                            String username) {

        User user = userService.findByUserName(username);

        JournalEntry journalEntry =
                getEntryById(id, username);

        user.getJournalEntries()
                .removeIf(entry -> entry.getId().equals(id));

        userService.saveUser(user);

        journalEntryRepository.delete(journalEntry);
    }



















}