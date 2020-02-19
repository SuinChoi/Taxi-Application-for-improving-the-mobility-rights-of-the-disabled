
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private SurfaceView cameraSurfaceView;
    private SurfaceView topSurfaceView;
    private Camera camera;
    private boolean allowTakePicture = false; // 중복사진 방지

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ActionBar ㅈ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 전체화면 설정
        setContentView(R.layout.activity_main); 



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||

                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||

                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);

        }



        // 카메라사용 SurfaceView

        cameraSurfaceView = findViewById(R.id.surfaceview);

        SurfaceHolder holder = cameraSurfaceView.getHolder();

        holder.setFixedSize(Utility.WidthPixel, Utility.HeightPixel);// 해상도 설정

        holder.setKeepScreenOn(true);

        holder.addCallback(new SurfaceHolder.Callback() { // SurfaceView只有当activity显示到了前台，该控件才会被创建。因此需要监听surfaceview的创建

            @Override

            public void surfaceCreated(SurfaceHolder holder) {
            // 카메라 설정                                                                        
                try {

                    camera = Camera.open();

                    Camera.Parameters params = camera.getParameters();

                    params.setJpegQuality(80); // 사진 품질 설정

                    params.setPictureSize(Utility.WidthPixel, Utility.HeightPixel);

                    params.setPreviewFrameRate(10); // 미리보기 프레임 속도

                    params.setPreviewSize(Utility.WidthPixel, Utility.HeightPixel);

                    camera.setParameters(params); // 카메라 매개 변수 설정

                    camera.setPreviewDisplay(cameraSurfaceView.getHolder()); // 미리보기 표시 설정

                    camera.startPreview(); // 미리보기 시작

                    camera.autoFocus(new Camera.AutoFocusCallback() { // 연속 자동 초점

                        @Override

                        public void onAutoFocus(boolean success, Camera camera) {

                            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

                            camera.autoFocus(this);

                        }

                    });

                    allowTakePicture = true;

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

            @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override

            public void surfaceDestroyed(SurfaceHolder holder) {

                if(camera != null){

                    camera.release();

                    camera = null;

                }

            }

        });



        // idCard 객체의 정보를 기반으로 사각형 영역 ID를 표시하는 최상위 레벨의 SurfaceView

        topSurfaceView = findViewById(R.id.topSurfaceView);

        topSurfaceView.setZOrderOnTop(true); // 위에

        holder = topSurfaceView.getHolder();

        holder.setFixedSize(Utility.WidthPixel, Utility.HeightPixel);

        holder.setFormat(PixelFormat.TRANSPARENT); // 투명

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override

            public void surfaceCreated(SurfaceHolder holder) {

                Canvas canvas = holder.lockCanvas();

                canvas.drawColor(Color.TRANSPARENT);

                Paint paint = new Paint();

                paint.setAntiAlias(true);

                paint.setColor(Color.RED);

                paint.setStyle(Paint.Style.STROKE);

                for (IdCard.Rect rect : Utility.idCard.getRects()) {

                    canvas.drawRect(rect.toRect(), paint);

                }

                holder.unlockCanvasAndPost(canvas);

            }

            @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override public void surfaceDestroyed(SurfaceHolder holder) { }

        });

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        for (int grantResult : grantResults) {

            if (grantResult != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "이 앱을 사용하려면 모든 권한을 부여하는 데 동의해야합니다.", Toast.LENGTH_LONG).show();

                System.exit(-1);

            }

        }

    }



    // 클릭 이벤트

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        if (allowTakePicture) {

            camera.takePicture(null, null, new Camera.PictureCallback() { // 사진찍기

                @Override

                public void onPictureTaken(byte[] data, Camera camera) { // 처리된 사진

                    try {

                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length); // 원본 이미지

                        Utility.saveBitmap(bitmap, getResources().getString(R.string.original_picture)); // 저장



                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);

                        startActivity(intent);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            });

            allowTakePicture = false;

        }

        return super.onTouchEvent(event);

    }

}
