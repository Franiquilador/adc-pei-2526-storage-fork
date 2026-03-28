package pt.unl.fct.di.adc.firstwebapp.util;

// response for the /createaccount endpoint REST operation Op1 and for showuserrole
public class UserResponse {

    public String status;

    public Data data;

    public static class Data {
        public String username;
        public String role;
    }

    public UserResponse(String username, String role) {
        this.status = "success";
        this.data = new Data();
        this.data.username = username;
        this.data.role = role;
    }

}
