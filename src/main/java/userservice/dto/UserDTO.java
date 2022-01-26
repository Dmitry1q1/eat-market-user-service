package userservice.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private String passwordConfirm;

    private String errorDescription;

    public UserDTO() {

    }
}
