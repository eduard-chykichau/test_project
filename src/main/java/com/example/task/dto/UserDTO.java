package com.example.task.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserDTO {
    private Long id;
    @NotBlank(message = "firstName is mandatory")
    private String firstName;
    @NotBlank(message = "firstName is mandatory")
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private List<EmailDTO> emails;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private List<PhoneDTO> phones;
}
