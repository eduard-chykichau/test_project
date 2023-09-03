package com.example.task.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "email")
public class Email {

    @Id
    @SequenceGenerator(name = "email_seq",
            sequenceName = "email_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_seq")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @jakarta.validation.constraints.Email
    private String email;

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(name = "user_data_id")
    private UserData userData;
}
