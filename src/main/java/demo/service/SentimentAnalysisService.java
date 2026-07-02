package demo.service;

import demo.entity.JournalEntry;
import demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SentimentAnalysisSevice {


private  final UserService userService;
    private  final EmailService emailService;
    @Scheduled(cron = "0 0 21 * * SUN")
    public void generateWeeklyReport(){

        //this method will run for all the users we have to find all user
        List<User> users=userService.getAllUsers();
        //send the email to all users

        for(User user:users){
            String body=calculateSentimentReport(user);
            emailService.sendEmail(user.getUserName(), "Sentiment analysis is ready",body);
        }
    }

//helper method to generate the sentiments
    private  String calculateSentimentReport(User user ){
        //we have a user
        //fetch all journal entries of user
        List<JournalEntry> allEntries=user.getJournalEntries();

        //now as we have all journalentries we need to filter only current weeks entries .

        //now on filterd entries we will be using the sentimentanalysis
        //extract the sentiment analysis

        //do the logic and store in report string
        String report="report is this ";
        return report;
    }
    





}
