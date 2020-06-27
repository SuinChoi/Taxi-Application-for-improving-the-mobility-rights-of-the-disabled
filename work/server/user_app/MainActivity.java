package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	//로그인화면
    TextView userId;
    Button btnSave;
    loadJsp task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //텍스트 뷰 읽어오기
        btnSave = (Button)findViewById(R.id.btnSave);
        userId = (TextView)findViewById(R.id.userId);


        //버튼 클릭
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 저장을 눌렀을때
                task = new loadJsp();
                task.execute();
            }
        });

    }

    class loadJsp extends AsyncTask<Void,String,Void>{

        @Override
        protected Void doInBackground(Void... param) {
            // TODO Auto-generated method stub

            try{
                HttpClient client = new DefaultHttpClient();

                // jsp 주소
                String postURL = "http://3.133.117.138:8080/index.jsp";

                HttpPost post = new HttpPost(postURL);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                //파
                params.add(new BasicNameValuePair("userId",userId.getText().toString()));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null)
                {
                    Log.i("RESPONSE", EntityUtils.toString(resEntity));
                }

            }catch(IOException e){
                e.printStackTrace();
            }
            return null;

        }
    }
}


