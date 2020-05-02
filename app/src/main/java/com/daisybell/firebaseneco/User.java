package com.daisybell.firebaseneco;

public class User {
    public String id, name, surname, email;

    public User() {
    }
    // Конструктор для вызова и заполнения нужными нам данными
    public User(String id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
