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
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/signUp/", data);
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        userManager.addInUser(new User(email, name, password, null, null));
        return true;
    }

    public boolean userSignIn(String email, String name ,String password){
        User[] users = userManager.getUserByEmail(email);
        if(users.length == 0){
            return false;
        }
        User user = users[0];
        String avatar;
        if(users[0].getAvatar()!=null){
            avatar = new String(ImageConverter.toTimestamp(users[0].getAvatar()));
        }else{
            avatar = "null";
        }
        StringBuilder data = new StringBuilder();
        data.append("email=");
        data.append(email);
        data.append("&name=");
        data.append(name);
        data.append("&password=");
        data.append(password);
        data.append("&oriFollowig=");
        if(users[0].getFollowig()!=null){
            data.append(SetConverter.toTimestamp(users[0].getFollowig()));
        }else{
            data.append("null");
        }
        data.append("&avatar=");
        data.append(avatar);
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/signIn/", data.toString());
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
        String res = serverHttpResponse.getResponse("http://166.111.5.239:8000/signOut/");
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
