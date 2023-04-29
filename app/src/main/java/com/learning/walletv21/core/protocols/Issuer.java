package com.learning.walletv21.core.protocols;


import com.learning.walletv21.core.protocols.javamodels.Claim;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
		claim.setIssuingDate(new Date());
		cal.setTime(claim.getIssuingDate());
		cal.add(Calendar.DAY_OF_MONTH, 30);
		claim.setExpirationDate(sdf.parse(sdf.format(cal.getTime())));
		claim.setPK(fhe.PK);
		claim.setHash(Arrays.hashCode(claim.getCiphers()) + claim.getIssuingDate().hashCode() + Arrays.hashCode(claim.getPK()));
		return claim;
	}
}
