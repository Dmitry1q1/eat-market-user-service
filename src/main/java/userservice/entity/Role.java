package userservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }
}
