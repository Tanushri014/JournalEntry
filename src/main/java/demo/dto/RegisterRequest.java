package demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    @Email
    @NotBlank(message = "Email is required")
    private String userEmail;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(min = 8)
    private String password;
}