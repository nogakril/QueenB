package queenb.app.y2021.queenb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import queenb.app.y2021.queenb.ui.main.GalleryFragment;
import queenb.app.y2021.queenb.ui.main.MeetingsFragment;
import queenb.app.y2021.queenb.ui.main.ViewPagerAdapter;

public class MainScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTabs();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AddImage.isBackFromAddImage){
            ViewPager viewPager = findViewById(R.id.viewPager);
            viewPager.setCurrentItem(2);
        }
    }

    private void setUpTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MeetingsFragment(), "Meetings");
        adapter.addFragment(new GalleryFragment(), "Gallery");
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabs.getTabAt(0)).setIcon(R.drawable.ic_baseline_school_24);
        Objects.requireNonNull(tabs.getTabAt(1)).setIcon(R.drawable.ic_baseline_photo_library_24);
    }
}