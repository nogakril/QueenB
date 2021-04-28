package queenb.app.y2021.queenb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImage extends AppCompatActivity  {

    private ImageView imageView;
    private TextView textStatus;
    private static final int GALLERY_REQUEST = 1046;
    private Boolean isImageSet; // is imageView is the initial icon or image from gallery
    private Uri imageData; // the image data to upload
    private Button uploadButton;
    private Button pickImageButton;

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDBHandler sqLiteDBHandler;
    public static boolean isBackFromAddImage;

    public AddImage() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        isBackFromAddImage = Boolean.FALSE;
        isImageSet = Boolean.FALSE;
        imageView = findViewById(R.id.imagePreview);
        textStatus = findViewById(R.id.textStatus);
        uploadButton = findViewById(R.id.uploadImageButton);
        pickImageButton = findViewById(R.id.pickImageButton);

        final FloatingActionButton backButton = findViewById(R.id.backButton);
        //String clearDBQuery = "DELETE FROM "+ "ImageDatabase";
        //sqLiteDatabase.execSQL(clearDBQuery);


        try {
            sqLiteDBHandler = new SQLiteDBHandler(this, "ImageDatabase", null, 1);
            sqLiteDatabase = sqLiteDBHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE ImageTable(Image BLOB)");
        } catch (Exception e) {
            e.printStackTrace(); //TODO - maybe change the line here
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST);

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToDataBase(v);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddImage.this, MainScreenActivity.class);
                isBackFromAddImage = Boolean.TRUE;
                startActivity(i);
                finish();
            }
        });


    }

    public void uploadImageToDataBase(View v) {
        if (isImageSet == Boolean.TRUE){
            Bitmap bitmap = loadFromUri(imageData);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,0, byteArrayOutputStream);
            byte[] bytesImage = byteArrayOutputStream.toByteArray();

            ContentValues contentValues = new ContentValues();
            contentValues.put("Image", bytesImage);

            sqLiteDatabase.insert("ImageTable", null, contentValues);
            textStatus.setText("התמונה הועלתה בהצלחה");
            textStatus.setVisibility(TextView.VISIBLE);
        }
        else{
            // TODO - choose what to do

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                imageData = data.getData();
                imageView.setImageURI(imageData);
                isImageSet = Boolean.TRUE;
                uploadButton.setVisibility(View.VISIBLE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else { // TODO - maybe delete it
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}