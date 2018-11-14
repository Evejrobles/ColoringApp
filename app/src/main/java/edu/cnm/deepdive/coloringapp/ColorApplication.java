package edu.cnm.deepdive.coloringapp;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.coloringapp.model.db.ColoringAppDatabase;


/**
 * The type Color application.
 */
public class ColorApplication extends Application {

  private ColoringAppDatabase database;

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    ColoringAppDatabase.getInstance(this);
  }

  @Override
  public void onTerminate() {
    ColoringAppDatabase.forgetInstance();
    super.onTerminate();
  }
}
