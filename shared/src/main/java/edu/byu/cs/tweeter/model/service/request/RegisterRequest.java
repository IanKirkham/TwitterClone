package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a register request.
 */
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private byte[] imageBytes;

    /**
     * Creates an instance.
     *
     * @param firstName  the first name of the user to be registered.
     * @param lastName   the last name of the user to be registered.
     * @param username   the username of the user to be registered.
     * @param password   the password of the user to be registered.
     * @param imageBytes the image of the user to be registered represented as a byte array.
     */
    public RegisterRequest(String firstName, String lastName, String username, String password, byte[] imageBytes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.imageBytes = imageBytes;
    }

    public RegisterRequest() {}

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
