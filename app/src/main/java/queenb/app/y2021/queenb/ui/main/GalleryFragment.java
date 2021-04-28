package queenb.app.y2021.queenb.ui.main;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

import queenb.app.y2021.queenb.AddImage;
import queenb.app.y2021.queenb.R;
import queenb.app.y2021.queenb.SQLiteDBHandler;

public class GalleryFragment extends Fragment{

    SQLiteDBHandler sqLiteDBHandler;
    public SQLiteDatabase sqLiteDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, null);
        FloatingActionButton addImageButton = (FloatingActionButton) root.findViewById(R.id.addImageButton);
        //int i = (((ViewGroup)getView().getParent()).getId());

        // try and catch so that the app could handle opening the app again after creating the table
        // already
        try {
            sqLiteDBHandler = new SQLiteDBHandler(getActivity(), "ImageDatabase", null, 1);
            sqLiteDatabase = sqLiteDBHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE ImageTable(Image BLOB)");
        } catch (Exception e) {
            e.printStackTrace(); //TODO - maybe change the line here
        }

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddImage.class);
                startActivity(i);
            }
        });


        GridView imageGridView = (GridView) root.findViewById(R.id.imageGrid);
        ArrayList<Bitmap> bitmapArray = sqLiteDBHandler.getImageList();
        if (bitmapArray != null) {  // if the bitmaps were found
            ImageGridViewAdapter adapter = new ImageGridViewAdapter(getActivity(), bitmapArray, 500);   // using custom adapter for showing images
            // columnWidth is just some int value representing image width
            imageGridView.setAdapter(adapter);
        }

        // Inflate the layout for this fragment
        return root;
    }

    public void startAddImageActivity(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), AddImage.class);
        startActivity(i);
    }

}

