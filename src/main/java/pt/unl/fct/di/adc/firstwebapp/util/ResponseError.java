package pt.unl.fct.di.adc.firstwebapp.util;

public class ResponseError {

    public String status;

    public String data;

    public ResponseError(String status, String errorMessage) {
        this.status = status;
        this.data = errorMessage;
    }

}
