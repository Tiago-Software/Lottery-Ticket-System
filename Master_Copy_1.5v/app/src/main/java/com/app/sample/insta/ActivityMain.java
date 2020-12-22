package com.app.sample.insta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.sample.insta.adapter.PageFragmentAdapter;

import com.app.sample.insta.data.Tools;
import com.app.sample.insta.fragment.PageFriendFragment;
import com.app.sample.insta.fragment.PageHomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
/**
 * Created by Wesley Wienand, Tiago Pinto, Daniel Dos Santos on 30,01,2020
 */
public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private View parent_view;
    private FirebaseAuth firebaseAuth;

    private PageFragmentAdapter adapter;

    private PageHomeFragment f_home;
    private PageFriendFragment f_ticket;
    private static int[] imageResId = {
            R.drawable.tab_home,
            R.drawable.tab_ticket,
            R.drawable.tab_gallery,
            R.drawable.tab_friend,
            R.drawable.tab_profile
    };

    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this);
        }

        parent_view = findViewById(android.R.id.content);

        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setTitle("");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupTabClick();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String dir = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();

                Intent intent = new Intent(ActivityMain.this, LiveBarcodeScanningActivity.class);
                File photo = new File(getExternalFilesDir(dir),  "Pic.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                intent.putExtra("uID", firebaseAuth.getCurrentUser().getUid());
                startActivityForResult(intent, TAKE_PICTURE);
             /* startActivity(new Intent(ActivityMain.this, LiveBarcodeScanningActivity.class));*/

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser)
    {

        if (currentUser == null)
        {
            startActivity(new Intent(ActivityMain.this, ActivityLogin.class));
        } else
        {
            return;
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_home == null) {
            f_home = new PageHomeFragment();
        }
        if (f_ticket == null) {
            f_ticket = new PageFriendFragment();
        }

        adapter.addFragment(f_home, null);
        adapter.addFragment(f_ticket, null);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
    }

    private void setupTabClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                //actionbar.setTitle(adapter.getTitle(position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity(); // Close all activites
            System.exit(0);  // Releasing resources
        }
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }
}
