package com.librarysystem;

import java.io.Serializable;

/**
 * Represents a user in the library system.
 * For simplicity, this class primarily serves to identify who borrowed a book.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    /**
     * Constructs a new User instance.
     * @param id The unique ID of the user.
     * @param name The name of the user.
     */
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the ID of the user.
     * @return The user ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     * @param id The new user ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the user.
     * @return The user name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name The new user name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the User object.
     * @return A string containing user details.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}
