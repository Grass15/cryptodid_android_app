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

	public Claim getClaim(String claimIssuerName, String claimType, String claimTitle, String claimContent, String attributeName) throws ParseException {
		Claim claim = new Claim(claimTitle, claimType, claimIssuerName, claimContent, attributeName);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Calendar cal = Calendar.getInstance();
		claim.setIssuingDate(new Date());
		cal.setTime(claim.getIssuingDate());
		cal.add(Calendar.DAY_OF_MONTH, 30);
		claim.setExpirationDate(sdf.parse(sdf.format(cal.getTime())));
		return claim;
	}
}
