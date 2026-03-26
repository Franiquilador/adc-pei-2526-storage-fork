package pt.unl.fct.di.adc.firstwebapp.util;

// response for the /createaccount endpoint REST operation Op1
public class LoginResponse {

    public String status;

    public Data data;

    public static class Data {
        public AuthToken token;

        public Data() {}
    }

    public LoginResponse(String username, AuthToken token) {
        this.status = "success";
        this.data = new Data();
        this.data.token = token;
    }

}
