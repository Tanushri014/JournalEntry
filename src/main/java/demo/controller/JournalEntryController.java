package demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JournalEntryController {

    @GetMapping("/check")
    public String  healthCheck(){
        return "working";

    }

}
