package com.example.among.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.among.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {
    EditText userID;
    EditText name;
    EditText password;
    EditText phone;
    EditText birth;
    Button register;
    Button idChk;
    RadioGroup radioGroup;
    TextView chkText;

    MemberDTO member;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);
        userID = findViewById(R.id.userID);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        birth = findViewById(R.id.birth);
        radioGroup = (RadioGroup) findViewById(R.id.radio1);
        chkText = findViewById(R.id.chktext);

        register = findViewById(R.id.register);
        idChk = findViewById(R.id.certificate);
        tokenMake();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = findViewById(radioGroup.getCheckedRadioButtonId());

                String id = userID.getText().toString();
                String na = name.getText().toString();
                String pass = password.getText().toString();
                String num = phone.getText().toString();
                String bir = birth.getText().toString();
                String gen = rb.getTag().toString();

                Log.d("msg",token);
                member = new MemberDTO(id, na, pass, num, bir, gen, token);

                HttpInsert task = new HttpInsert();
                task.execute(member);
            }
        });

        idChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userID.getText().toString();

                member = new MemberDTO(id);
                HttpIDChk task = new HttpIDChk();
                task.execute(member);
            }
        });
    }

    public void tokenMake(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                //토큰을 가져오다 실패하면 실행하게 된다.
                if (!task.isSuccessful()) {
                    Log.d("myfcm", "getInstanceId failed", task.getException());
                    return;
                }
                token = task.getResult().getToken();
                Log.d("myfcm", token);
            }
        });
    }

    class HttpInsert extends AsyncTask<MemberDTO, Void, String>{

        @Override
        protected String doInBackground(MemberDTO... memberDTOS) {
            URL url = null;
            JSONObject object = new JSONObject();
            String data="";
            try {
                object.put("userID",memberDTOS[0].getUserID());
                object.put("name",memberDTOS[0].getName());
                object.put("password",memberDTOS[0].getPassword());
                object.put("phone",memberDTOS[0].getPhone());
                object.put("birth",memberDTOS[0].getBirth());
                object.put("gender",memberDTOS[0].getGender());
                object.put("token",memberDTOS[0].getToken());
                url = new URL("http://172.20.10.4:8088/among/member/insert.do");

                OkHttpClient client = new OkHttpClient();
                String json = object.toString();
                Log.d("msg",json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"),json))
                        .build();

                Response response = client.newCall(request).execute();
                data = response.body().string();
                Log.d("msg",data);

                // Connection을 이용한 GET 통신 방법
                /*HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                data = "userID="+memberDTOS[0].getUserID()+"&name="+memberDTOS[0].getName()
                    +"&password="+memberDTOS[0].getPassword()+"&phone="+memberDTOS[0].getPhone()
                    +"&birth="+memberDTOS[0].getBirth()+"&gender="+memberDTOS[0].getGender();
                osw.write(data);
                osw.flush();
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()
                            ,"UTF-8"));
                    Log.d("myhttp",data);
                    Intent intent = new Intent(SignUp.this,LoginActivity.class);
                    startActivity(intent);
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("1")){
                Intent intent = new Intent(SignUp.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(SignUp.this,"다시작성해 주세요",Toast.LENGTH_LONG).show();
            }
        }
    }

    class HttpIDChk extends AsyncTask<MemberDTO, Void, String>{

        @Override
        protected String doInBackground(MemberDTO... memberDTOS) {
            URL url = null;
            BufferedReader br = null;
            String data;
            String str="";
            try {
                url = new URL("http://172.20.10.4:8088/among/member/chk.do");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                data = "userID="+memberDTOS[0].getUserID();
                osw.write(data);
                osw.flush();

                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()
                            ,"UTF-8"));
                    str = br.readLine();
                    Log.d("msg",str+"chk");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("0")){
                chkText.setText("사용가능한 ID입니다.");
                chkText.setTextColor(Color.parseColor("#2196F3"));
            }else{
                chkText.setText("중복되는 ID");
                chkText.setTextColor(Color.parseColor("#CD1212"));
            }
        }
    }
}
