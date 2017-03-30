package com.example.administrator.my_word;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements SpeechSynthesizerListener {

    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "bd_etts_speech_female.dat";
    private static final String TEXT_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "bd_etts_text.dat";
    private SpeechSynthesizer mSpeechSynthesizer;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_start = (Button) findViewById(R.id.btn);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(FILE_PATH);
                File file1 = new File(TEXT_FILE_PATH);
                Log.i(TAG, FILE_PATH);
                if (file.exists()) {
                    Toast.makeText(MainActivity.this, "bd_etts_speech_female.dat", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, TEXT_FILE_PATH);
                if (file1.exists()) {
                    Toast.makeText(MainActivity.this, "bd_etts_text.dat", Toast.LENGTH_SHORT).show();
                }
                startTTS();
            }
        });


    }

    private void startTTS() {
        // 获取语音合成对象实例
        copyFileToSD("bd_etts_speech_female.dat");
        copyFileToSD("bd_etts_text.dat");


        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(this);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);

        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("P7BQBkAM8sjQGz54IA9kSzZl", "e595e212c2fcb70d7d1376dfbd04a474");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("9458759");

        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, TEXT_FILE_PATH);
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, FILE_PATH);
        // 获取语音合成授权信息
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
        if (authInfo.isSuccess()) {
            Toast.makeText(this, "授权成功", Toast.LENGTH_LONG).show();
            mSpeechSynthesizer.initTts(TtsMode.MIX);
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
            mSpeechSynthesizer.speak("Hello,合成成功了!");
        } else {
            // 授权失败
            Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSynthesizeStart(String s) {
        Log.i(TAG, "onSynthesizeStart");
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        Log.i(TAG, "onSynthesizeDataArrived: ");
    }

    @Override
    public void onSynthesizeFinish(String s) {
        Log.i(TAG, "onSynthesizeFinish: ");
    }

    @Override
    public void onSpeechStart(String s) {
        Log.i(TAG, "onSpeechStart: ");
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        Log.i(TAG, "onSpeechProgressChanged: ");
    }

    @Override
    public void onSpeechFinish(String s) {
        Log.i(TAG, "onSpeechFinish: ");
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        Log.i(TAG, "onError: ");
    }

    private void copyFileToSD(String fileName) {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(absolutePath, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "拷贝文件失败1", Toast.LENGTH_LONG).show();
            }
            try {
                InputStream open = getAssets().open(fileName);
                OutputStream outputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len;
                while ((len = open.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }
                open.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "拷贝文件失败2", Toast.LENGTH_LONG).show();
            }
        }
    }
}