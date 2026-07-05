package demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @NotNull(message = "OTP is required")
    private Integer otp;
}