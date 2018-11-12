package edu.cnm.deepdive.coloringapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ColoringEntity {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "coloring_id")
  private long id;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
