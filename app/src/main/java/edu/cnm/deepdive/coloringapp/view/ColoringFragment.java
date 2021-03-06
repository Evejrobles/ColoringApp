package edu.cnm.deepdive.coloringapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import edu.cnm.deepdive.coloringapp.model.db.ColoringAppDatabase;
import edu.cnm.deepdive.coloringapp.model.entity.ColoringBackgroundEntity;
import java.util.List;


/**
 * The type Coloring fragment.
 */
public class ColoringFragment extends DrawingFragment implements OnTouchListener {

  private List<ColoringBackgroundEntity> coloringBackGrounds;
  private int currentBackGround;
  private boolean hasDrawn = false;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    new ColoringInsertTask().execute();

    drawView.setOnTouchListener(this);

    return view;
  }

  /**
   * Switch background.
   */
  public void switchBackground() {

    if (hasDrawn) {
      AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
      newDialog.setTitle("New drawing");
      newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
      newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

          drawView.startNew();
          String fileName = coloringBackGrounds.get(currentBackGround).getFileName();
          drawView.setBackground(getActivity().getDrawable(getActivity().getResources()
              .getIdentifier(fileName, "drawable", getActivity().getPackageName())));
          currentBackGround++;
          if (currentBackGround > coloringBackGrounds.size() - 1) {
            currentBackGround = 0;

          }
          hasDrawn = false;
          dialog.dismiss();
        }
      });
      newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });

      newDialog.show();


    } else {
      drawView.startNew();
      String fileName = coloringBackGrounds.get(currentBackGround).getFileName();
      drawView.setBackground(getActivity().getDrawable(getActivity().getResources()
          .getIdentifier(fileName, "drawable", getActivity().getPackageName())));
      currentBackGround++;
      if (currentBackGround > coloringBackGrounds.size() - 1) {
        currentBackGround = 0;

      }


    }

  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    hasDrawn = true;
    return false;
  }

  private class ColoringInsertTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
      int count = ColoringAppDatabase.getInstance(getActivity()).getColoringDao().select().size();
      if (count == 0) {
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("balloon");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("flower");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("elephant");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("hauntedhouse");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("hummingbird");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("r2d2");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("sponge_bob");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("star_wars");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("sugar_skull");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
        {
          ColoringBackgroundEntity coloringBackgroundEntity = new ColoringBackgroundEntity();
          coloringBackgroundEntity.setFileName("yoda");
          ColoringAppDatabase.getInstance(getActivity()).getColoringDao()
              .insert(coloringBackgroundEntity);
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      new ColoringTask().execute();

    }
  }


  private class ColoringTask extends AsyncTask<Void, Void, List<ColoringBackgroundEntity>> {

    @Override
    protected List<ColoringBackgroundEntity> doInBackground(Void... voids) {
      return ColoringAppDatabase.getInstance(getActivity()).getColoringDao().select();
    }

    @Override
    protected void onPostExecute(List<ColoringBackgroundEntity> coloringBackgroundEntities) {
      ColoringFragment.this.coloringBackGrounds = coloringBackgroundEntities;
      switchBackground();
    }
  }
}

