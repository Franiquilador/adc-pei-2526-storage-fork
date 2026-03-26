package pt.unl.fct.di.adc.firstwebapp.util;

public class ErrorResponse {

    public String status;

    public String data;

    public ErrorResponse(String status, String errorMessage) {
        this.status = status;
        this.data = errorMessage;
    }

}
