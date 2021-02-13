package edu.byu.cs.tweeter.view.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;
import edu.byu.cs.tweeter.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class RegisterFragment extends Fragment implements RegisterPresenter.View, RegisterTask.Observer {

    private static final String LOG_TAG = "RegisterFragment";

    private EditText firstNameField, lastNameField, usernameField, passwordField;
    private TextView takeProfilePicture;
    private Button registerButton;

    private RegisterPresenter presenter;
    private Toast registerToast;

    public RegisterFragment() {}
    public static RegisterFragment newInstance() { return new RegisterFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new RegisterPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // find text fields
        firstNameField = v.findViewById(R.id.firstNameField);
        lastNameField = v.findViewById(R.id.lastNameField);
        usernameField = v.findViewById(R.id.usernameField);
        passwordField = v.findViewById(R.id.passwordField);

        // register watchers
        firstNameField.addTextChangedListener(registerWatcher);
        lastNameField.addTextChangedListener(registerWatcher);
        usernameField.addTextChangedListener(registerWatcher);
        passwordField.addTextChangedListener(registerWatcher);

        // set up click events for getting a profile picture
        takeProfilePicture = v.findViewById(R.id.takeProfilePicture);
        takeProfilePicture.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: Change this to open up the camera and get a picture
                Toast.makeText(getActivity(), "Profile Picture Link Clicked!", Toast.LENGTH_LONG).show();
            }
        });

        // set up register button
        registerButton = v.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                registerToast = Toast.makeText(getContext(), "Attempting to register user", Toast.LENGTH_LONG);
                registerToast.show();

                RegisterRequest registerRequest = new RegisterRequest(firstNameField.getText().toString(),
                        lastNameField.getText().toString(), usernameField.getText().toString(), passwordField.getText().toString());
                RegisterTask registerTask = new RegisterTask(presenter, RegisterFragment.this);
                registerTask.execute(registerRequest);
            }
        });

        return v;
    }

    // TODO: incorporate checking that a profile picture was also taken (check a variable?)
    private final TextWatcher registerWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            registerButton.setEnabled(
                    firstNameField.getText().toString().length() != 0 &&
                            lastNameField.getText().toString().length() != 0 &&
                            usernameField.getText().toString().length() != 0 &&
                            passwordField.getText().toString().length() != 0);
        }
    };

    /**
     * The callback method that gets invoked for a successful registration. Displays the MainActivity.
     *
     * @param registerResponse the response from the register request.
     */
    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        Intent intent = new Intent(getActivity(), MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, registerResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, registerResponse.getAuthToken());

        registerToast.cancel();
        Toast.makeText(getActivity(), "Welcome " + registerResponse.getUser().getFirstName(), Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    /**
     * The callback method that gets invoked for an unsuccessful login. Displays a toast with a
     * message indicating why the login failed.
     *
     * @param registerResponse the response from the login request.
     */
    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        Toast.makeText(getActivity(), "Failed to register. " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * A callback indicating that an exception was thrown in an asynchronous method called on the
     * presenter.
     *
     * @param exception the exception.
     */
    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getActivity(), "Failed to register because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}