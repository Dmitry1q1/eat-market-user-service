package userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private String passwordConfirm;

    private String errorDescription;

}
