package com.example.newstoday;


import android.content.Context;

public class UserManagerOnServer {

    private ServerHttpResponse serverHttpResponse;
    private UserManager userManager;
    private static UserManagerOnServer INSTANCE = null;

    private UserManagerOnServer(Context context){
        serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
        userManager = UserManager.getUserManager(context);
    }

    public static UserManagerOnServer getUserManagerOnServer(Context context){
        if(INSTANCE == null){
            INSTANCE = new UserManagerOnServer(context);
        }
        return INSTANCE;
    }


    public boolean userSignUp(String email, String name, String password){
        String data = "email="+email+"&name="+name+"&password="+password;
        String res = serverHttpResponse.postResponse("http://192.168.0.165:8000/signUp/", data);
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        userManager.addInUser(new User(email, name, password));
        return true;
    }

    public boolean userSignIn(String email, String name ,String password){
        String data = "email="+email+"&name="+name+"&password="+password;
        String res = serverHttpResponse.postResponse("http://192.168.0.165:8000/signIn/", data);
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        return true;
    }

    public boolean userSignOut(){
        String res = serverHttpResponse.getResponse("http://192.168.0.165:8000/signOut/");
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        return true;
    }


}
