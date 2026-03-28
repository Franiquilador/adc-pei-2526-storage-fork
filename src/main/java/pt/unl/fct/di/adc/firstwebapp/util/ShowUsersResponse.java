package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.ArrayList;
import java.util.List;

public class ShowUsersResponse {

    public String status;

    public Data data;

    public static class Data {
        public List<UserResponse> users;
    }

    public static class UserResponse {

        public String username;
        public String role;

        public UserResponse(String username, String role) {
            this.username = username;
            this.role = role;
        }
    }

    public ShowUsersResponse(List<UserResponse> users) {
        this.status = "success";
        data = new Data();
        data.users = users;
    }
}
