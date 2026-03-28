package pt.unl.fct.di.adc.firstwebapp.util;

public class ChangeUserPasswordRequest {

    public Input input;

    public AuthToken token;

    public static class Input {
        public String username;
        public String oldPassword;
        public String newPassword;
    }
}
