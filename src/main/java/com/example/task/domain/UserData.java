package com.example.task.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_data")
public class UserData {

    @Id
    @SequenceGenerator(name = "user_sequence",
            sequenceName = "user_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(
            mappedBy = "userData",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Email> emails;

    @OneToMany(
            mappedBy = "userData",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Phone> phones;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserData userData = (UserData) o;

        return id.equals(userData.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void addEmail(Email newEmail) {
        if (Objects.isNull(emails)) {
            emails = new ArrayList<>();
        }
        emails.add(newEmail);
        newEmail.setUserData(this);
    }

    public void addPhone(Phone newPhone) {
        if (Objects.isNull(phones)) {
            phones = new ArrayList<>();
        }
        phones.add(newPhone);
        newPhone.setUserData(this);
    }
}
