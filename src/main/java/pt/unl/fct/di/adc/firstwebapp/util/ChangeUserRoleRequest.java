package pt.unl.fct.di.adc.firstwebapp.util;


public class ChangeUserRoleRequest {

    public Input input;

    public AuthToken token;

    public static class Input {
        public String username;
        public String newRole;
    }
}
