package com.enova.web.api.Models.Requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
  @NotBlank(message = "Authentication Request : username NotBlank")
  @Size(min = 2, max = 32 , message = "Authentication Request : username must between 2 - 32")
  String username;
  @Size(min = 4, max = 16 , message = "Authentication Request: password must between 8 - 16")
  String password;
  @Email(message = "Authentication Request : email must be correct")
  String email;
}
