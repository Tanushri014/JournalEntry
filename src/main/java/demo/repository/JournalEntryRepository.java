package demo.repository;

import demo.entity.JournalEntry;
import demo.enums.Sentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserUserEmail(String userEmail);

    List<JournalEntry> findByUserUserEmailAndDateBetween(
            String userEmail,
            LocalDateTime start,
            LocalDateTime end);

    List<JournalEntry> findByUserUserEmailAndMood(
            String userEmail,
            Sentiment mood);
    Optional<JournalEntry> findByIdAndUserUserEmail(Long id, String userEmail);
}