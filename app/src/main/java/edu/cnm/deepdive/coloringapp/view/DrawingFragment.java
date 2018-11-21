package edu.cnm.deepdive.coloringapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import edu.cnm.deepdive.coloringapp.R;
import edu.cnm.deepdive.coloringapp.controler.MainActivity.PaintClickable;
import java.util.UUID;


/**
 * The type Drawing fragment.
 */
public class DrawingFragment extends Fragment implements OnClickListener, PaintClickable {

  private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
  private static final int MY_PERMISSIONS_REQUEST_SHARE = 2;

  /**
   * The Draw view.
   */
  protected DrawingView drawView;
  private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
  private float smallBrush, mediumBrush, largeBrush;
  private DrawingViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this).get(DrawingViewModel.class);

    View view = inflater.inflate(R.layout.draw_activity, container, false);

    drawView = (DrawingView) view.findViewById(R.id.drawing);
    drawView.setCanvasBitmap(viewModel.bitmap);
    drawView.setDrawPath(viewModel.path);

    LinearLayout paintLayout = (LinearLayout) view.findViewById(R.id.paint_colors);

    currPaint = (ImageButton) paintLayout.getChildAt(0);
    currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
    String color = "#AF660000";
    drawView.setColor(color);
    smallBrush = getResources().getInteger(R.integer.small_size);
    mediumBrush = getResources().getInteger(R.integer.medium_size);
    largeBrush = getResources().getInteger(R.integer.large_size);
    drawBtn = (ImageButton) view.findViewById(R.id.draw_btn);
    drawBtn.setOnClickListener(this);
    drawView.setBrushSize(getFromSharedPrefs());
    eraseBtn = (ImageButton) view.findViewById(R.id.erase_btn);
    eraseBtn.setOnClickListener(this);
    newBtn = (ImageButton) view.findViewById(R.id.new_btn);
    newBtn.setOnClickListener(this);
    saveBtn = (ImageButton) view.findViewById(R.id.save_btn);
    saveBtn.setOnClickListener(this);

    return view;
  }

  /**
   * Paint Clicked
   *
   * @param view the view
   */
  public void paintClicked(View view) {
    drawView.setErase(false);

    if (view != currPaint) {
      ImageButton imgView = (ImageButton) view;
      String color = view.getTag().toString();
      drawView.setColor(color);
      imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
      currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
      currPaint = (ImageButton) view;
      //hasDrawn = true;

    }
    drawView.setBrushSize(drawView.getLastBrushSize());
  }

  @Override
  public void onClick(View view) {

    if (view.getId() == R.id.draw_btn) {
      final Dialog brushDialog = new Dialog(getActivity());
      brushDialog.setTitle("Brush size:");
      brushDialog.setContentView(R.layout.brush_chooser);
      ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
      setBrushListener(brushDialog, smallBtn, smallBrush);
      ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
      setBrushListener(brushDialog, mediumBtn, mediumBrush);

      ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
      setBrushListener(brushDialog, largeBtn, largeBrush);
      brushDialog.show();

    } else if (view.getId() == R.id.erase_btn) {
      final Dialog brushDialog = new Dialog(getActivity());
      brushDialog.setTitle("Eraser size:");
      brushDialog.setContentView(R.layout.brush_chooser);
      ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
      setBrushSizeListener(brushDialog, smallBtn, smallBrush);
      ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
      setBrushSizeListener(brushDialog, mediumBtn, mediumBrush);
      ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
      setBrushSizeListener(brushDialog, largeBtn, largeBrush);
      brushDialog.show();
    } else if (view.getId() == R.id.new_btn) {
      AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
      newDialog.setTitle("New drawing");
      newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
      newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          drawView.startNew();
          dialog.dismiss();

        }
      });
      newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });
      newDialog.show();
    } else if (view.getId() == R.id.save_btn) {
      saveImage(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

  }

  private void setBrushSizeListener(Dialog brushDialog, ImageButton button, float brushSize) {
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        drawView.setErase(true);
        drawView.setBrushSize(brushSize);
        saveToSharedPrefs(brushSize);
        brushDialog.dismiss();
      }
    });
  }

  private void saveImage(int code) {
    int externalPermissionCheck = ContextCompat.checkSelfPermission(getActivity(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if (externalPermissionCheck == -1) {
      askPermissionStorage(code);
    } else {
      Uri uriToImage = Uri.parse(saveCurrentImage());
      if (code == MY_PERMISSIONS_REQUEST_SHARE) {
        shareFromUri(uriToImage);
      }
    }
  }

  private void shareFromUri(Uri uriToImage) {
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
    shareIntent.setType("image/jpeg");
    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
  }

  /**
   * Share image.
   */
  public void shareImage() {
    saveImage(MY_PERMISSIONS_REQUEST_SHARE);
  }

  private String saveCurrentImage() {
    drawView.setDrawingCacheEnabled(true);
    String imgSaved = MediaStore.Images.Media.insertImage(
        getActivity().getContentResolver(), drawView.getDrawingCache(),
        UUID.randomUUID().toString() + ".png", "drawing");
    if (imgSaved != null) {
      Toast savedToast = Toast.makeText(getActivity().getApplicationContext(),
          "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
      savedToast.show();
    } else {
      Toast unsavedToast = Toast.makeText(getActivity().getApplicationContext(),
          "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
      unsavedToast.show();
    }
    drawView.destroyDrawingCache();
    return imgSaved;
  }

  private void setBrushListener(final Dialog brushDialog, ImageButton button,
      final float brush) {
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        drawView.setBrushSize(brush);
        drawView.setLastBrushSize(brush);
        drawView.setErase(false);
        brushDialog.dismiss();
      }
    });
  }

  private void askPermissionStorage(int code) {
    //for media
    if (ContextCompat
        .checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.
            WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      requestPermissions(new
              String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
          code);

    }

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
        || requestCode == MY_PERMISSIONS_REQUEST_SHARE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Uri uriToImage = Uri.parse(saveCurrentImage());
        if (requestCode == MY_PERMISSIONS_REQUEST_SHARE) {
          shareFromUri(uriToImage);
        }
      } else {
        Toast unsavedToast = Toast.makeText(getActivity().getApplicationContext(),
            "Oops! Need permission to save image", Toast.LENGTH_SHORT);
        unsavedToast.show();

      }
    }
  }


  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    viewModel.bitmap = drawView.getCanvasBitmap();
    viewModel.path = drawView.getDrawPath();
    super.onSaveInstanceState(outState);
  }

  /**
   * The type Drawing view model.
   */
  public static class DrawingViewModel extends ViewModel {

    /**
     * The Bitmap.
     */
    public Bitmap bitmap;
    /**
     * The Path.
     */
    public Path path;
  }

  private void saveToSharedPrefs(float bs) {
    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putFloat(getString(R.string.string_key), bs);

    editor.apply();
  }

  private Float getFromSharedPrefs() {
    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    return sharedPreferences.getFloat(getString(R.string.string_key), smallBrush);
  }
}

