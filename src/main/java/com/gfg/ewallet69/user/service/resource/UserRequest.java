package com.gfg.ewallet69.user.service.resource;

import com.gfg.ewallet69.user.domain.User;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "username cannot be blank")
    private String name;
    @NotBlank(message = "password cannot be blank")
    private String password;
    @Email(message = "email should be valid")
    private String email;


    public User toUser() {
        return User.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
    }

}
