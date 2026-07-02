package demo.serviceTests;

import demo.entity.JournalEntry;
import demo.entity.User;
import demo.exception.JournalEntryNotFoundException;
import demo.repository.JournalEntryRepository;
import demo.service.JournalEntryService;
import demo.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class JournalEntryServiceTests {

    @InjectMocks
    private JournalEntryService journalEntryService;
    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @Test
    void getAllJournalEntries_Check(){
        //assert
        JournalEntry j1=new JournalEntry();
        JournalEntry j2=new JournalEntry();
        List<JournalEntry> entries=List.of(j1,j2);

User user=new User();
user.setUserName("ab");
user.setJournalEntries(entries);


when(userService.findByUserName("ab")).thenReturn(user);

        //act
        List<JournalEntry> result=journalEntryService.getAllEntries("ab");
        //check
assertEquals(entries,result);
        //verify
        verify(userService).findByUserName("ab");

    }



@Test
    void getEntryById_shouldReturnEntryWithiD(){

        User user=new User();
        user.setId(1L);
        user.setUserName("ab");

        JournalEntry j1=new JournalEntry();
        j1.setId(1L);
    JournalEntry j2=new JournalEntry();
    j2.setId(2L);

    List<JournalEntry> entries=List.of(j1,j2);
    user.setJournalEntries(entries);


    when(userService.findByUserName("ab")).thenReturn(user);
    //act
    JournalEntry result=journalEntryService.getEntryById(1L,"ab");

    assertEquals(j1,result);

    verify(userService).findByUserName("ab");

}

    @Test
    void getEntryById_FailedToReturnEntryWithiD(){

        User user=new User();
        user.setUserName("ab");

        JournalEntry j1=new JournalEntry();
        j1.setId(1L);


        List<JournalEntry> entries=List.of(j1);
        user.setJournalEntries(entries);


        when(userService.findByUserName("ab")).thenReturn(user);

        assertThrows(

                JournalEntryNotFoundException.class,
                ()->journalEntryService.getEntryById(2L,"ab")
        );

        verify(userService).findByUserName("ab");

    }

    @Test
    void deleteEntry_shouldDeleteJournalEntry() {

        // Arrange
        User user = new User();
        user.setUserName("ab");

        JournalEntry entry = new JournalEntry();
        entry.setId(1L);

        user.setJournalEntries(new ArrayList<>(List.of(entry)));

        when(userService.findByUserName("ab")).thenReturn(user);

        // Act
        journalEntryService.deleteEntry(1L, "ab");

        // Verify

        verify(userService).saveUser(user);
        verify(journalEntryRepository).delete(entry);

        // Assert
        assertTrue(user.getJournalEntries().isEmpty());
    }

    @Test
    void createEntry_shouldCreateJournalEntry() {

        // Arrange
        User user = new User();
        user.setUserName("ab");
        user.setJournalEntries(new ArrayList<>());

        JournalEntry entry = new JournalEntry();
        entry.setTitle("My Title");
        entry.setContent("My Content");

        when(userService.findByUserName("ab")).thenReturn(user);
        when(journalEntryRepository.save(entry)).thenReturn(entry);

        // Act
        JournalEntry result = journalEntryService.createEntry(entry, "ab");

        // Assert
        assertEquals(entry, result);
        assertEquals(user, entry.getUser());
        assertNotNull(entry.getDate());
        assertTrue(user.getJournalEntries().contains(entry));

        // Verify
        verify(userService).findByUserName("ab");
        verify(journalEntryRepository).save(entry);
    }



    @Test
    void updateEntry_shouldUpdateEntry() {

        User user = new User();
        user.setUserName("ab");

        JournalEntry existing = new JournalEntry();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setContent("Old Content");

        user.setJournalEntries(new ArrayList<>(List.of(existing)));

        JournalEntry updated = new JournalEntry();
        updated.setTitle("New");
        updated.setContent("New Content");

        when(userService.findByUserName("ab"))
                .thenReturn(user);

        when(journalEntryRepository.save(existing))
                .thenReturn(existing);

        JournalEntry result =
                journalEntryService.updateEntry(1L, updated, "ab");

        assertEquals("New", result.getTitle());
        assertEquals("New Content", result.getContent());

        verify(journalEntryRepository).save(existing);
    }
}
