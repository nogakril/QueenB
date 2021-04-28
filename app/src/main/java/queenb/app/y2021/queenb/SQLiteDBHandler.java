package queenb.app.y2021.queenb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteDBHandler extends SQLiteOpenHelper {
    public SQLiteDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Bitmap> getImageList() {

        Bitmap bitmap = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        String sqlTable = "ImageTable";
        String selectQuery = "SELECT * FROM "+ sqlTable;

        Cursor c = db.rawQuery(selectQuery, null);


        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        if (c != null) {
            while (c.moveToNext()) {
                byte[] blob = c.getBlob(c.getColumnIndex("Image"));
                bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                bitmaps.add(bitmap);
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return bitmaps;
    }
}
