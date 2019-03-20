package real.estate.gokulam.localdatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sendbird.android.BaseMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.MessageListQuery
import real.estate.gokulam.localdatabase.pojo.Notificationpojo


class MyDBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {
    companion object {
        private val DB_NAME = "UsersDB"
        private val DB_VERSIOM = 1
        private val TABLE_NAME = "Chats"
        private val ID = "id"
        private val SENDER_ID = "SENDER_ID"
        private val SENDER_NAME = "SENDER_NAME"
        private val CHANNEL_URL = "CHANNEL_URL"
        private val CHANNEL_NAME = "CHANNEL_NAME"
        private val CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP"
        private val COLUMN_NAME_PAYLOAD = "PAYLOAD"
        private val COLUMN_NAME_ID = "MESSAGE_ID"
        private val MESSAGE = "MESSAGE"
    }

    /* override fun onOpen(db: SQLiteDatabase) {
         onCreate(db)
     }*/
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID Integer PRIMARY KEY, $SENDER_ID TEXT,$SENDER_NAME TEXT, $CHANNEL_URL TEXT, $MESSAGE TEXT,$CHANNEL_NAME TEXT,$CURRENT_TIMESTAMP  DATETIME DEFAULT CURRENT_TIMESTAMP)"
        db?.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //Inserting (Creating) data
    fun addUser(item: Notificationpojo): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SENDER_ID, item.sender_id)
        values.put(CHANNEL_URL, item.channel_url)
        values.put(SENDER_NAME, item.sendername)
        values.put(MESSAGE, item.message)
        values.put(CHANNEL_NAME, item.channelname)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    //get all users
    fun getAllUsers(): ArrayList<Notificationpojo> {
        var allUser = ArrayList<Notificationpojo>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val sender_id = cursor.getString(cursor.getColumnIndex(SENDER_ID))
                    val sender_name = cursor.getString(cursor.getColumnIndex(SENDER_NAME))
                    val channel_url = cursor.getString(cursor.getColumnIndex(CHANNEL_URL))
                    val message = cursor.getString(cursor.getColumnIndex(MESSAGE))
                    val channel_name = cursor.getString(cursor.getColumnIndex(CHANNEL_NAME))
                    val current_time_stamp = cursor.getString(cursor.getColumnIndex(CURRENT_TIMESTAMP))
                    Log.d("CURRENT_TIME", "" + current_time_stamp)
                    val user = Notificationpojo(id.toInt(), sender_id, sender_name, channel_url, message, channel_name)
                    allUser.add(user)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allUser
    }


    fun getAllUserschannels(): ArrayList<Notificationpojo> {
        var allUser = ArrayList<Notificationpojo>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME  GROUP BY  $CHANNEL_URL "
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val sender_id = cursor.getString(cursor.getColumnIndex(SENDER_ID))
                    val sender_name = cursor.getString(cursor.getColumnIndex(SENDER_NAME))
                    val channel_url = cursor.getString(cursor.getColumnIndex(CHANNEL_URL))
                    val message = cursor.getString(cursor.getColumnIndex(MESSAGE))
                    val channel_name = cursor.getString(cursor.getColumnIndex(CHANNEL_NAME))
                    val user = Notificationpojo(id.toInt(), sender_id, sender_name, channel_url, message, channel_name)
                    allUser.add(user)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allUser
    }


    @SuppressLint("Recycle")
    fun getdata(chaturl: String): ArrayList<Notificationpojo> {
        val db = readableDatabase
        /**
         * get all column names
         *
        val dbCursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val columnNames = dbCursor.getColumnNames()
        for (columnames in columnNames){
        Log.d("TABLECOLUMN ",""+columnames)
        }*/

        var allUser = ArrayList<Notificationpojo>()
        val selectALLQuery = "SELECT * FROM $TABLE_NAME  WHERE $CHANNEL_URL = '$chaturl'"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val sender_id = cursor.getString(cursor.getColumnIndex(SENDER_ID))
                    val sender_name = cursor.getString(cursor.getColumnIndex(SENDER_NAME))
                    val channel_url = cursor.getString(cursor.getColumnIndex(CHANNEL_URL))
                    val message = cursor.getString(cursor.getColumnIndex(MESSAGE))
                    val channel_name = cursor.getString(cursor.getColumnIndex(CHANNEL_NAME))
                    val user = Notificationpojo(id.toInt(), sender_id, sender_name, channel_url, message, channel_name)
                    allUser.add(user)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allUser
    }


    fun getchannelurl(id: Int): String {
        val db = readableDatabase
        var channel_url = ""
        var allUser = ArrayList<Notificationpojo>()
        val selectALLQuery = "SELECT * FROM $TABLE_NAME  WHERE $ID = $id"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                channel_url = cursor.getString(cursor.getColumnIndex(CHANNEL_URL))
            }
        }
        cursor.close()
        db.close()
        return channel_url
    }


    fun delete(id: String): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$ID=$id", null) > 0

    }

    fun truncate() {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_NAME)
        db.close()
    }

/*    fun sendbird(CURRENT_CHANNEL_URL :String,mChannel:GroupChannel) {
        // Get messages from local database
        val database = this.writableDatabase!!
        val selection = CHANNEL_URL + " = ?"
        val selectionArgs = arrayOf<String>(CURRENT_CHANNEL_URL)

        val sortOrder = CURRENT_TIMESTAMP + " DESC"

        val cursor = database.query(
                TABLE_NAME, // Don't group the rows.
                null, // The columns to return; all if null.
                selection, // The columns for the WHERE clause
                selectionArgs, null, null, // Don't filter by row groups.
                sortOrder, // The sort order
                "30" // The limit
        )// The values for the WHERE clause

// Create a List of BaseMessages by deserializing each item.
        val prevMessageList = ArrayList<BaseMessage>()

        while (cursor.moveToNext()) {
            val data = cursor.getBlob(cursor.getColumnIndex(PAYLOAD))
            val message = BaseMessage.buildFromSerializedData(data)
            prevMessageList.add(message)
        }

        cursor.close()

// Pass messages to adapter for displaying them in a RecyclerView, ListView, and so on.
        mMessageListAdapter.addMessages(prevMessageList)

// Get new messages from SendBird server
        val latestStoredTs = prevMessageList.get(0).getCreatedAt() // Get the timestamp of the last stored message.
        val query = mChannel.createMessageListQuery()
        query.next(latestStoredTs, 30, false, MessageListQuery.MessageListQueryResult { list, e ->
            if (e != null) {
                // Error!
                return@MessageListQueryResult
            }

            // New messages successfully fetched.
            mMessageListAdapter.addMessages(list)

            // Insert each new message in your local database.
            for (message in list) {
                // Store each new message into the local database.
                val values = ContentValues()
                values.put(MESSAGE_ID, message.messageId)
                values.put(CHANNEL_URL, message.channelUrl)
                values.put(CURRENT_TIMESTAMP, message.createdAt)
                values.put(PAYLOAD, message.serialize())

                database.insert(TABLE_NAME, null, values)
            }
        })

        database.close()

    }*/
}
