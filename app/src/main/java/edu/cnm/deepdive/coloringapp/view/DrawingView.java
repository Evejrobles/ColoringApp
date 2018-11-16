package edu.cnm.deepdive.coloringapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import edu.cnm.deepdive.coloringapp.R;

/**
 * The type Drawing view.
 */
public class DrawingView extends View {

  private Path drawPath;
  private Paint drawPaint, canvasPaint;
  private int paintColor = 0xFF660000;
  private Canvas drawCanvas;
  private Bitmap canvasBitmap;
  private float brushSize, lastBrushSize;
  private boolean erase = false;


  /**
   * Instantiates a new Drawing view.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public DrawingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setupDrawing();
  }

  private void setupDrawing() {
    brushSize = getResources().getInteger(R.integer.medium_size);
    lastBrushSize = brushSize;
    if (drawPath == null) {
      drawPath = new Path();
    }
    drawPaint = new Paint();
    drawPaint.setColor(paintColor);
    drawPaint.setColor(Color.TRANSPARENT);

    drawPaint.setAntiAlias(true);
    drawPaint.setStrokeWidth(brushSize);
    drawPaint.setStyle(Paint.Style.STROKE);
    drawPaint.setStrokeJoin(Paint.Join.ROUND);
    drawPaint.setStrokeCap(Paint.Cap.ROUND);
    canvasPaint = new Paint(Paint.DITHER_FLAG);

  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    super.onSizeChanged(w, h, oldw, oldh);
    if (canvasBitmap == null) {
      canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }
    if (drawPath == null) {
      drawPath = new Path();
    }
    drawCanvas = new Canvas(canvasBitmap);
  }

  @Override
  protected void onDraw(Canvas canvas) {

    canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    canvas.drawPath(drawPath, drawPaint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    float touchX = event.getX();
    float touchY = event.getY();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        drawPath.moveTo(touchX, touchY);
        break;
      case MotionEvent.ACTION_MOVE:
        drawPath.lineTo(touchX, touchY);
        break;
      case MotionEvent.ACTION_UP:
        drawCanvas.drawPath(drawPath, drawPaint);
        drawPath.reset();
        break;
      default:
        return false;
    }
    invalidate();
    return true;
  }

  /**
   * Set color.
   *
   * @param newColor the new color
   */
  public void setColor(String newColor) {
    invalidate();
    paintColor = Color.parseColor(newColor);
    drawPaint.setColor(paintColor);
  }

  /**
   * Set brush size.
   *
   * @param newSize the new size
   */
  public void setBrushSize(float newSize) {
//update size
    float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        newSize, getResources().getDisplayMetrics());
    brushSize = pixelAmount;
    drawPaint.setStrokeWidth(brushSize);

  }

  /**
   * Set last brush size.
   *
   * @param lastSize the last size
   */
  public void setLastBrushSize(float lastSize) {
    lastBrushSize = lastSize;
  }

  /**
   * Get last brush size float.
   *
   * @return the float
   */
  public float getLastBrushSize() {
    return lastBrushSize;
  }

  /**
   * Set erase.
   *
   * @param isErase the is erase
   */
  public void setErase(boolean isErase) {
    erase = isErase;
    if (erase) {
      drawPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    } else {
      drawPaint.setXfermode(null);
    }
  }

  /**
   * Start new.
   */
  public void startNew() {
    drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    invalidate();
  }

  /**
   * Gets canvas bitmap.
   *
   * @return the canvas bitmap
   */
  public Bitmap getCanvasBitmap() {
    return canvasBitmap;
  }

  /**
   * Sets canvas bitmap.
   *
   * @param canvasBitmap the canvas bitmap
   */
  public void setCanvasBitmap(Bitmap canvasBitmap) {
    this.canvasBitmap = canvasBitmap;
  }

  /**
   * Gets draw path.
   *
   * @return the draw path
   */
  public Path getDrawPath() {
    return drawPath;
  }

  /**
   * Sets draw path.
   *
   * @param drawPath the draw path
   */
  public void setDrawPath(Path drawPath) {
    this.drawPath = drawPath;
  }
}
