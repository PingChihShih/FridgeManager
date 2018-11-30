package cool.project.fridgemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ItemDB {
    private SQLiteDatabase db;
    Context context;
    final String TABLE_NAME = "Item"; //資料表的名稱
    final int MAX_LENGTH = 50; //限制最多幾筆資料

    //建構子
    public ItemDB(Context c){
        context = c;
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public String toString(){
        return "FridgeData.db";
    }

    //取得資料的筆數
    public int getCount(){
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(id) FROM " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            count = cursor.getCount();
        }
        cursor.close();
        return count;
    }

    //新增一個item
    public void add(String name, String uri, String tag, Date putdate, Date duedate){

        if(getCount()>=MAX_LENGTH){ //記錄的看板到達上限
            String orderBy =  "timestamp ASC";
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
            if(cursor.moveToFirst()){
                int first_id = cursor.getInt(0);
                db.delete(TABLE_NAME, "id = " + first_id, null); //刪掉第一筆記錄
            }
        }

        ContentValues values = new ContentValues();
        if(name != "") values.put("name", name);
        if(uri != "") values.put("uri", uri);
        if(tag != "") values.put("tag", tag);
        if(putdate != null) values.put("put_date", getDateTime(putdate));
        if(duedate != null) values.put("due_date", getDateTime(duedate));
        db.insert(TABLE_NAME, null, values);
    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    //輸出瀏覽過的看板列表，依時間排序，由新到舊

    public ArrayList<ArrayList<String>> getFullData() {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        String orderBy = "timestamp DESC";
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            ArrayList<String> tmp = new ArrayList<>();
            for(int i = 1; i <= 6; i++) {
                tmp.add(cursor.getString(i));
            }
            result.add(tmp);
        }
        cursor.close();
        return result;
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> result = new ArrayList<>();
        String orderBy = "timestamp DESC";
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            Item tmp = new Item(context);
            System.out.println("****************************");
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getString(2));
            System.out.println(cursor.getString(3));
            System.out.println(cursor.getString(4));
            System.out.println(cursor.getString(5));
            System.out.println(cursor.getString(6));
            System.out.println("****************************");
            tmp.setValues(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5));
            tmp.setId(cursor.getString(0));
            result.add(tmp);
        }
        cursor.close();
        return result;
    }

    public ArrayList<String> getArrayList(){
        ArrayList<String> result = new ArrayList<>();
        String orderBy = "timestamp DESC";
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1) + " " + cursor.getString(3));
        }
        cursor.close();
        return result;
    }

    //清除所有資料
    public void clear(){
        db.delete(TABLE_NAME, null, null);
    }

    //關閉資料庫
    public void close(){
        db.close();
    }

}
