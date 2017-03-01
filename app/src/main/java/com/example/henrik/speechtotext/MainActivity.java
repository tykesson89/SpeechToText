package com.example.henrik.speechtotext;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView btnSpeak;
    private TextView txtResult;
    private TextView txtHint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSpeak = (ImageView)findViewById(R.id.btnSpeak);
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtHint = (TextView)findViewById(R.id.txtHint);
        initListeners();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void initListeners(){
        btnSpeak.setOnClickListener(new BtnSpeakListener());
    }


    private class BtnSpeakListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            promtSpeechInput();
        }
    }

    private void promtSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");

        try{
        startActivityForResult(i, 1);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "Sorry, your device do not support speech language", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:if (resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(result.get(0).contains("yes") && !result.get(0).contains("no")){
                    txtResult.setText("YES");
                    txtResult.setTextColor(Color.RED);
                    txtHint.setText("Wrong answer");
                }else if(result.get(0).contains("no") && !result.get(0).contains("yes")){
                    txtResult.setText("NO");
                    txtResult.setTextColor(Color.GREEN);
                    txtHint.setText("Right answer");
                }else{
                    txtResult.setText("");
                    txtHint.setText("Hint: Use only 'yes' or 'no'");
                }
            }
        }

    }
}
