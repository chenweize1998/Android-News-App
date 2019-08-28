//package com.example.newstoday;
//
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import androidx.annotation.NonNull;
//import androidx.room.Dao;
//import androidx.room.Database;
//import androidx.room.Delete;
//import androidx.room.Entity;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.PrimaryKey;
//import androidx.room.Query;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.concurrent.ExecutionException;
//
//@Entity
//class SearchHistory {
//
//    @NonNull
//    @PrimaryKey
//    private String keyword;
//
//    public SearchHistory(String keyword){
//        this.keyword = keyword;
//    }
//
//    String getKeyword(){
//        return keyword;
//    }
//
//    void setKeyword(String keyword){
//        this.keyword = keyword;
//    }
//
//}
//
//@Dao
//interface SearchHistoryDao{
//
//    @Query("select keyword from SearchHistory")
//    String[] getAllSearchHistory();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertSearchHistory(SearchHistory... keywords);
//
//    @Delete
//    void deleteSearchHistory(SearchHistory... keywords);
//}
//
//@Database(entities = SearchHistory.class, version = 1)
//abstract class SearchHistoryDB extends RoomDatabase {
//
//    public abstract SearchHistoryDao searchHistoryDao();
//
//    public static SearchHistoryDB getSearchHistoryDB() {
//        return getSearchHistoryDB();
//    }
//
//    public static SearchHistoryDB getSearchHistoryDB(Context context, String name){
//        return  Room.databaseBuilder(context.getApplicationContext(), SearchHistoryDB.class,
//                name).build();
//    }
//}
//
//class SearchHistoryRepo{
//
//    private final SearchHistoryDao searchHistoryDao;
//    private final SearchHistoryDB searchHistoryDB;
//
//    public SearchHistoryRepo(SearchHistoryDB searchHistoryDB){
//        this.searchHistoryDB = searchHistoryDB;
//        this.searchHistoryDao = searchHistoryDB.searchHistoryDao();
//    }
//
//    /**
//     * get all search history
//     */
//    public ArrayList<String> getAllSearchHistory(){
//        try{
//            GetAllSearchHistoryTask getAllSearchHistoryTask = new GetAllSearchHistoryTask();
//            return new ArrayList(Arrays.asList(getAllSearchHistoryTask.execute(0).get()));
//        }catch(ExecutionException e){
//            e.printStackTrace();
//        }catch(InterruptedException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private class GetAllSearchHistoryTask extends AsyncTask<Integer, Void, String[]>{
//
//        @Override
//        protected String[] doInBackground(Integer... params){
//            return searchHistoryDao.getAllSearchHistory();
//        }
//    }
//
//
//    /**
//     * insert search history into database
//     */
//    public void insertSearchHistory(String... keywords){
//        InsertSearchHistoryTask insertSearchHistoryTask = new InsertSearchHistoryTask();
//        insertSearchHistoryTask.execute(keywords);
//    }
//
//    private class InsertSearchHistoryTask extends AsyncTask<String, Void, Void>{
//
//        @Override
//        protected Void doInBackground(String... keywords){
//            searchHistoryDao.insertSearchHistory(new SearchHistory(keywords[0]));
//            return null;
//        }
//    }
//
//
//    /**
//     * delete search history from database
//     */
//    public void deleteSearchHistory(String... keywords){
//        DeleteSearchHistoryTask deleteSearchHistoryTask = new DeleteSearchHistoryTask();
//        deleteSearchHistoryTask.execute(keywords);
//    }
//
//    private class DeleteSearchHistoryTask extends AsyncTask<String, Void, Void>{
//
//        @Override
//        protected Void doInBackground(String... keywords){
//            searchHistoryDao.deleteSearchHistory(new SearchHistory(keywords[0]));
//            return null;
//        }
//    }
//
//}
//
//class SearchHistoryManager{
//
//    private SearchHistoryRepo searchHistoryRepo;
//    private SearchHistoryManager INSTANCE;
//
//    private SearchHistoryManager(Context context){
//        searchHistoryRepo = new SearchHistoryRepo(SearchHistoryDB.getSearchHistoryDB(context, "searchHistory"));
//    }
//
//    public SearchHistoryManager getSearchHistoryManager(Context context){
//        if(INSTANCE == null){
//            INSTANCE = new SearchHistoryManager(context);
//        }
//        return INSTANCE;
//    }
//
//    public ArrayList<String> getAllSearchHistory(){
//        return this.searchHistoryRepo.getAllSearchHistory();
//    }
//
//    public void insertSearchHistory(String... keywords){
//        this.searchHistoryRepo.insertSearchHistory(keywords);
//    }
//
//    public void deleteSearchHistory(String... keywords){
//        this.searchHistoryRepo.deleteSearchHistory(keywords);
//    }
//}
