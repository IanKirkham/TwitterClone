package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a register request.
 */
public class RegisterRequest {

    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final byte[] imageBytes;

    /**
     * Creates an instance.
     *
     * @param firstName the first name of the user to be registered.
     * @param lastName the last name of the user to be registered.
     * @param username the username of the user to be registered.
     * @param password the password of the user to be registered.
     * @param imageBytes the image of the user to be registered represented as a byte array.
     */
    public RegisterRequest(String firstName, String lastName, String username, String password, byte[] imageBytes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.imageBytes = imageBytes;
    }

    /**
     * Returns the first name of the user to be registered by this request.
     *
     * @return the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user to be registered by this request.
     *
     * @return the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the username of the user to be registered by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user to be registered by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the byte array of the user to be registered by this request.
     *
     * @return the byte array.
     */
    public byte[] getImageBytes() {
        return imageBytes;
    }
}
