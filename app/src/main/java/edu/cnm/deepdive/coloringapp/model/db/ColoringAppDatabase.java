package edu.cnm.deepdive.coloringapp.model.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import edu.cnm.deepdive.coloringapp.model.doa.ColoringDao;
import edu.cnm.deepdive.coloringapp.model.doa.DrawingDao;
import edu.cnm.deepdive.coloringapp.model.entity.ColoringBackgroundEntity;
import edu.cnm.deepdive.coloringapp.model.entity.DrawingEntity;
import java.util.Date;

/**
 * The type Coloring app database.
 */
@Database(
    entities = {DrawingEntity.class, ColoringBackgroundEntity.class},
    version = 1,
    exportSchema = true
)
@TypeConverters(ColoringAppDatabase.Converters.class)
public abstract class ColoringAppDatabase extends RoomDatabase {

  private static final String DB_NAME = "coloring_app_db";
  private static ColoringAppDatabase instance = null;

  /**
   * Gets instance.
   *
   * @param context the context
   * @return the instance
   */
  public synchronized static ColoringAppDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room
          .databaseBuilder(context.getApplicationContext(), ColoringAppDatabase.class, DB_NAME)
          .build();
    }
    return instance;
  }

  /**
   * Forget instance.
   */
  public synchronized static void forgetInstance() {
    instance = null;
  }

  /**
   * Gets coloringt dao.
   *
   * @return the coloringt dao
   */
  public abstract ColoringDao getColoringtDao();

  /**
   * Gets drawing dao.
   *
   * @return the drawing dao
   */
  public abstract DrawingDao getDrawingDao();


  /**
   * The type Converters.
   */
  public static class Converters {

    /**
     * Date from long date.
     *
     * @param time the time
     * @return the date
     */
    @TypeConverter
    public static Date dateFromLong(Long time) {
      return (time != null) ? new Date(time) : null;
    }

    /**
     * Long from date long.
     *
     * @param date the date
     * @return the long
     */
    @TypeConverter
    public static long longFromDate(Date date) {
      return (date != null) ? date.getTime() : null;
    }


  }


}
