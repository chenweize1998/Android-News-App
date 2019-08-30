package com.example.newstoday;


public class UserManagerOnServer {

    private ServerHttpResponse serverHttpResponse;
    private static UserManagerOnServer INSTANCE = null;

    private UserManagerOnServer(){
        serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    }

    public static UserManagerOnServer getUserManagerOnServer(){
        if(INSTANCE == null){
            INSTANCE = new UserManagerOnServer();
        }
        return INSTANCE;
    }


    public boolean userSignUp(String email, String name, String password){
        String data = "email="+email+"&name="+name+"&password="+password;
        String res = serverHttpResponse.postResponse("http://183.172.218.1:8000/signUp/", data);
        if(res==null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        return true;
    }

    public boolean userSignIn(String email, String name ,String password){
        String data = "email="+email+"&name="+name+"&password="+password;
        String res = serverHttpResponse.postResponse("http://183.172.218.1:8000/signIn/", data);
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
        String res = serverHttpResponse.getResponse("http://183.172.218.1:8000/signOut/");
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
