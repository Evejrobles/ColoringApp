package edu.cnm.deepdive.coloringapp.model.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import edu.cnm.deepdive.coloringapp.model.doa.ColoringDao;
import edu.cnm.deepdive.coloringapp.model.doa.DrawingDao;
import edu.cnm.deepdive.coloringapp.model.entity.ColoringEntity;
import edu.cnm.deepdive.coloringapp.model.entity.DrawingEntity;
import java.util.Date;

@Database(
    entities = {DrawingEntity.class, ColoringEntity.class},
    version = 1,
    exportSchema = true
)
@TypeConverters(ColoringAppDatabase.Converters.class)
public abstract class ColoringAppDatabase extends RoomDatabase {

  private static final String DB_NAME = "coloring_app_db";
  private static ColoringAppDatabase instance = null;

  public synchronized static ColoringAppDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room
          .databaseBuilder(context.getApplicationContext(), ColoringAppDatabase.class, DB_NAME)
          .build();
    }
    return instance;
  }

  public synchronized static void forgetInstance() {
    instance = null;
  }

  public abstract ColoringDao getColoringtDao();

  public abstract DrawingDao getDrawingDao();



  public static class Converters {

    @TypeConverter
    public static Date dateFromLong(Long time) {
      return (time != null) ? new Date(time) : null;
    }

    @TypeConverter
    public static long longFromDate(Date date) {
      return (date != null) ? date.getTime() : null;
    }


  }


}
