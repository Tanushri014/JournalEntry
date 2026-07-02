package demo.service;


import demo.dto.QuoteResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalApiService {

private final RestTemplate restTemplate;
//external api for quote
    @Value("${external.api.quote-url}")
    private  String API;
    public QuoteResponse  generateQuote(){
       QuoteResponse response=restTemplate.getForObject(API, QuoteResponse.class);
       return  response;

    }


  //external api for whether




}
