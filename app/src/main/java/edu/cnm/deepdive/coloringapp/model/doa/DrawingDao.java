package edu.cnm.deepdive.coloringapp.model.doa;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.coloringapp.model.entity.DrawingEntity;
import java.util.List;

/**
 * The interface Drawing dao.
 */
@Dao
public interface DrawingDao {

  @Insert
  long insert(DrawingEntity drawingEntity);

  @Query("SELECT * FROM drawingentity")
  List<DrawingEntity> select();

}
