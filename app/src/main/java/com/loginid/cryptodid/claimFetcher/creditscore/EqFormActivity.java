package com.loginid.cryptodid.claimFetcher.creditscore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.loginid.cryptodid.R;
import com.loginid.cryptodid.claimFetcher.creditscore.requests.ApiServiceEquifax;
import com.loginid.cryptodid.claimFetcher.creditscore.tokenrequest.AccessTokenRequest;
import com.loginid.cryptodid.claimFetcher.creditscore.tokenrequest.AccessTokenResponse;
import com.loginid.cryptodid.claimFetcher.creditscore.tokenrequest.ApiService;
import com.loginid.cryptodid.claimFetcher.creditscore.tokenrequest.ApiServiceClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EqFormActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText city;
    EditText StreetName;
    EditText StreetNum;
    EditText PostalCode;
    EditText TaxId;
    Button btnGet;
    ProgressBar progressBar;

    private ApiServiceEquifax apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq_form);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        city = (EditText) findViewById(R.id.City);
        StreetName = (EditText) findViewById(R.id.StreetName);
        StreetNum = (EditText) findViewById(R.id.StreetNum);
        PostalCode = (EditText) findViewById(R.id.PostalCode);
        TaxId = (EditText) findViewById(R.id.TaxId);
        btnGet = (Button) findViewById(R.id.btnGet);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        firstName.setText("MICHAEL");
        lastName.setText("WZXKBTKH");
        city.setText("HILLIARD");
        StreetName.setText("HILLIARD STATION RD");
        StreetNum.setText("3711");
        PostalCode.setText("43026");
        TaxId.setText("666565662");

        btnGet.setOnClickListener(new View.OnClickListener() {

            String fullName = "";
            String score = "";
            String source = "";
            String uid = "";


            @Override
            public void onClick(View view) {

                String getfirstName = firstName.getText().toString().trim();
                String getlastName = lastName.getText().toString().trim();
                String getcity = city.getText().toString().trim();
                String getStreetName = StreetName.getText().toString().trim();
                String getStreetNum = StreetNum.getText().toString().trim();
                String getPostalCode = PostalCode.getText().toString().trim();
                String getTaxId = TaxId.getText().toString().trim();

                if (    getfirstName.isEmpty() ||
                        getlastName.isEmpty() ||
                        getcity.isEmpty() ||
                        getStreetName.isEmpty() ||
                        getStreetNum.isEmpty() ||
                        getPostalCode.isEmpty() ||
                        getTaxId.isEmpty()
                ) {
                    allFieldRequired();
                    return;
                }

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                StartPrograss();

                String postData = "{\n" +
                        "  \"PersonInfo\": {\n" +
                        "    \"PersonName\": {\n" +
                        "      \"FirstName\": \""+getfirstName+"\",\n" +
                        "      \"LastName\": \""+getlastName+"\",\n" +
                        "      \"MiddleName\": \"J\"\n" +
                        "    },\n" +
                        "    \"ContactInfo\": {\n" +
                        "      \"PostAddr\": {\n" +
                        "        \"StreetNum\": \""+getStreetNum+"\",\n" +
                        "        \"StreetName\": \""+getStreetName+"\",\n" +
                        "        \"City\": \""+getcity+"\",\n" +
                        "        \"StateProv\": \"PA\",\n" +
                        "        \"PostalCode\": \""+getPostalCode+"\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"TINInfo\": {\n" +
                        "      \"TINType\": \"1\",\n" +
                        "      \"TaxId\": \""+getTaxId+"\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

                //your bearer token
                String bearerToken = "";
                final String CLIENT_ID = "t8A6KtDPqeAaRnTIe3yEQn996UCy6ckA";
                final String CLIENT_SECRET = "bDC4D3DzGMMLBFkw";

                // Create the access token request object
                AccessTokenRequest request = new AccessTokenRequest(CLIENT_ID, CLIENT_SECRET, "client_credentials");

                // Call the API endpoint with Retrofit
                ApiService apiServiceToken = ApiServiceClient.getClient();
                Call<AccessTokenResponse> call = apiServiceToken.getAccessToken(request);

                try {
                    Response<AccessTokenResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        AccessTokenResponse accessTokenResponse = response.body();
                        bearerToken = accessTokenResponse.getAccess_token();
                    } else {
                        System.out.println("Error: " + response.code());
                        noGetToken();
                        return;
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                    noGetToken();
                    return;
                }

                // Create a Retrofit instance with the desired base URL and a Gson converter factory
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100,TimeUnit.SECONDS).build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://apitest.microbilt.com/").client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create an instance of your API interface using the Retrofit instance
                apiService = retrofit.create(ApiServiceEquifax.class);

                // Create a RequestBody object to hold the data you want to send in your POST request
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), postData);

                // Call your API interface method with the bearer token and the request body as parameters, and use `enqueue()` to execute the request asynchronously
                apiService.postData("Bearer " + bearerToken, requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                // Convert the response body to a String
                                String responseBodyString = response.body().string();

                                // Parse the response body String into a JSON object
                                JSONObject json = new JSONObject(responseBodyString);


                                uid = ""+json.getJSONObject("MsgRsHdr").getString("RqUID");
                                source = ""+json.getJSONArray("Subject").getJSONObject(0).getJSONObject("PersonInfo").getString("Source");
                                fullName = json.getJSONArray("Subject").getJSONObject(0).getJSONObject("PersonInfo").getJSONObject("PersonName").getString("FirstName")+" "+
                                        json.getJSONArray("Subject").getJSONObject(0).getJSONObject("PersonInfo").getJSONObject("PersonName").getString("LastName");
                                score = ""+json.getJSONArray("Subject").getJSONObject(0).getJSONArray("Score").getJSONObject(0).getLong("Value");
                                // Print the JSON object to the console
                                System.out.println("Response: " + json.toString());
                                StopPrograss();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Check(fullName,score,uid,source);

                            } catch (JSONException e) {
                                // Handle the JSON parsing error
                                e.printStackTrace();
                                String fullName = "";
                                String score = "";
                                String source = "";
                                String uid = "";
                                Check(fullName,score,uid,source);
                            } catch (IOException e) {
                                // Handle the IO error
                                e.printStackTrace();
                                String fullName = "";
                                String score = "";
                                String source = "";
                                String uid = "";
                                Check(fullName,score,uid,source);
                            }
                        } else {
                            System.out.println("ELSE -> "+response);
                            String fullName = "";
                            String score = "";
                            String source = "";
                            String uid = "";
                            Check(fullName,score,uid,source);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String fullName = "";
                        String score = "";
                        String source = "";
                        String uid = "";
                        Check(fullName,score,uid,source);
                        System.out.println("ERROR -> "+t);
                    }
                });

            }
        });
    }

    private void allFieldRequired() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please, all fields are required !")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void Check(String fullName,String score,String uid,String source) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (source != ""  && uid != ""  && fullName != ""  && score != "") {
            builder.setMessage("Full Name : "+fullName + "\nScore : "+score+"\nSource : "+source)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            Intent intent = getIntent();
            //With big numbers like 700 the encryption bug, so we divide by ten to be good.
            intent.putExtra("attribute", (Integer.parseInt(score)/10)+"");
            intent.putExtra("claimTitle", "Credit Claim");
            intent.putExtra("claimType", "Credit Score");
            intent.putExtra("issuerName", "Crypto DID: Expires on ");
            intent.putExtra("claimContent", "You can use this claim to attest your credit score");
            setResult(RESULT_OK, intent);
        }

        if ((fullName == ""  && score == "" && source == ""  && uid == "")){
            builder.setMessage("No Score found ! Please Retry !!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            showMainActivity();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void noGetToken(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Can't get Token Acces ! Please Retry !!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        showMainActivity();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void StartPrograss() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void StopPrograss() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showMainActivity(){
        Intent intent = new Intent(this, CreditScoreActivity.class);
        startActivity(intent);
        finish();
    }

}