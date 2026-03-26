package pt.unl.fct.di.adc.firstwebapp.util;

public class RegisterResponse {

    public String status;

    public Data data;

    public static class Data {
        public String username;
        public String role;
    }

    public RegisterResponse(String username, String role) {
        this.status = "success";
        this.data = new Data();
        this.data.username = username;
        this.data.role = role;
    }
}
