package demo.service;


import demo.dto.QuoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.hibernate.query.results.Builders.fetch;

@Service
@RequiredArgsConstructor
public class QuoteService {

private final RestTemplate restTemplate;

    private static final String API="https://dummyjson.com/quotes/random";
    public QuoteResponse  generateQuote(){
       QuoteResponse response=restTemplate.getForObject(API, QuoteResponse.class);
       return  response;

    }

}
