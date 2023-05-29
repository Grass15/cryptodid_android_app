package com.loginid.cryptodid.protocols;

import com.google.gson.Gson;
import com.loginid.cryptodid.R;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.presentation.MainActivity;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Issuer implements Serializable {
	X509Certificate x509certificate;
	byte[] certificateBytes;
	PrivateKey privateKey;
	private Gson gson = new Gson();
	public Issuer() throws CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
		Security.addProvider(new BouncyCastleProvider());
		KeyStore keystore = KeyStore.getInstance("BKS");

		InputStream inputStream = MainActivity.getActivityContext().getResources().openRawResource(R.raw.keystore);
		keystore.load(inputStream, "loginid".toCharArray());
		// Get the private key and certificate from the keystore
		String alias = "myalias";
		String keyPass = "loginid";
		privateKey = (PrivateKey) keystore.getKey(alias, keyPass.toCharArray());
		x509certificate = (X509Certificate) keystore.getCertificate(alias);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(x509certificate);
		certificateBytes = baos.toByteArray();
	}
	private int attribute;

	private final String filesFolderPath = String.valueOf(MainActivity.getFilesFolder());

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	private byte[] getEncryptedAttribute(String type) throws IOException {
		File file = new File(filesFolderPath+"/"+type+"Cloud.data");
		return FileUtils.readFileToByteArray(file);
	}

	public Claim getClaim(String title, String claimIssuerName, String claimType, String content) throws Exception {
		byte[] encryptedAttribute = getEncryptedAttribute(claimType);
		byte[] signature = signVC(encryptedAttribute);

		Claim claim = new Claim(title, claimType, content, claimIssuerName, getEncryptedAttribute(claimType), signature);

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] revocationNonce = digest.digest(gson.toJson(claim).getBytes(StandardCharsets.UTF_8));
		claim.setRevocationNonce(revocationNonce);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Calendar cal = Calendar.getInstance();
		claim.setIssuingDate(new Date());
		cal.setTime(claim.getIssuingDate());
		cal.add(Calendar.DAY_OF_MONTH, 30);
		claim.setExpirationDate(sdf.parse(sdf.format(cal.getTime())));
		return claim;
	}

	private byte[] signVC(byte[] vcEncryptedData) throws Exception {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(vcEncryptedData);
		return signature.sign();
	}

	private String changeBlockchainState(String url, String json) throws IOException {
		MediaType JSON = MediaType.get("application/json");

		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, json);

		Request request = new Request.Builder()
				.method("POST", body)
				.url(url)
				.build();
		try (Response response = client.newCall(request).execute()) {
			assert response.body() != null;
			return response.body().string();
		}
	}
}
