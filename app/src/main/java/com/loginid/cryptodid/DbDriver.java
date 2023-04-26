package com.loginid.cryptodid;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DbDriver extends SQLiteOpenHelper {

    public static final String name = "cryptodid.db";

    public DbDriver(Context context) {
        super(context, name, null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table users(user_id TEXT primary key,username TEXT unique, password TEXT, firstname TEXT, lastname TEXT, phone TEXT, address TEXT)");
        sqLiteDatabase.execSQL("insert into users(user_id ,username , password , firstname , lastname , phone , address ) " +
                "values(0 , 'demo' , 'demo' , '' , '' , '' , '' )");
        sqLiteDatabase.execSQL("create table claims(claim_id INTEGER PRIMARY KEY, title TEXT, type TEXT, issuerName TEXT, content TEXT,  issuingDate TEXT, expirationDate TEXT, hash INTEGER, ciphers BLOB, PK BLOB, fhe BLOB )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists users");
        sqLiteDatabase.execSQL("drop table if exists claims");
        onCreate(sqLiteDatabase);
    }

    public Boolean register(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("user_id", UUID.randomUUID().toString());
        content.put("username",username);
        content.put("password",password);

        long result = db.insert("users",null,content);
        db.close();
        return result != -1;
    }

    public User getUser(){
        String selectQuery = "SELECT  * FROM users";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user = null;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            user = new User(cursor.getString(1), cursor.getString(2),  cursor.getString(0));
            user.firstname = cursor.getString(3);
            user.lastname = cursor.getString(4);
            user.phone = cursor.getString(5);
            user.address = cursor.getString(6);
        }
        cursor.close();
        return user;
    }


    public long insertClaim(Claim claim) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("title", claim.getTitle());
        content.put("type", claim.getType());
        content.put("issuerName", claim.getIssuerName());
        content.put("content", claim.getContent());
        content.put("issuingDate", claim.getIssuingDate());
        content.put("expirationDate", claim.getExpirationDate());
        content.put("hash", claim.getHash());
        content.put("ciphers", claim.ciphersToByteArray(claim.getCiphers()));
        content.put("PK", claim.PKToByteArray(claim.getPK()));
        content.put("fhe", claim.fheToByteArray(claim.getFhe()));
        long result = db.insert("claims",null,content);
        return result;
    }

    public List<Claim> getClaims() throws IOException, ParseException, ClassNotFoundException {
        List<Claim> claimList = new ArrayList<Claim>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.CANADA);
        // Select All Query
        String selectQuery = "SELECT  * FROM claims";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Claim claim = new Claim(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4) );
                claim.setIssuingDate(cursor.getString(5));
                claim.setExpirationDate(cursor.getString(6));
                claim.setHash(cursor.getInt(7));
                claim.setId(cursor.getInt(0));
                claim.setCiphers(claim.byteArrayToCiphers(cursor.getBlob(8)));
                claim.setPK(claim.byteArrayToPK(cursor.getBlob(9)));
                claim.setFhe(claim.byteArrayToFhe(cursor.getBlob(10)));
                claimList.add(claim);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return claimList;
    }

    public Claim getClaimFromACertainType(String type) throws ParseException, IOException, ClassNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.CANADA);
        Cursor cursor = db.query("claims", new String[] { "claim_id", "title",
                        "type", "issuerName", "content", "issuingDate", "expirationDate", "hash", "ciphers", "PK", "fhe" },  "type=?",
                new String[] { type }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            Claim claim = new Claim(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4) );
            claim.setIssuingDate(cursor.getString(5));
            claim.setExpirationDate(cursor.getString(6));
            claim.setHash(cursor.getInt(7));
            claim.setId(cursor.getInt(0));
            claim.setCiphers(claim.byteArrayToCiphers(cursor.getBlob(8)));
            claim.setPK(claim.byteArrayToPK(cursor.getBlob(9)));
            claim.setFhe(claim.byteArrayToFhe(cursor.getBlob(10)));
            cursor.close();
            return claim;
        }
        return null;
    }

    public List<Claim> getClaimsFromACertainType(String type) throws ParseException, IOException, ClassNotFoundException {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Claim> claimList = new ArrayList<Claim>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.CANADA);
        Cursor cursor = db.query(
                "claims",
                new String[] { "claim_id", "title",
                        "type", "issuerName", "content", "issuingDate", "expirationDate", "hash", "ciphers", "PK", "fhe" },
                "type=?",
                new String[] { type },
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                Claim claim = new Claim(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4) );
                claim.setIssuingDate(cursor.getString(5));
                claim.setExpirationDate(cursor.getString(6));
                claim.setHash(cursor.getInt(7));
                claim.setId(cursor.getInt(0));
                claim.setCiphers(claim.byteArrayToCiphers(cursor.getBlob(8)));
                claim.setPK(claim.byteArrayToPK(cursor.getBlob(9)));
                claim.setFhe(claim.byteArrayToFhe(cursor.getBlob(10)));
                claimList.add(claim);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return claimList;
    }

    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curr = db.rawQuery("select * from users where username = ?",new String[] {username});
        if(curr.getCount() > 0){
            curr.close();
            return true;
        }
        curr.close();
        return false;
    }

    public void dropClaim(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM claims WHERE claim_id = ?",new String[] {String.valueOf(id)});
    }

    public Boolean checkCred(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor curr = db.rawQuery("select * from users where username = ?",new String[] {"user_good"});
        Cursor curr = db.rawQuery("select * from users where username = ? and password = ?",new String[] {username,password});
        if(curr.getCount() > 0){
            curr.close();
            return true;

        }else{
            curr.close();
            return false;
        }


    }

    public void updateUserInformation(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(user.id);
        System.out.println(user.firstname);
        db.execSQL("UPDATE users " +
                "SET firstname = ?  ," +
                "lastname = ?  ," +
                "address = ?  ," +
                "phone = ?  " +
                "WHERE user_id LIKE ?",
                new String[] {user.firstname, user.lastname, user.address, user.phone, user.id});

    }
}
