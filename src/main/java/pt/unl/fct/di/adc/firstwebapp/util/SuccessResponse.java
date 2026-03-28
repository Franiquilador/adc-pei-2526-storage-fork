package pt.unl.fct.di.adc.firstwebapp.util;

// response format used by commands such as deleteaccount, modaccount, changeuserrole, changeuserpwd and logout endpoints
public class SuccessResponse {

    public String status;

    public Data data;

    public static class Data {
        public String message;
    }

    public SuccessResponse(String message) {
        this.status = "success";
        this.data = new SuccessResponse.Data();
        this.data.message = message;
    }

}
