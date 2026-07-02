package demo.dto;

import lombok.Data;
//what the external api is going to return
@Data
public class QuoteResponse {

    private int id;
    private String quote;
    private String author;
}