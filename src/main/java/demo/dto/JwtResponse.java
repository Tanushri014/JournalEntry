package demo.dto;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private final String type = "Bearer";

    public JwtResponse(String token) {
        this.token = token;
    }
}