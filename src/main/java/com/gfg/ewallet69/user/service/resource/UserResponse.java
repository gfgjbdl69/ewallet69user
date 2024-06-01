package com.gfg.ewallet69.user.service.resource;

import com.gfg.ewallet69.user.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String email;

    public UserResponse(User user){
        this.id=user.getId().toString();
        this.name=user.getName();
        this.email=user.getEmail();
    }

}
