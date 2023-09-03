package com.example.task.repositories;

import com.example.task.domain.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Long> {
    @Query("select u from UserData u left join fetch u.emails where u.id=:id")
    Optional<UserData> findUserWithJoinFetchEmail(long id);

    @Query("select u from UserData u left join fetch u.phones where u.id=:id")
    Optional<UserData> findUserWithJoinFetchPhone(long id);
}
