package com.example.myapplication;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

public class OCRResultActivity extends AppCompatActivity {

    private Handler handler;
    private Map<Integer, EditText> tagViewMap;
    private int remainingTask;
    private Bitmap originalPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        final TextView stateTextView = findViewById(R.id.state); // 현재 상태 정보를 표시하는 텍스트 상자
        final long startTime = System.currentTimeMillis(); // 기록 시작 시간
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.recognizing));
        progressDialog.show();*/

        // 테스트 할 모든 ID 매개 변수
        tagViewMap = new HashMap<>();
        tagViewMap.put(R.string.name, (EditText) findViewById(R.id.name));
        tagViewMap.put(R.string.sex, (EditText) findViewById(R.id.sex));
        tagViewMap.put(R.string.ethnicity, (EditText) findViewById(R.id.ethnicity));
        tagViewMap.put(R.string.year, (EditText) findViewById(R.id.year));
        tagViewMap.put(R.string.month, (EditText) findViewById(R.id.month));
        tagViewMap.put(R.string.day, (EditText) findViewById(R.id.day));
        tagViewMap.put(R.string.number, (EditText) findViewById(R.id.number));
        tagViewMap.put(R.string.address, (EditText) findViewById(R.id.address));
        remainingTask = tagViewMap.size();

        // 매개 변수 감지가 완료되면 호출됨.
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tagViewMap.get(msg.what).setText((String) msg.obj);
                remainingTask--;

                if (remainingTask <= 0) {
                    //progressDialog.cancel();
                    float spendTime = (System.currentTimeMillis() - startTime) / 1000f;
                    stateTextView.setText(getResources().getString(R.string.spend_time) + spendTime + "s");
                    stateTextView.setTextColor(Color.GREEN);

                }
            }
        };

        // 원본 읽기
        originalPicture = BitmapFactory.decodeFile(OCRUtility.getWorkDirectory() + "/" + getResources().getString(R.string.original_picture) + ".jpg");
        OCRIdCard.Rect rect = OCRUtility.idCard.getCardRect(); // ID 자르고 정보 가져오기
        Bitmap cardBitmap = Bitmap.createBitmap(originalPicture, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()); // ID카드 세분화
        OCRUtility.saveBitmap(cardBitmap, getResources().getString(rect.getTagId())); // 저장
        ((ImageView) findViewById(R.id.photo)).setImageBitmap(cardBitmap); // 잘라진 ID 보여주기

        // ID 카드의 각 매개 변수를 개별적으로 스레드 및 처리
        for (int tagId : tagViewMap.keySet()) {
            new OcrThread(tagId).start();
        }
    }

    // 쓰레드 실행
    public class OcrThread extends Thread {
        private int tagId;
        public OcrThread(int tagId) {
            this.tagId = tagId;
        }
        @Override
        public void run() {
            OCRIdCard.Rect rect = OCRUtility.idCard.getTagRectMap().get(tagId);
            Bitmap bitmap = Bitmap.createBitmap(originalPicture, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()); // 裁剪
            //bitmap = Utility.binary(bitmap); // 이진화
            OCRUtility.saveBitmap(bitmap, getResources().getString(tagId)); // 저장
            String result = OCRUtility.doOcr(bitmap); // 텍스트 이미지
            result = OCRUtility.fix(result, tagId, getResources()); // 버그 픽스
            Message message = new Message();
            message.what = tagId;
            message.obj = result;
            handler.sendMessage(message);
        }
    }
}