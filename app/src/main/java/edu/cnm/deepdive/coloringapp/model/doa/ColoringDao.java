package edu.cnm.deepdive.coloringapp.model.doa;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.coloringapp.model.entity.ColoringBackgroundEntity;
import java.util.List;

/**
 * The interface Coloring dao.
 */
@Dao
public interface ColoringDao {

  @Insert
  long insert(ColoringBackgroundEntity coloringBackgroundEntity);

  @Query("SELECT * FROM ColoringBackgroundEntity")
  List<ColoringBackgroundEntity> select();

}
