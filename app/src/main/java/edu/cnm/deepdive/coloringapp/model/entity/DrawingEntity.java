package edu.cnm.deepdive.coloringapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * The type Drawing entity.
 */
@Entity
public class DrawingEntity {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "drawing_id")
  private long id;

  private String fileName;

  /**
   * Gets id.
   *
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets file name.
   *
   * @return the file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Sets file name.
   *
   * @param fileName the file name
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}


