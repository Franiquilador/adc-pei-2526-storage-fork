package pt.unl.fct.di.adc.firstwebapp.util;

// used by deleteaccount and showuserrole
public class AccountRequest {

    public Input input;

    public AuthToken token;

    public static class Input {
        public String username;
    }
}
