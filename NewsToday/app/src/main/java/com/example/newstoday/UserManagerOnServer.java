package com.example.newstoday;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.ArraySet;

import com.example.newstoday.Activity.Table;

import org.json.JSONException;
import org.json.JSONObject;


import dmax.dialog.SpotsDialog;

public class UserManagerOnServer {

    private ServerHttpResponse serverHttpResponse;
    private UserManager userManager;
    private AsyncServerNews asyncServerNews;
    private static UserManagerOnServer INSTANCE = null;

    private UserManagerOnServer(Context context){
        serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
        userManager = UserManager.getUserManager(context);
        asyncServerNews = AsyncServerNews.getAsyncServerNews(context);
    }

    public static UserManagerOnServer getUserManagerOnServer(Context context){
        if(INSTANCE == null){
            INSTANCE = new UserManagerOnServer(context);
        }
        return INSTANCE;
    }


    public boolean userSignUp(String email, String name, String password){
        String data = "email="+email+"&name="+name+"&password="+password;
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/signUp/", data);
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        User user = new User(email, name, password, null, "");
        userManager.addInUser(user);
        return true;
    }

    public boolean userSignIn(String email, String name ,String password){
        StringBuilder data = new StringBuilder();
        data.append("email=");
        data.append(email);
        data.append("&name=");
        data.append(name);
        data.append("&password=");
        data.append(password);
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/signIn/", data.toString());
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        try {
            JSONObject json = new JSONObject(res);
            User user = new User(email, name, password, null, "");
            String followig = json.getString("followig");
            String avatar = json.getString("avatar");

            if(followig.equals("null")){
            }else{
                ArraySet<String> as = new ArraySet<>();
                String[] followigs = followig.split(",");
                for(String _followig:followigs){
                    as.add(_followig);
                }
                user.setFollowig(as);
            }
            user.setAvatar(avatar);
            userManager.addInUser(user);
            return true;
        }catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean userSignOut(final Activity activity){
        String data = "user="+ Table.header.getActiveProfile().getEmail().toString();
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/signOut/", data);
        final AlertDialog spotsDialog = new SpotsDialog.Builder()
                .setContext(activity)
                .setCancelable(false)
                .setTheme(R.style.Uploading)
                .build();
        spotsDialog.show();
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                asyncServerNews.asyncDataToServer();
                asyncServerNews.asyncUserToServer(userManager.getUserByEmail(Table.header.getActiveProfile().getEmail().toString())[0]);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Table.header.removeProfileByIdentifier(Table.header.getActiveProfile().getIdentifier());
                        spotsDialog.dismiss();
                    }
                });
            }
        }).start();
        return true;

    }

}
