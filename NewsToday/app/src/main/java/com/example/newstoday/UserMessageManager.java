package com.example.newstoday;

import android.content.Context;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class UserMessageManager {

    /**
     * 所有用户的所有发布的消息都存在一个共同的数据库里面。
     * 此类的作用就是控制管理这个数据库。
     * 此类的方法名比较详实，应该可以看懂对应方法的功能。
     * 此类的功能和ForwordingNewsManager类大致相同。
     * */

    private static UserMessageManager INSTANCE;
    private UserMessageDB userMessageDB;
    private UserMessageDao userMessageDao;

    private UserMessageManager(Context context){
        userMessageDB = UserMessageDB.getUserMessageDB(context, "userMessage");
        userMessageDao = userMessageDB.userMessageDao();
    }

    public static UserMessageManager getUserMessageManager(Context context){
        if(INSTANCE == null){
            INSTANCE = new UserMessageManager(context);
        }
        return INSTANCE;
    }

    /**
     * 发布一条message加进数据库，发布就调用
     * @param userMessage
     */
    public void addOneUserMessage(UserMessage... userMessage){
        AddOneUserMessageTask addOneUserMessageTask = new AddOneUserMessageTask();
        addOneUserMessageTask.execute(userMessage);
    }

    private class AddOneUserMessageTask extends AsyncTask<UserMessage, Void, Void>{
        @Override
        protected Void doInBackground(UserMessage...userMessage){
            userMessageDao.insert(userMessage[0]);
            return null;
        }
    }

    public ArrayList<UserMessage> getAllUserMessage(){
        try{
            GetAllUserMessageTask getAllUserMessageTask = new GetAllUserMessageTask();
            return new ArrayList(Arrays.asList(getAllUserMessageTask.execute(0).get()));
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllUserMessageTask extends AsyncTask<Integer, Void, UserMessage[]>{
        @Override
        protected UserMessage[] doInBackground(Integer... params){
            return userMessageDao.getAllUserMessage();
        }
    }

    /**
     * 得到某个人的所有message
     * @param email
     * @return
     */
    public ArrayList<UserMessage> getCerternUserMessageByUserEmail(String... email){
        try{
            GetCerternUserMessageByUserEmailTask getCerternUserMessageByUserEmail = new GetCerternUserMessageByUserEmailTask();
            return new ArrayList(Arrays.asList(getCerternUserMessageByUserEmail.execute(email).get()));
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetCerternUserMessageByUserEmailTask extends AsyncTask<String, Void, UserMessage[]>{
        @Override
        protected UserMessage[] doInBackground(String... email){
            return userMessageDao.getCerternUserMessageByUserEmail(email);
        }
    }

    /**
     * 所有关注人的message
     * @param user
     * @return
     */
    public ArrayList<UserMessage> getUserAllFollowigMessage(User user){
        String[] followigs = user.getFollowig().toArray(new String [user.getFollowig().size()]);
        return getCerternUserMessageByUserEmail(followigs);
    }

    /**
     * 删除某条
     */
    public void deleteMessageByMessageID(String... messageID){
        DeleteMessageByMessageIDTask deleteMessageByMessageIDTask = new DeleteMessageByMessageIDTask();
        deleteMessageByMessageIDTask.execute(messageID);
    }


    private class DeleteMessageByMessageIDTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... messageID){
            userMessageDao.deleteMessageByMessageID(messageID);
            return null;
        }
    }

    /**
     * 删除某个人的所有message
     * @param email
     */
    public void deleteMessageByUserEmail(String... email){
        DeleteMessageByUserEmailTask deleteMessageByUserEmailTask = new DeleteMessageByUserEmailTask();
        deleteMessageByUserEmailTask.execute(email);

    }

    private class DeleteMessageByUserEmailTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... email){
            userMessageDao.deleteMessageByUserEmail(email[0]);
            return null;
        }
    }




}
