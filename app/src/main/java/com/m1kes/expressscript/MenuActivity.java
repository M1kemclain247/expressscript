package com.m1kes.expressscript;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m1kes.expressscript.adapters.menu.DefaultListMenuFragment;
import com.m1kes.expressscript.adapters.menu.RightDrawerListAdapter;
import com.m1kes.expressscript.fragments.ContactsFragment;
import com.m1kes.expressscript.fragments.HealthTipsFragment;
import com.m1kes.expressscript.fragments.QuotesFragment;
import com.m1kes.expressscript.fragments.RequestQuotesFragment;
import com.m1kes.expressscript.objects.MenuId;
import com.m1kes.expressscript.objects.MenuItem;
import com.m1kes.expressscript.utils.extra.AbstractListMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity  implements AbstractListMenuFragment.OnListMenuListener,  View.OnClickListener{


    private ImageView backIcon;
    private ImageView drawImageView;
    private ImageView homeImageView;
    private ImageView contactUsImageView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RelativeLayout menuHeading;

    public static String LANDING_FRAGMENT_TAG = "LANDING_MENU_FRAGMENT";
    public static String MENU_FRAGMENT_TAG = "MENU_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        createActionBar();
        createNavigationDraw();

        backIcon = findViewById(R.id.toolbar_back_button);
        backIcon.setOnClickListener(this);

        drawImageView = findViewById(R.id.toolbar_drawer_icon);
        drawImageView.setOnClickListener(this);

        homeImageView = findViewById(R.id.right_draw_home_imageview);
        homeImageView.setOnClickListener(this);

        contactUsImageView = findViewById(R.id.right_draw_contact_imageview);
        contactUsImageView.setOnClickListener(this);
        menuHeading = findViewById(R.id.menu_heading);


        if (savedInstanceState != null) {
            return;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ImageView drawerIcon = findViewById(R.id.toolbar_drawer_icon);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawerIcon.setVisibility(View.VISIBLE);

        try {
            TextView versionTextView = findViewById(R.id.version_textview);

            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version = "ExpressScript Version " + version;
            versionTextView.setVisibility(View.VISIBLE);
            versionTextView.setText(version);

        } catch (PackageManager.NameNotFoundException ignore) { }


        replaceFragment(createLandingListMenu(), LANDING_FRAGMENT_TAG);
    }


    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(tag);

        fragmentTransaction.commit();
    }

    private void createNavigationDraw() {

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        String username = "Welcome back!";

        TextView nameView = drawerLayout.findViewById(R.id.right_drawer_user_name_textview);
        nameView.setText(username);

        TextView lastLoginView = drawerLayout.findViewById(R.id.right_draw_user_lastlogin_textview);

        ListView bankingListView = drawerLayout.findViewById(R.id.right_drawer_banking_listview);

        final List<MenuItem> sideMenuDrawer = new ArrayList<>();
        sideMenuDrawer.add(new MenuItem("Navigation"));
        sideMenuDrawer.add(new MenuItem(MenuId.CREATE_QUOTE_MENU, "Request Quotes", null, R.drawable.icon_cart));
        sideMenuDrawer.add(new MenuItem(MenuId.ORDERS_MENU, "Orders", null, R.drawable.icon_connect));
        sideMenuDrawer.add(new MenuItem(MenuId.QUOTES_MENU, "Quotes", null, R.drawable.icon_cart));
        sideMenuDrawer.add(new MenuItem(MenuId.HEALTH_TIPS_MENU, "Health Tips", null, R.drawable.icon_locator));
        sideMenuDrawer.add(new MenuItem(MenuId.BRANCH_LOCATOR, "Branch Locator", null, R.drawable.icon_locator));
        sideMenuDrawer.add(new MenuItem(MenuId.CONTACT_US_MENU, "Contact  us", null, R.drawable.icon_connect));


        bankingListView.setAdapter(new RightDrawerListAdapter(this, sideMenuDrawer));
        bankingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem menuItem = sideMenuDrawer.get(i);
                if (menuItem.getId() != null) {
                    initialiseMenu(menuItem.getId(), false);
                }
            }
        });


    }

    protected AbstractListMenuFragment createLandingListMenu() {
        return new DefaultListMenuFragment().createMenus().showCabsHeading(true)
                .addMenu(MenuId.CREATE_QUOTE_MENU,"Create Quote",
                        "Request's a Quote", R.drawable.cart_plus)
                .addMenu(MenuId.QUOTES_MENU, "Quotes",
                        "View all your Quotes", R.drawable.file_compare)
                .addMenu(MenuId.ORDERS_MENU, "Orders",
                        "View all your Orders", R.drawable.icon_cart)
                .addMenu(MenuId.MEDICAL_AID, "Medical Aid",
                        "Manage linked Medical Aids", R.drawable.credit_card)
                .addMenu(MenuId.BRANCH_LOCATOR, "Branch Locator",
                        "Find a branch near you", R.drawable.icon_locator)
                .addMenu(MenuId.HEALTH_TIPS_MENU, "Health Tips",
                        "Health Tips keeping you in shape", R.drawable.book_open_page_variant)
                .addMenu(MenuId.MY_PROFILE, "My Profile",
                        "View your profile", R.drawable.face_profile)
                .addMenu(MenuId.CONTACT_US_MENU, "Contact Us",
                        "Connect with us now!", R.drawable.icon_connect);
    }

    protected void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    private void createActionBar() {
        toolbar =  findViewById(R.id.cabs_toolbar);
        setSupportActionBar(toolbar);
    }

    protected void toggleDrawerClosed() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public void onClick(View v) {
        if (R.id.toolbar_back_button == v.getId()) {
            onBackClicked(v);
            return;
        }
        if (R.id.toolbar_drawer_icon == v.getId()) {
            drawerLayout.openDrawer(Gravity.RIGHT);
            hideKeyboard();
            return;
        }
        if (R.id.right_draw_home_imageview == v.getId()) {
            initialiseMenu(MenuId.LANDING_MAIN_MENU, false);
            return;
        }
    }

    protected void initialiseMenu(MenuId menuId, boolean direct) {

        toggleDrawerClosed();
        hideKeyboard();

        switch (menuId) {
            case LANDING_MAIN_MENU:{
                replaceFragment(createLandingListMenu(), MENU_FRAGMENT_TAG);
                break;
            }
            case CREATE_QUOTE_MENU: {
                startActivity(new Intent(MenuActivity.this,CreateQuotation.class));
                break;
            }
            case QUOTES_MENU: {
                startActivity(new Intent(MenuActivity.this,Quotes.class));
                break;
            }
            case ORDERS_MENU: {
                startActivity(new Intent(MenuActivity.this,Orders.class));
                break;
            }
            case MEDICAL_AID: {
                startActivity(new Intent(MenuActivity.this,MedicalAidActivity.class));
                break;
            }
            case BRANCH_LOCATOR: {
                startActivity(new Intent(MenuActivity.this,BranchNavigatorActivity.class));
                break;
            }
            case HEALTH_TIPS_MENU: {
                replaceFragment(new HealthTipsFragment(), MENU_FRAGMENT_TAG);
                break;
            }
            case CONTACT_US_MENU: {
                replaceFragment(new ContactsFragment(), MENU_FRAGMENT_TAG);
                break;
            }
        }
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.getFragments().isEmpty()) {
            replaceFragment(createLandingListMenu(), LANDING_FRAGMENT_TAG);
        }else {
            super.onBackPressed();
            hideKeyboard();
        }
    }

    @Override
    public void onMenuItemClicked(MenuItem menuItem) {
        initialiseMenu(menuItem.getId(), false);
    }

}
