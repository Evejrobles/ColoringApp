package edu.cnm.deepdive.coloringapp.controler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import edu.cnm.deepdive.coloringapp.ColorApplication;
import edu.cnm.deepdive.coloringapp.R;
import edu.cnm.deepdive.coloringapp.view.ColoringFragment;
import edu.cnm.deepdive.coloringapp.view.DrawingFragment;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private FloatingActionButton fab;
  private DrawingFragment fragment;
  private ColoringFragment coloring_fragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    fab = (FloatingActionButton) findViewById(R.id.fab);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    fab.setVisibility(View.INVISIBLE);
    fragment = new DrawingFragment();
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
        .commit();
  }
  private void displayLicensesAlertDialog() {
    WebView view = (WebView) LayoutInflater.from(this).inflate(R.layout.dialog_licenses, null);
    view.loadUrl("file:///android_asset/open_source_licenses.html");
   AlertDialog alertDialog = new Builder(this,
        R.style.Theme_AppCompat_Light_Dialog_Alert)
        .setTitle(getString(R.string.action_licenses))
        .setView(view)
        .setPositiveButton(android.R.string.ok, null)
        .show();
  }
  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.sign_out:
        signOut();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }


  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_draw) {
      fab.setVisibility(View.INVISIBLE);
      fragment = new DrawingFragment();
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
          fragment).commit();
    } else if (id == R.id.nav_color) {
      fab.setVisibility(View.VISIBLE);
      coloring_fragment = new ColoringFragment();
      fab.setOnClickListener((v) -> coloring_fragment.switchBackground());
      fragment = coloring_fragment;
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
          .commit();
    } else if (id == R.id.nav_save) {

    } else if (id == R.id.nav_share) {
      fragment.shareImage();

    }


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  /**
   * Paint clicked.
   *
   * @param view the view
   */
  public void paintClicked(View view) {
    ((PaintClickable) fragment).paintClicked(view);
  }

  /**
   * The interface Paint clickable.
   */
  public interface PaintClickable {

    /**
     * Paint clicked.
     *
     * @param view the view
     */
    void paintClicked(View view);
  }


  private void signOut() {
    ColorApplication app = ColorApplication.getInstance();
    app.getClient().signOut().addOnCompleteListener(this, (task) -> {
      app.setAccount(null);
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    });
  }

}
