package com.example.among.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.among.R;

public class SignUpTerms extends AppCompatActivity {
    CheckBox[] checkArr = new CheckBox[3];
    CheckBox checkBoxAll;
    Button next;
    CheckBoxListner chkListner;
    ButtonClickListener btnListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);
        checkArr[0] = findViewById(R.id.checkbox1);
        checkArr[1] = findViewById(R.id.checkbox2);
        checkArr[2] = findViewById(R.id.checkbox3);
        checkBoxAll = findViewById(R.id.checkboxAll);
        next = findViewById(R.id.next);


        chkListner = new CheckBoxListner();
        btnListener = new ButtonClickListener();
        checkBoxAll.setOnCheckedChangeListener(chkListner);
        for(int i=0;i<checkArr.length;i++){
            checkArr[i].setOnCheckedChangeListener(chkListner);
        }

    }
    class CheckBoxListner implements CompoundButton.OnCheckedChangeListener{
        //모든 체크박스의 상태를 체크 상태로 설정 - 매개변수를 이용해서 설정 및 해제
        public void setCheckVal(boolean chkVal){
            for(int i=0; i<checkArr.length;i++){
                checkArr[i].setChecked(chkVal);

            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(checkBoxAll.isChecked()){
                setCheckVal(checkBoxAll.isChecked());
                next.setEnabled(true);
                next.setOnClickListener(btnListener);
            }else {
                if(checkArr[0].isChecked()&checkArr[1].isChecked()&checkArr[2].isChecked()){
                    next.setEnabled(true);
                    next.setOnClickListener(btnListener);
                }else {
                    next.setEnabled(false);
                }
            }

        }
    }

    class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SignUpTerms.this,SignUp.class);
            startActivity(intent);
            finish();
        }
    }
}
