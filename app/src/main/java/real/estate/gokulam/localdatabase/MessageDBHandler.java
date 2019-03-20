package real.estate.gokulam.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import real.estate.gokulam.localdatabase.pojo.MessagePojo;

public class MessageDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "Message";

    public MessageDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Messagechat");
        db.execSQL("create  table Messagechat ( id integer primary key autoincrement ," +
                "message_id long default  null ," +
                "chaturl varchar(255) not null," +
                "message Text default null," +
                "currenttime long default null ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DATABASEUPDATE", "" + oldVersion);
        Log.d("DATABASEUPDATE", "" + newVersion);
        onCreate(db);
    }

    public boolean insertdata(MessagePojo item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("chaturl", item.getChannelurl());
        values.put("message", item.getPayLoad());
        values.put("message_id", item.getMessageID());
        values.put("currenttime", item.getTimestamp());
        Long sucess = database.insert("Messagechat", null, values);
        database.close();
        Log.d("INSERT", " " + sucess);
        return (sucess > 0);
    }

    public ArrayList<MessagePojo> getdatafrom(String chaturl) {
        ArrayList<MessagePojo> messagePojoArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  Messagechat where  chaturl = '" + chaturl + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id =cursor.getInt(cursor.getColumnIndex("id"));
                    byte[] payload = cursor.getBlob(cursor.getColumnIndex("message"));
                    Long messageId = cursor.getLong(cursor.getColumnIndex("message_id"));
                    Long timestamp = cursor.getLong(cursor.getColumnIndex("currenttime"));
                    MessagePojo messagePojo =new MessagePojo(id,messageId,payload,timestamp,chaturl);
                    messagePojoArrayList.add(messagePojo);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
    return messagePojoArrayList;
    }

    public  void DeleteURldata(String chaturl){
        try{
            SQLiteDatabase database =this.getWritableDatabase();
            database.execSQL("delete from Messagechat where chaturl='"+chaturl+"'" );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
