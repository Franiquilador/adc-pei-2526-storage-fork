package pt.unl.fct.di.adc.firstwebapp.util;

public class LoginData {

	public RegisterData.Input input;

	public static class Input {
		public String username;
		public String password;
	}
	public LoginData() { }
	
	public LoginData(String username, String password) {
		//this.username = username;
		//this.password = password;
	}
	
}
