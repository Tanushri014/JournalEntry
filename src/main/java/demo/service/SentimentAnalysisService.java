package demo.service;

import demo.entity.JournalEntry;
import demo.entity.User;
import demo.enums.Sentiment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class SentimentAnalysisService {



private  final UserService userService;
 private  final EmailService emailService;
 @Scheduled(cron = "0 0 21 * * SUN")//every week on sunday at 9 pm
 @Transactional
 public void generateWeeklyReport(){

        //this method will run for all the users we have to find all user
        List<User> users=userService.getAllUsers();

        //send the email to all users via a for loop

        for(User user:users){
            //get the report of individual user
            String body=calculateSentimentReport(user);
            //send mail
            emailService.sendEmail(user.getUserEmail(), "Sentiment analysis is ready",body);
        }
    }

//helper method to generate the sentiments
    private  String calculateSentimentReport(User user ){
        //we have a user
        //fetch all journal entries of user
        List<JournalEntry> allEntries=user.getJournalEntries();

        //now as we have all journalentries we need to filter only current weeks entries .
List<JournalEntry> currentWeekEntries=filterEntries(allEntries);

        //now on filterd entries we will be using the sentimentanalysis

        //extract the sentiment analysis
        Map<Sentiment,Long> sentimentCount=currentWeekEntries.stream().collect(Collectors.groupingBy(JournalEntry::getMood,Collectors.counting()));

        Long happy=sentimentCount.getOrDefault(Sentiment.HAPPY,0L);
        long sad = sentimentCount.getOrDefault(Sentiment.SAD, 0L);
        long neutral = sentimentCount.getOrDefault(Sentiment.NEUTRAL, 0L);
        long excited = sentimentCount.getOrDefault(Sentiment.EXCITED, 0L);
        long angry = sentimentCount.getOrDefault(Sentiment.ANGRY, 0L);



        //do the logic and store in report string
        String report="""
Hello %s, Here is your weekly mood summary.

😊 Happy    : %d days
😔 Sad      : %d days
😐 Neutral  : %d days
🎉 Excited  : %d days
😠 Angry    : %d days

Keep writing your journal!
""".formatted(
                user.getUserName(),
                happy,
                sad,
                neutral,
                excited,
                angry
        );
        return report;
    }


//helper method to filter the current weeks entries

    private List<JournalEntry> filterEntries(List<JournalEntry> journalEntries){

//find the time one week ago
        LocalDateTime oneWeekAgo =LocalDateTime.now().minusDays(7);

        //return the entries
        return journalEntries.stream().filter(entry->entry.getDate().isAfter(oneWeekAgo)).toList();



    }


}

/*
*   //we can count thesentiment by using collectors.groupingbY
     //CALCULATES THE FREQUENCY OF EACH SENTIMENT

* HAPPY-2
* SAD-3
* SO ITS A key value pair
* map is best choice for it
*

* so now we need to calculate their values as well ...with index
* getOrDefault return safely 0 if value is null .
*
*
*
*
*
* */