package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.List;

public class ShowAuthenticatedSessionsResponse {

    public String status;

    public Data data;

    public static class Data {
        public List<Session> sessions;
    }

    public static class Session {
        public String tokenId;
        public String username;
        public String role;
        public long expiresAt;

        public Session(String tokenId, String username, String role, long expiresAt) {
            this.tokenId = tokenId;
            this.username = username;
            this.role = role;
            this.expiresAt = expiresAt;
        }
    }

    public ShowAuthenticatedSessionsResponse(List<Session> sessions) {
        status = "success";
        data = new Data();
        data.sessions = sessions;
    }

}
