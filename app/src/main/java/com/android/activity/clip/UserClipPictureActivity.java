package com.android.activity.clip;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.alan.common.AndroidTools;
import com.alan.common.UriTools;
import com.android.tools.R;
import com.android.tools.base.BaseActivity;

import java.io.File;
import java.io.IOException;

/**
 * 用户裁剪头像
 *
 * @author huangxiaotao
 */
public class UserClipPictureActivity extends BaseActivity implements OnClickListener {
    private ClipView mClipView;
    private int mClipStyle = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_clip_picture_layout);
        initView();
    }

    private void initBitmap() {
        Uri imgUri = getIntent().getParcelableExtra("user_uri");
        String imgPath = getIntent().getStringExtra("user_img_path");
        int degree = 0;
        if (!TextUtils.isEmpty(imgPath)) {
            degree = getBitmapDegree(imgPath);
        }
        Rect clipRect = initClipFrame();
        if (imgPath != null && checkFilePath(imgPath)) {
            mClipView.setImage(imgPath, clipRect, mClipStyle, degree);
        } else if (imgUri != null) {
            mClipView.setImage(imgUri, clipRect, mClipStyle, degree);
        } else {
            this.finish();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private boolean checkFilePath(String imgPath) {
        try {
            File f = new File(imgPath);
            if (f.exists())
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    private Rect initClipFrame() {
        int top = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.pic_clip_frame_top_margin);
        int[] screenSize = AndroidTools.getScreenSize(this);
        int screenWidth = screenSize[0];
        Rect clipRect = new Rect(0, top, screenWidth, top + screenWidth);
        View clip_frame = findViewById(R.id.clip_frame);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) clip_frame.getLayoutParams();
        params.height = screenWidth;
        params.width = screenWidth;
        clip_frame.setLayoutParams(params);
        return clipRect;
    }

    @Override
    protected void onDestroy() {
        mClipView.onDestroy();
        super.onDestroy();
    }

    private void clipBitmap() {
        String imgName = mClipView.getBitmap();
        Intent intent = new Intent();
        intent.putExtra("pic_name", imgName);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.sure) {
            clipBitmap();
        } else if (v.getId() == R.id.cancel) {
            finish();
        } else if (v.getId() == R.id.rotate_left) {
            mClipView.rotate(-90.0f);
        } else if (v.getId() == R.id.rotate_right) {
            mClipView.rotate(90.0f);
        }

    }

    protected void initView() {
        mClipView = findViewById(R.id.clipview);
        findViewById(R.id.rotate_left).setOnClickListener(this);
        findViewById(R.id.rotate_right).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        initBitmap();
    }

    public static void startClipPicActivity(Activity activity, Uri uri, int requet) {
        Intent intent = new Intent(activity, UserClipPictureActivity.class);

        String imgPath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            imgPath = UriTools.getPath(activity, uri);
        } else {
            imgPath = UriTools.getFilePathFromIntentData(uri, activity);
        }

        if (imgPath != null) {
            intent.putExtra("user_img_path", imgPath);
        } else if (uri != null) {
            intent.putExtra("user_uri", uri);
        }
        activity.startActivityForResult(intent, requet);
    }

}