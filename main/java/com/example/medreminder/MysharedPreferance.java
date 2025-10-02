package com.example.medreminder;

import android.content.Context;
import android.content.SharedPreferences;

public class MysharedPreferance {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;
    private static MysharedPreferance mysharedPreferance=null;

    private MysharedPreferance(Context context)
    {
        sharedPreferences = context.getSharedPreferences("shared",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }


    public static synchronized MysharedPreferance getPreferences(Context context)
    {

        if(mysharedPreferance==null) {
            mysharedPreferance = new MysharedPreferance(context);
        }

        return mysharedPreferance;
    }

    public void setSession(String session)
    {
        editor.putString("login",session);
        editor.apply();
    }

    public String getSession()
    {
        return sharedPreferences.getString("login","none");
    }

    public void setlogin_type(String login_type)
    {
        editor.putString("login_type",login_type);
        editor.apply();
    }

    public String getlogin_type()
    {
        return sharedPreferences.getString("login_type","none");
    }


    public void setSplashScreen(int login_type)
    {
        editor.putInt("SplashScreen",login_type);
        editor.apply();
    }

    public int getSplashScreen()
    {
        return sharedPreferences.getInt("SplashScreen",1);
    }



    public void setName(String name)
    {
        editor.putString("name",name);
        editor.apply();
    }

    public String getName()
    {
        return sharedPreferences.getString("name","none");
    }







    public void setPhone(String phone)
    {
        editor.putString("phone",phone);
        editor.apply();
    }

    public String getPhone()
    {
        return sharedPreferences.getString("phone","none");
    }


    public  void setEmail(String email)
    {
        editor.putString("email",email);
        editor.apply();

    }

    public  String getemail(){return  sharedPreferences.getString("email","none");}


    public  void setSosContact(String sosContact)
    {
        editor.putString("sosContact",sosContact);
        editor.apply();

    }

    public  String getSosContact(){return  sharedPreferences.getString("sosContact","empty");}


    public  void setSosText(String sosText)
    {
        editor.putString("SosText",sosText);
        editor.apply();

    }

    public  String getSosText(){return  sharedPreferences.getString("SosText","I'm feeling bad, Need emergency help !");}


    public  void setUserID(String userID)
    {
        editor.putString("UserID",userID);
        editor.apply();

    }

    public  String getUserID(){return  sharedPreferences.getString("UserID","none");}



}
