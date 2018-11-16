package edu.cnm.deepdive.coloringapp;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import edu.cnm.deepdive.coloringapp.model.db.ColoringAppDatabase;


/**
 * The type Color application.
 */
public class ColorApplication extends Application {

  private ColoringAppDatabase database;
  private GoogleSignInClient client;
  private GoogleSignInAccount account;
  private static ColorApplication instance;


  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static ColorApplication getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    Stetho.initializeWithDefaults(this);
    ColoringAppDatabase.getInstance(this);
    GoogleSignInOptions options = new GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestId()
        //Include requestIdToken if we're using Google Sign-in for authenticating on back end.
        .build();
    client = GoogleSignIn.getClient(this, options);
  }

  @Override
  public void onTerminate() {
    ColoringAppDatabase.forgetInstance();
    super.onTerminate();
  }

  /**
   * Gets client.
   *
   * @return the client
   */
  public GoogleSignInClient getClient() {
    return client;
  }

  /**
   * Sets client.
   *
   * @param client the client
   */
  public void setClient(GoogleSignInClient client) {
    this.client = client;
  }

  /**
   * Gets account.
   *
   * @return the account
   */
  public GoogleSignInAccount getAccount() {
    return account;
  }

  /**
   * Sets account.
   *
   * @param account the account
   */
  public void setAccount(GoogleSignInAccount account) {
    this.account = account;
  }
}
