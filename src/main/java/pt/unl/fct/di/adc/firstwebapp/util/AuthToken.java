package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.UUID;

public class AuthToken {

	public static final long EXPIRATION_TIME = 900000; // 2h

	public String tokenId;
	public String username;
	public long issuedAt;
	public long expiresAt;
	
	public AuthToken() { }
	
	public AuthToken(String username) {
		this.tokenId = UUID.randomUUID().toString();
		this.username = username;
		this.issuedAt = System.currentTimeMillis();
		this.expiresAt = this.issuedAt + EXPIRATION_TIME;
	}
	
}
