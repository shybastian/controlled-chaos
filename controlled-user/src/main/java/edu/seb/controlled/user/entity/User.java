package edu.seb.controlled.user.entity;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
//@Entity(name = "chaos_user")
public class User {
//    @Id
//    @SequenceGenerator(name = "chaos_user_id_seq", initialValue = 1, allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chaos_user_id_seq")
//    @Column(name = "id", nullable = false)
    private Long id;
    //@Column(name = "username", nullable = false)
    private String username;
    //@Column(name = "password", nullable = false)
    private String password;
    //@Column(name = "first_name", nullable = false)
    private String firstName;
    //@Column(name = "last_name", nullable = false)
    private String lastName;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
