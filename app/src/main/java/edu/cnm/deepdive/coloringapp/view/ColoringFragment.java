package edu.cnm.deepdive.coloringapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


public class ColoringFragment extends Fragment implements OnClickListener, PaintClickable {

  private DrawingView drawView;
  private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
  private float smallBrush, mediumBrush, largeBrush;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.draw_activity, container, false);

    drawView = (DrawingView) view.findViewById(R.id.drawing);
    //drawView.setBackground(getActivity().getDrawable(R.drawable.));
    LinearLayout paintLayout = (LinearLayout) view.findViewById(R.id.paint_colors);

    currPaint = (ImageButton) paintLayout.getChildAt(0);
    currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
    smallBrush = getResources().getInteger(R.integer.small_size);
    mediumBrush = getResources().getInteger(R.integer.medium_size);
    largeBrush = getResources().getInteger(R.integer.large_size);
    drawBtn = (ImageButton) view.findViewById(R.id.draw_btn);
    drawBtn.setOnClickListener(this);
    drawView.setBrushSize(mediumBrush);
    eraseBtn = (ImageButton)view.findViewById(R.id.erase_btn);
    eraseBtn.setOnClickListener(this);
    newBtn = (ImageButton)view.findViewById(R.id.new_btn);
    newBtn.setOnClickListener(this);
    saveBtn = (ImageButton)view.findViewById(R.id.save_btn);
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
      smallBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          drawView.setBrushSize(smallBrush);
          drawView.setLastBrushSize(smallBrush);
          drawView.setErase(false);
          brushDialog.dismiss();
        }
      });
      ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
      mediumBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          drawView.setBrushSize(mediumBrush);
          drawView.setLastBrushSize(mediumBrush);
          drawView.setErase(false);
          brushDialog.dismiss();
        }
      });

      ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
      largeBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          drawView.setBrushSize(largeBrush);
          drawView.setLastBrushSize(largeBrush);
          drawView.setErase(false);
          brushDialog.dismiss();
        }
      });
      brushDialog.show();

    }else if(view.getId()==R.id.erase_btn){
      final Dialog brushDialog = new Dialog(getActivity());
      brushDialog.setTitle("Eraser size:");
      brushDialog.setContentView(R.layout.brush_chooser);
      ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
      smallBtn.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View v) {
          drawView.setErase(true);
          drawView.setBrushSize(smallBrush);
          brushDialog.dismiss();
        }
      });
      ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
      mediumBtn.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View v) {
          drawView.setErase(true);
          drawView.setBrushSize(mediumBrush);
          brushDialog.dismiss();
        }
      });
      ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
      largeBtn.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View v) {
          drawView.setErase(true);
          drawView.setBrushSize(largeBrush);
          brushDialog.dismiss();
        }
      });
      brushDialog.show();
    }
    else if(view.getId()==R.id.new_btn){
      AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
      newDialog.setTitle("New drawing");
      newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
      newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
          drawView.startNew();
          dialog.dismiss();
        }
      });
      newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
          dialog.cancel();
        }
      });
      newDialog.show();
    }
    else if(view.getId()==R.id.save_btn){
      AlertDialog.Builder saveDialog = new AlertDialog.Builder(getActivity());
      saveDialog.setTitle("Save drawing");
      saveDialog.setMessage("Save drawing to device Gallery?");
      saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
          //save drawing
        }
      });
      saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
          dialog.cancel();
        }
      });
      saveDialog.show();
      drawView.setDrawingCacheEnabled(true);
      String imgSaved = MediaStore.Images.Media.insertImage(
          getActivity().getContentResolver(), drawView.getDrawingCache(),
          UUID.randomUUID().toString()+".png", "drawing");
      if(imgSaved!=null){
        Toast savedToast = Toast.makeText(getActivity().getApplicationContext(),
            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
        savedToast.show();
      }
      else{
        Toast unsavedToast = Toast.makeText(getActivity().getApplicationContext(),
            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
        unsavedToast.show();
      }
      drawView.destroyDrawingCache();
    }

  }

}

