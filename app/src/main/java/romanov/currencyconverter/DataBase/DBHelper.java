package romanov.currencyconverter.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_STORY = "storyDb"; // имя бд.
    public static final String TABLE_CONVERT_STORY= "storyTable";  // имя табл.

    //коллонки таблицы
    public static final String KEY_ID       = "_id";
    public static final String KEY_CODE_fROM= "codeFrom";
    public static final String KEY_CODE_TO  = "codeTo";
    public static final String KEY_ENTER_NUM  = "enterNum";
    public static final String KEY_PRICE    = "price";
    public static final String KEY_TIME     = "time";


    public DBHelper(Context context) {
        super(context, DATABASE_STORY, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONVERT_STORY + " (" + KEY_ID
                + " integer primary key," + KEY_CODE_fROM + " text," + KEY_CODE_TO + " text," + KEY_ENTER_NUM + " text," + KEY_PRICE + " text," + KEY_TIME + " text" +") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exits " + TABLE_CONVERT_STORY);

        onCreate(db);
    }
}
