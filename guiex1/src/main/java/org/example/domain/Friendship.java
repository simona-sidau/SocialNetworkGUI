package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Friendship<ID extends UUID>extends Entity<ID>{

    ID user1;
    ID user2;
    LocalDateTime date;

    public Friendship(){}

    public Friendship(ID user1, ID user2) {
        this.user1 = user1;
        this.user2 = user2;

        date = LocalDateTime.now();
    }

    public ID getUser1() {
        return user1;
    }

    public void setUser1(ID user1) {
        this.user1 = user1;
    }

    public ID getUser2() {
        return user2;
    }

    public void setUser2(ID user2) {
        this.user2 = user2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship<?> that = (Friendship<?>) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2, date);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", date=" + date +
                '}';
    }
}
