package demo.controller;

import demo.dto.QuoteResponse;
import demo.entity.JournalEntry;
import demo.service.ExternalApiService;
import demo.service.JournalEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
@Tag(name="Journal Entries",description = "Journal CRUD Operationss")
@SecurityRequirement(name = "Bearer Authentication")
public class JournalEntryController {
private final ExternalApiService externalApiService;
    private final JournalEntryService journalEntryService;

    @Operation(summary = "Get all journal Entries of user")
    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntries(
            Authentication authentication) {

        return ResponseEntity.ok(
                journalEntryService.getAllEntries(authentication.getName())
        );
    }
    @Operation(summary = "Get single journal Entry of user")
    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getEntry(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                journalEntryService.getEntryById(id, authentication.getName())
        );
    }

    @Operation(summary = "Create a Journal Entry")
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(
            @Valid @RequestBody JournalEntry journalEntry,
            Authentication authentication) {

        JournalEntry saved =
                journalEntryService.createEntry(
                        journalEntry,
                        authentication.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }
    @Operation(summary = "Update Journal Entry")
    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateEntry(
            @PathVariable Long id,
            @Valid @RequestBody JournalEntry journalEntry,
            Authentication authentication) {

        JournalEntry updated =
                journalEntryService.updateEntry(
                        id,
                        journalEntry,
                        authentication.getName());

        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Delete Journal Entry ")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntry(
            @PathVariable Long id,
            Authentication authentication) {

        journalEntryService.deleteEntry(
                id,
                authentication.getName());

        return ResponseEntity.ok("Journal entry deleted successfully.");
    }
    @Operation(summary = "Get Quote ")
    @GetMapping("/quote")
    public ResponseEntity<QuoteResponse> getQuote(){

        return ResponseEntity.ok(externalApiService.generateQuote());
    }

}