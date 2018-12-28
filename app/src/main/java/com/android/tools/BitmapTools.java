package com.android.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BitmapTools {


    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap getBitmapFromPath(String pathName, Activity activity) {
        try {
            int[] screenSize = AndroidTools.getScreenSize(activity);
            int screenWidth = screenSize[0];
            int screenHeight = screenSize[1];
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            if (options.outWidth > screenWidth
                    && options.outHeight > screenHeight) {
                options.inSampleSize = computeSampleSize(options, -1,
                        screenWidth * screenHeight);
            }
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(pathName, options);
            return bitmap;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }


    public static int[] getBitmapSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Logger.d("aaaaa-----" + path);
        BitmapFactory.decodeFile(path, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    public static boolean save(Bitmap bitmap, String path) {
        if (bitmap == null || path == null)
            return false;
        OutputStream out = null;
        try {
            out = new FileOutputStream(path);
            CompressFormat format = null;
            String ext = FileTools.getFileExt(path);
            if (ext != null) {
                if (ext.equals("jpg")) {
                    format = CompressFormat.JPEG;
                } else if (ext.equals("png")) {
                    format = CompressFormat.PNG;
                }
            }
            if (format == null) {
                format = CompressFormat.PNG;
            }
            return bitmap.compress(format, 100, out);
        } catch (FileNotFoundException e) {
        } catch (Throwable t) {

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }


    // 计算图片的缩放�??
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        boolean isOptionWidthLarger = options.outWidth > options.outHeight;
        boolean isReqWidthLarger = reqWidth > reqHeight;
        final int height = isOptionWidthLarger ? options.outHeight
                : options.outWidth;
        final int width = isOptionWidthLarger ? options.outWidth
                : options.outHeight;
        int reqW = isReqWidthLarger ? reqWidth : reqHeight;
        int reqH = isReqWidthLarger ? reqHeight : reqWidth;
        int inSampleSize = 1;

        if (height > reqH || width > reqW) {
            final int heightRatio = Math.round((float) height / (float) reqH);
            final int widthRatio = Math.round((float) width / (float) reqW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //按比例缩放
    public static Bitmap scaleBitmap(Bitmap origin, float scale) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(scale, scale);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    public static String shot(Bitmap bitmap, String filePath) {
        if (bitmap != null) {
            try {
                File file = new File(filePath);
                if (file.exists()) file.delete();
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                bitmap = null;
                return filePath;
            } catch (Exception e) {
            }
        }
        return "";
    }

    public static Bitmap getViewBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        if (width > 720) {
            float size = 720f / width;
            return scaleBitmap(bitmap, size);
        }
        return bitmap;

    }

    public static Bitmap[] getViewBitmaps(View view) {
        try {
            int w = view.getWidth();
            int h = view.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, (int) (w / 2f), h);
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, (int) (w / 2f), 0, (int) (w / 2f), h);


            return new Bitmap[]{bitmap1, bitmap2};
        } catch (Exception ignored) {
        } catch (Error ignored) {
        }
        return null;
    }

    public static void recycle(Bitmap... bitmap) {
        if (null == bitmap || bitmap.length == 0) return;
        for (Bitmap b : bitmap) {
            if (null != b && !b.isRecycled()) {
                b.recycle();
                b = null;
            }
        }
    }

    /**
     * @param maxSize //图片允许最大空间   单位：KB
     */
    public static Bitmap imageZoom(Bitmap bitMap, double maxSize) {
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
        return bitMap;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     */
    private static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                    double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
    }

    public static Bitmap decodeFromResourceResize(Context context, int res, int width, int height) {
        Bitmap bitmapFromRes = getBitmapFromRes(context, res);
        Bitmap bitmap = zoomImage(bitmapFromRes, width, height);
        recycle(bitmapFromRes);
        return bitmap;
    }

    public static Bitmap getBitmapFromRes(Context context, int res) {
        return BitmapFactory.decodeResource(context.getResources(), res);
    }

    public static Drawable getDrawableFromRes(Context context, int res) {
        return context.getResources().getDrawable(res);
    }


}
