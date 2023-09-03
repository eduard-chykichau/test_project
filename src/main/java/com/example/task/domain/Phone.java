package com.example.task.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "phone")
public class Phone {

    @Id
    @SequenceGenerator(name = "phone_seq",
            sequenceName = "phone_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_seq")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phone;

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(name = "user_data_id")
    private UserData userData;
}
