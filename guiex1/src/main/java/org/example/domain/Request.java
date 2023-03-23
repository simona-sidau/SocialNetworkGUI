package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Request<ID>extends Entity<ID>{

    UUID user1;

    UUID user2;

    LocalDateTime date;

    boolean status;


    //CONSTRUCTORS

    public Request(){}

    public Request(UUID user1, UUID user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = false;
        this.date =LocalDateTime.now();

    }


    // SETTERS AND GETTERS

    public UUID getUser1() {
        return user1;
    }

    public void setUser1(UUID user1) {
        this.user1 = user1;
    }

    public UUID getUser2() {
        return user2;
    }

    public void setUser2(UUID user2) {
        this.user2 = user2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    //EQUALS AND HASHCODE METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request<?> request = (Request<?>) o;
        return status == request.status && Objects.equals(user1, request.user1) && Objects.equals(user2, request.user2) && Objects.equals(date, request.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2, date, status);
    }

    // TO STRING METHOD

    @Override
    public String toString() {
        return "Request{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
