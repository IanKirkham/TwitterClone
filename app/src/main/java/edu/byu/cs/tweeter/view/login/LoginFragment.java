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
import android.widget.Toast;

import java.util.Objects;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class LoginFragment extends Fragment implements LoginPresenter.View, LoginTask.Observer {

    private static final String LOG_TAG = "LoginFragment";

    private EditText usernameField, passwordField;
    private Button loginButton;

    private LoginPresenter presenter;
    private Toast loginInToast;

    public LoginFragment() {}
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // find text fields
        usernameField = v.findViewById(R.id.usernameField);
        passwordField = v.findViewById(R.id.passwordField);

        // register watchers
        usernameField.addTextChangedListener(loginWatcher);
        passwordField.addTextChangedListener(loginWatcher);

        // set up button
        loginButton = v.findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginInToast = Toast.makeText(getContext(), "Logging In", Toast.LENGTH_LONG);
                loginInToast.show();

                LoginRequest loginRequest = new LoginRequest(usernameField.getText().toString(), passwordField.getText().toString());
                LoginTask loginTask = new LoginTask(presenter, LoginFragment.this);
                loginTask.execute(loginRequest);
            }
        });

        return v;
    }

    private final TextWatcher loginWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            loginButton.setEnabled(usernameField.getText().toString().length() != 0 && passwordField.getText().toString().length() != 0);
        }
    };

    /**
     * The callback method that gets invoked for a successful login. Displays the MainActivity.
     *
     * @param loginResponse the response from the login request.
     */
    @Override
    public void loginSuccessful(LoginResponse loginResponse) {
        Intent intent = new Intent(getActivity(), MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, loginResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, loginResponse.getAuthToken());

        loginInToast.cancel();
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    /**
     * The callback method that gets invoked for an unsuccessful login. Displays a toast with a
     * message indicating why the login failed.
     *
     * @param loginResponse the response from the login request.
     */
    @Override
    public void loginUnsuccessful(LoginResponse loginResponse) {
        Toast.makeText(getActivity(), "Failed to login. " + loginResponse.getMessage(), Toast.LENGTH_LONG).show();
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
        Toast.makeText(getActivity(), "Failed to login because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}