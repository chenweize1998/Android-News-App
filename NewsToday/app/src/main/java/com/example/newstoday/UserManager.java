package com.example.newstoday;

import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class UserManager{

    private static UserManager INSTANCE;
    private UserDao userDao;
    private UserDB userDB;
    ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();

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
            return getAllUsersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,0).get();

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

    public User[] getUserByEmail(String... email){
        try{
            GetUserByEmailTask getUserByEmailTask = new GetUserByEmailTask();
            return getUserByEmailTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,email).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetUserByEmailTask extends AsyncTask<String, Void, User[]>{
        @Override
        protected User[] doInBackground(String... email){
            return userDao.getUserByEmail(email);
        }
    }

    /**
     * 本地有一个user数据库，sign up调用
     * @param user
     */
    public void addInUser(User... user){
        AddInUserTask addInUserTask = new AddInUserTask();
        addInUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,user);
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
        deleteOneUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,user);
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
        deleteAllUsersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,0);
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
            return getPasswordTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,email).get();
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

    public void addOneFollowigForUser(User user, String email){
        user.addFollowig(email);
        updateUser(user);
    }


    /**
     * 在每次更改了内存中的user之后，也就是换头像，或者修改关注，就必须update到数据库里面
     * */
    public void updateUser(User user){
        UpdateUserInDBTask updateUserInDBTask = new UpdateUserInDBTask();
        updateUserInDBTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,user);
    }

    private class UpdateUserInDBTask extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User... user){
            userDao.updateUser(user[0]);
            return null;
        }
    }

    public void deleteOneFollowigForUser(User user, String email){
        user.deleteFollowig(email);
        updateUser(user);
    }
}



