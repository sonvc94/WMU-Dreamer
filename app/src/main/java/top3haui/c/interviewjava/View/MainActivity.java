package top3haui.c.interviewjava.View;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top3haui.c.interviewjava.R;
import top3haui.c.interviewjava.models.Categorys;
import top3haui.c.interviewjava.models.Items;
import top3haui.c.interviewjava.models.SQLite;
import top3haui.c.interviewjava.models.SQLiteDataController;

public class MainActivity extends AppCompatActivity {
    public static int navItemIndex = 0;
    private Toolbar toolbar;
    private View navHeader;
    private ArrayList<Items> item;
    private ArrayList<Items> mParent;
    private HashMap<Items, List<String>> mChild;
    private ArrayList<Categorys> categoryses;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_LIKE = "like";
    //    private static final String TAG_TYPE = "type";
    private boolean shouldLoadHomeFragOnBackPress = true;
    public static String CURRENT_TAG = TAG_HOME;
    private AlertDialog.Builder dialog;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    //    // flag to load home fragment when user presses back key
//    private boolean shouldLoadHomeFragOnBackPress = true;
    boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler;
    SQLite like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        item= new ArrayList<>();
        mParent= new ArrayList<>();
        mChild=new HashMap<>();
        categoryses=new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askpmis();
        }
        createDB();
        dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thoát")
                .setMessage("Bạn có muốn thoát ứng dụng không?")
                .setIcon(R.mipmap.ic_exit)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);

                    }

                })
                .setNegativeButton("No", null);

        mHandler = new Handler();
        navHeader = navigationView.getHeaderView(0);
//        txtName = (TextView) navHeader.findViewById(R.id.name);
//        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
//        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
//        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        navigationView.setItemIconTintList(null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        like = new SQLite(this);
        item = like.getList();
        categoryses = like.getListCategory();
        for (Items s : item) {
            ArrayList<String> childList = new ArrayList<>();
            childList.add(s.getInformation());
            mParent.add(s);
            mChild.put(s, childList);
        }
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        startActivity(new Intent(getApplicationContext(), Splash.class));
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }

    private void loadHomeFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar titl

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    private void createDB() {
// khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(this);
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                FragmentHome fragmentHome = new FragmentHome(mParent,mChild,categoryses);
                return fragmentHome;
            // return FragmentHome.newInstance();
            case 2:
                FragmentLike fragmentLike = new FragmentLike();
                return fragmentLike;
            //  return FragmentLike.newInstance();
            default:
                return new FragmentHome();
            //return FragmentHome.newInstance();
        }
    }


    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_Home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

//                    case R.id.nav_type:
//                        navItemIndex = 1;
//                        CURRENT_TAG = TAG_TYPE;
//                        break;
                    case R.id.nav_like:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_LIKE;
                        break;
//                    case R.id.nav_notifications:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_NOTIFICATIONS;
//                        break;
                    case R.id.nav_gopy:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "vancuongvp.pro@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, ActivityAbout.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_share:
                        // launch new intent instead of loading fragment
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Hey check out my app at: https://play.google.com/store/apps/details?id=top3haui.c.interviewjava");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_exit:
                        dialog.show();
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back lần nữa để thoát!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        //super.onBackPressed();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askpmis() {


        int hasWriteContactsPermission1 = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission1 != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
}
