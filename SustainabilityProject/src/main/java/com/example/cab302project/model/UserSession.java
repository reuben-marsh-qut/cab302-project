package com.example.cab302project.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Stores the currently logged-in user for the application.
 *
 * <p>This class uses a singleton so controllers share the same session.
 * Observers can listen for replacement or clearing of the current user.</p>
 */
public class UserSession {

    private static UserSession instance;

    private final ReadOnlyObjectWrapper<User> user =
            new ReadOnlyObjectWrapper<>();

    /**
     * Creates the shared session.
     *
     * <p>Private to prevent callers from creating separate sessions.</p>
     */
    private UserSession() {
    }

    /**
     * Returns the singleton instance of {@code UserSession}.
     *
     * @return the shared user session
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }

        return instance;
    }

    /**
     * Returns the user currently logged in.
     *
     * @return the current user, or {@code null} if no user is logged in
     */
    public User getUser() {
        return user.get();
    }

    /**
     * Sets the user associated with this session.
     *
     * <p>Replacing the user notifies observers. Controllers can use this
     * method to supply refreshed database values after an XP award.</p>
     *
     * @param user the user to store, or {@code null} to clear the session
     */
    public void setUser(User user) {
        this.user.set(user);
    }

    /**
     * Clears the user session and notifies observers when a user
     * was previously present.
     */
    public void clearUserSession() {
        user.set(null);
    }

    /**
     * Returns a read-only observable property for the current user.
     *
     * <p>The property reports replacement or clearing of the user.
     * Changing fields on the same {@code User} object does not itself
     * produce a property-change notification.</p>
     *
     * @return the read-only user property
     */
    public ReadOnlyObjectProperty<User> userProperty() {
        return user.getReadOnlyProperty();
    }
}