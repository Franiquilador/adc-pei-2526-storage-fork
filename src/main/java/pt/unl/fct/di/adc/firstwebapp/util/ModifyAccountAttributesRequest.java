package pt.unl.fct.di.adc.firstwebapp.util;

public class ModifyAccountAttributesRequest {
    public Input input;

    public AuthToken token;

    public static class Input {
        public String username;

        public Attributes attributes;

        public static class Attributes {
            public String phone;
            public String address;
        }
    }
}
