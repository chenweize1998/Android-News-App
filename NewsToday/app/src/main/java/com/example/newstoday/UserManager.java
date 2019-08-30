package com.example.newstoday;

import android.content.Context;
import android.os.AsyncTask;


import java.util.concurrent.ExecutionException;

public  class UserManager{

    private static UserManager INSTANCE;
    private UserDao userDao;
    private UserDB userDB;

    private UserManager(Context context){
        userDB = UserDB.getUserDB(context, "user");
        userDao = userDB.userDao();
    }

    public static UserManager getUserManager(Context context){
        if(INSTANCE == null){
            INSTANCE = new UserManager(context);
        }
        return INSTANCE;
    }

    public User[] getAllUsers(){
        try{
            GetAllUsersTask getAllUsersTask = new GetAllUsersTask();
            return getAllUsersTask.execute(0).get();

        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllUsersTask extends AsyncTask<Integer, Void, User[]> {
        @Override
        protected User[] doInBackground(Integer... params){
            return userDao.getAllUsers();
        }
    }

    public void addInUser(User... user){
        AddInUserTask addInUserTask = new AddInUserTask();
        addInUserTask.execute(user);
    }

    private class AddInUserTask extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User...user){
            userDao.insert(user);
            return null;
        }
    }

    public void deleteOneUser(User... user){
        DeleteOneUserTask deleteOneUserTask = new DeleteOneUserTask();
        deleteOneUserTask.execute(user);
    }

    private class DeleteOneUserTask extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User...user){
            userDao.delete(user);
            return null;
        }
    }

    public void deleteAllUsers(){
        DeleteAllUsersTask deleteAllUsersTask = new DeleteAllUsersTask();
        deleteAllUsersTask.execute(0);
    }

    private class DeleteAllUsersTask extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... params){
            userDao.clear();
            return null;
        }
    }

    public String getPassword(String...email){
        try{
            GetPasswordTask getPasswordTask = new GetPasswordTask();
            return getPasswordTask.execute(email).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetPasswordTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String...email){
            return userDao.getPassword(email);
        }
    }

}



