package pt.unl.fct.di.adc.firstwebapp.util;

public class RegisterData {

	public Input input;

	public static class Input {
		public String username;
		public String password;
		public String confirmation; // password confirmation
		public String phone;
		public String address;
		public String role;
	}
	
	public RegisterData() {
		
	}
	
	private boolean nonEmptyOrBlankField(String field) {
		return field != null && !field.isBlank();
	}
	
	public boolean validRegistration() {
		return nonEmptyOrBlankField(input.username) &&
			   nonEmptyOrBlankField(input.password) &&
				nonEmptyOrBlankField(input.phone) &&
				nonEmptyOrBlankField(input.address) &&
				nonEmptyOrBlankField(input.role) &&
			   //nonEmptyOrBlankField(email) &&
			   //nonEmptyOrBlankField(name) &&
			   input.username.contains("@") &&
			   input.password.equals(input.confirmation);
	}
}