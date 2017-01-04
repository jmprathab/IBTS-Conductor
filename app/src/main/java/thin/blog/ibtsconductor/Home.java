package thin.blog.ibtsconductor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static thin.blog.ibtsconductor.ApplicationHelper.writeToSharedPreferences;

/**
 * Main Activity which contains Navigation View and takes care of all fragment replacement
 */
public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String QR_CODE_DATA = "qr_code_data";
    public static final int QR_CODE_REQUEST_CODE = 1;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    private FragmentManager fragmentManager;
    private boolean backPressed = false;

    @OnClick(R.id.floating_action_button)
    public void floatingButtonPressed() {
        android.support.v4.app.Fragment current = fragmentManager.findFragmentById(R.id.activity_root_layout_linear);
        if (current instanceof Scan) {
            Intent intent = new Intent(this, QRReader.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        Scan scan = (Scan) fragmentManager.findFragmentByTag("SCAN");
        if (scan == null) {
            scan = Scan.newInstance();
        }
        replaceFragment(scan, "SCAN", "Scan Passenger");
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setImageResource(R.drawable.icon_search);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed) {
                super.onBackPressed();
                return;
            }
            this.backPressed = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Settings settings = (Settings) fragmentManager.findFragmentByTag("SETTINGS");
            if (settings == null) {
                settings = Settings.newInstance();
            }
            replaceFragment(settings, "SETTINGS", "Settings");
            floatingActionButton.setVisibility(View.INVISIBLE);
            return true;
        } else if (id == R.id.action_refresh) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.activity_root_layout_linear);
            if (fragment instanceof Scan) {
                ((Scan) fragment).doRefresh();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        navigationView.setCheckedItem(id);
        if (id == R.id.menu_scan) {
            Scan scan = (Scan) fragmentManager.findFragmentByTag("SCAN");
            if (scan == null) {
                scan = Scan.newInstance();
            }
            replaceFragment(scan, "SCAN", "Scan Passenger");
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(R.drawable.icon_search);
        } else if (id == R.id.menu_trip_details) {

            TripDetails tripDetails = (TripDetails) fragmentManager.findFragmentByTag("TRIP_DETAILS");
            if (tripDetails == null) {
                tripDetails = TripDetails.newInstance();
            }
            replaceFragment(tripDetails, "TRIP_DETAILS", "Trip Details");
            floatingActionButton.setVisibility(View.INVISIBLE);

        } else if (id == R.id.menu_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AlertDialogDark);
            AlertDialog dialog;
            builder.setCancelable(false);
            builder.setTitle("Logout of Application?");
            builder.setMessage("Do you want to logout of the Application");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    writeToSharedPreferences(Constants.SUCCESSFUL_LOGIN_HISTORY, false);
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO:Update current selected fragment in navigation drawer
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } else if (id == R.id.menu_settings) {
            Settings settings = (Settings) fragmentManager.findFragmentByTag("SETTINGS");
            if (settings == null) {
                settings = Settings.newInstance();
            }
            replaceFragment(settings, "SETTINGS", "Settings");
            floatingActionButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.help) {
            Help help = (Help) fragmentManager.findFragmentByTag("HELP");
            if (help == null) {
                help = Help.newInstance();
            }
            replaceFragment(help, "HELP", "Help");
            floatingActionButton.setVisibility(View.INVISIBLE);

        } else if (id == R.id.about) {
            About about = (About) fragmentManager.findFragmentByTag("ABOUT");
            if (about == null) {
                about = About.newInstance();
            }
            replaceFragment(about, "ABOUT", "About");
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CODE_REQUEST_CODE) {
            if (data != null) {
                String qrCodeData = data.getStringExtra(QR_CODE_DATA);
                Fragment fragment = fragmentManager.findFragmentById(R.id.activity_root_layout_linear);
                if (fragment instanceof Scan) {
                    ((Scan) fragment).processQRCodeData(qrCodeData);
                }
            } else {
                Toast.makeText(this, "Scan QR Code Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void replaceFragment(Fragment fragment, String tag, String actionBarTitle) {
        fragmentManager.beginTransaction().replace(R.id.activity_root_layout_linear, fragment, tag).commit();
        getSupportActionBar().setTitle(actionBarTitle);
    }
}
