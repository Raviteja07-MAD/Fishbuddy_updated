package com.fishbuddy.customfonts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCommpression {


    public static String compressImage(String filePath) {


        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();



        options.inJustDecodeBounds = true;

        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;

        int actualWidth = options.outWidth;


       /* float maxHeight = 400.0f;
        float maxWidth = 300.0f;*/

        float maxHeight = 800.0f;
        float maxWidth = 700.0f;

        float imgRatio = actualWidth / actualHeight;

        float maxRatio = maxWidth / maxHeight;



        if (actualHeight > maxHeight || actualWidth > maxWidth)
        {

            if (imgRatio < maxRatio) {

                imgRatio = maxHeight / actualHeight;

                actualWidth = (int) (imgRatio * actualWidth);

                actualHeight = (int) maxHeight;

            } else if (imgRatio > maxRatio) {

                imgRatio = maxWidth / actualWidth;

                actualHeight = (int) (imgRatio * actualHeight);

                actualWidth = (int) maxWidth;

            } else {

                actualHeight = (int) maxHeight;

                actualWidth = (int) maxWidth;

            }
        }



        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);


        //      inJustDecodeBounds set to false to load the actual bitmap

        options.inJustDecodeBounds = false;


        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;

        options.inInputShareable = true;

        options.inTempStorage = new byte[16 * 1024];


        try {

            //          load the bitmap from its path

            bmp = BitmapFactory.decodeFile(filePath, options);

        } catch (OutOfMemoryError exception) {

            exception.printStackTrace();

        }
        try {

            scaledBitmap = Bitmap.createBitmap(actualWidth,
                    actualHeight,Bitmap.Config.ARGB_8888);

        } catch (OutOfMemoryError exception) {

            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;

        float ratioY = actualHeight / (float) options.outHeight;

        float middleX = actualWidth / 2.0f;

        float middleY = actualHeight / 2.0f;


        Matrix scaleMatrix = new Matrix();

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);



        Canvas canvas = new Canvas(scaledBitmap);

        canvas.setMatrix(scaleMatrix);

        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        //      check the rotation of the image and display it properly

        ExifInterface exif;

        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);

            Log.d("EXIF", "Exif: " + orientation);

            Matrix matrix = new Matrix();

            if (orientation == 6) {

                matrix.postRotate(90);

                Log.d("EXIF", "Exif: " + orientation);

            } else if (orientation == 3) {

                matrix.postRotate(180);

                Log.d("EXIF", "Exif: " + orientation);

            } else if (orientation == 8) {

                matrix.postRotate(270);

                Log.d("EXIF", "Exif: " + orientation);

            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

        } catch (IOException e) {

            e.printStackTrace();
        }

        FileOutputStream out = null;

        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);


            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);


        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Foldername/Images");

        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;

        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height/ (float)
                    reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }       final float totalPixels = width * height;

        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Bitmap highlightImage(Bitmap src) {

        // create new bitmap, which will be painted and becomes result image

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Bitmap.Config.ARGB_8888);

        // setup canvas for painting

        Canvas canvas = new Canvas(bmOut);

        // setup default color

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // create a blur paint for capturing alpha

        Paint ptBlur = new Paint();

        ptBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

        int[] offsetXY = new int[2];

        // capture alpha into a bitmap

        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);

        // create a color paint
        Paint ptAlphaColor = new Paint();

        ptAlphaColor.setColor(0xFFFFFFFF);

        // paint color for captured alpha region (bitmap)

        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);

        // free memory

        bmAlpha.recycle();

        // paint the image source
        canvas.drawBitmap(src, 0, 0, null);

        // return out final image
        return bmOut;
    }
}
