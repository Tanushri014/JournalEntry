package demo.entity;



import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
//currently pojo
public class JournalEntry {

    private Long id;
    private String title;
    private String content;

}
