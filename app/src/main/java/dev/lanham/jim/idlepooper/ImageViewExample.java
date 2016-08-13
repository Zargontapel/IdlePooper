package dev.lanham.jim.idlepooper;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewExample extends Activity implements OnClickListener {

    /** Called when the activity is first created. */

    int image_index = 0;
    private static final int MAX_IMAGE_COUNT = 3;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private Integer[] mImageIds = {
            R.raw.image1,
            R.raw.image2,
            R.raw.image3
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnPrevious = (Button)findViewById(R.id.previous_btn);
        btnPrevious.setOnClickListener(this);
        Button btnNext = (Button)findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);

        showImage();

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        //options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = 8;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private void showImage() {

        ImageView imgView = (ImageView) findViewById(R.id.myimage);

        //set our options to subsample the resource at an 8:1 ratio
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap original_bitmap=BitmapFactory.decodeResource(getResources(),mImageIds[image_index],options);

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        //rotate the bitmap so it is upright
        Bitmap rotatedBitmap = Bitmap.createBitmap(original_bitmap, 0, 0, original_bitmap.getWidth(), original_bitmap.getHeight(), matrix, true);

        imgView.setImageBitmap(rotatedBitmap);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case (R.id.previous_btn):

                image_index--;

                if (image_index == -1) {
                    image_index = MAX_IMAGE_COUNT - 1;
                }

                showImage();

                break;

            case (R.id.next_btn):

                image_index++;

                if (image_index == MAX_IMAGE_COUNT) {
                    image_index = 0;
                }

                showImage();

                break;
        }
    }
}
