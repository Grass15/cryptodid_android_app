package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;

public class Issuer implements Serializable {
	private int attribute;

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public Claim getClaim(String username, String password, MG_FHE fhe, String claimIssuerName, String claimType, String claimTitle, String claimContent) throws ParseException {
		Claim claim = new Claim(claimTitle, claimType, claimIssuerName, claimContent);
		MG_FHE.MG_Cipher[] claimCiphers = new MG_FHE.MG_Cipher[8];
		for (int i = 0; i < 8; i++) {
			claimCiphers[i] = fhe.encrypt(new BigInteger("" + ((attribute>>i) & 0x1), 10));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Calendar cal = Calendar.getInstance();
		claim.setCiphers(claimCiphers);
		cal.setTime(new Date());
		claim.setIssuingDate(sdf.format(cal.getTime()));
		cal.add(Calendar.DAY_OF_MONTH, 30);
		claim.setExpirationDate(sdf.format(cal.getTime()));
		claim.setPK(fhe.PK);
		claim.setHash(Arrays.hashCode(claim.getCiphers()) + claim.getIssuingDate().hashCode() + Arrays.hashCode(claim.getPK()));
		return claim;
	}
}
