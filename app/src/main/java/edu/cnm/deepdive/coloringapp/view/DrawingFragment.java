package edu.cnm.deepdive.coloringapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Path;
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

  private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 12;
  /**
   * The Draw view.
   */
  protected DrawingView drawView;
  private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
  private float smallBrush, mediumBrush, largeBrush;
  private byte[] mapImage;
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
    smallBrush = getResources().getInteger(R.integer.small_size);
    mediumBrush = getResources().getInteger(R.integer.medium_size);
    largeBrush = getResources().getInteger(R.integer.large_size);
    drawBtn = (ImageButton) view.findViewById(R.id.draw_btn);
    drawBtn.setOnClickListener(this);
    drawView.setBrushSize(mediumBrush);
    eraseBtn = (ImageButton) view.findViewById(R.id.erase_btn);
    eraseBtn.setOnClickListener(this);
    newBtn = (ImageButton) view.findViewById(R.id.new_btn);
    newBtn.setOnClickListener(this);
    saveBtn = (ImageButton) view.findViewById(R.id.save_btn);
    saveBtn.setOnClickListener(this);

    return view;
  }

  public void paintClicked(View view) {
    drawView.setErase(false);

    if (view != currPaint) {
      ImageButton imgView = (ImageButton) view;
      String color = view.getTag().toString();
      drawView.setColor(color);
      imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
      currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
      currPaint = (ImageButton) view;
    }
    drawView.setBrushSize(drawView.getLastBrushSize());
  }

  @Override
  public void onClick(View view) {
//respond to clicks
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
      smallBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          drawView.setErase(true);
          drawView.setBrushSize(smallBrush);
          brushDialog.dismiss();
        }
      });
      ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
      mediumBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          drawView.setErase(true);
          drawView.setBrushSize(mediumBrush);
          brushDialog.dismiss();
        }
      });
      ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
      largeBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          drawView.setErase(true);
          drawView.setBrushSize(largeBrush);
          brushDialog.dismiss();
        }
      });
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
      int externalPermissionCheck = ContextCompat.checkSelfPermission(getActivity(),
          Manifest.permission.WRITE_EXTERNAL_STORAGE);
      if (externalPermissionCheck == -1) {
        askPermissionStorage();
      } else {
        saveCurrentImage();
      }
    }

  }

  private void saveCurrentImage() {
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

  private void askPermissionStorage() {
    //for media
    if (ContextCompat
        .checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.
            WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      requestPermissions(new
              String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
          MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

    }

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        saveCurrentImage();
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

/*  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putByteArray(mapImage);
    super.onSaveInstanceState(outState);
  }*/
}

