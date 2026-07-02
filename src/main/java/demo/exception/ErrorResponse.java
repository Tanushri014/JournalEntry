package demo.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {

    private LocalDateTime localDateTime;
    private String status;
    private String error;
}
