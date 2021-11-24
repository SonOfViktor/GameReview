package com.fairycompany.reviewer.model.entity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;

public class User extends Entity{
    private long userId;
    private String login;
    private String firstName;
    private String secondName;
    private LocalDate birthday;
    private int phone;
    private BigDecimal balance;
    private String photo;
    private Role userRole;
    private Status userStatus;

    public enum Status {
        NOT_CONFIRMED, BANNED, ROOKIE, CRITIC, GRAND_MASTER
    }

    public enum Role {
        ADMIN, USER, GUEST
    }

    private User(long userId, String login, String firstName, String secondName, LocalDate birthday,
                 int phone, BigDecimal balance, String photo, Role userRole, Status userStatus) {
        this.userId = userId;
        this.login = login;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
        this.phone = phone;
        this.balance = balance;
        this.photo = photo;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public Status getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (phone != user.phone) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (secondName != null ? !secondName.equals(user.secondName) : user.secondName != null) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null) return false;
        if (balance != null ? !balance.equals(user.balance) : user.balance != null) return false;
        if (userRole != user.userRole) return false;
        return userStatus == user.userStatus;                   //todo add photo
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + phone;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        result = 31 * result + (userStatus != null ? userStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("User { ")
                .append("userId = ").append(userId)
                .append(", login = ").append(login)
                .append(", firstName = ").append(firstName)
                .append(", secondName = ").append(secondName)
                .append(", birthday = ").append(birthday)
                .append(", phone = ").append(phone)
                .append(", balance = ").append(balance)
                .append(", userRole = ").append(userRole)
                .append(", userStatus = ").append(userStatus)
                .append(" }");
        return builder.toString();
    }

    public static class UserBuilder {

        private long userId;
        private String login;
        private String firstName;
        private String secondName;
        private LocalDate birthday;
        private int phone;
        private BigDecimal balance;
        private String photo;
        private Role userRole;
        private Status userStatus;

        public UserBuilder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder setLogin(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public UserBuilder setBirthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public UserBuilder setPhone(int phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public UserBuilder setPhoto(String photo) {
            this.photo = photo;
            return this;
        }

        public UserBuilder setUserRole(Role userRole) {
            this.userRole = userRole;
            return this;
        }

        public UserBuilder setUserStatus(Status userStatus) {
            this.userStatus = userStatus;
            return this;
        }

        public User createUser() {
            return new User(userId, login, firstName, secondName, birthday, phone, balance, photo, userRole, userStatus);
        }
    }
}
