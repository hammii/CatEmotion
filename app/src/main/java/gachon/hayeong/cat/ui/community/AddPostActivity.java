package gachon.hayeong.cat.ui.community;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gachon.hayeong.cat.R;

public class AddPostActivity extends AppCompatActivity {
    private EditText et_videoLink;
    private EditText et_explain;
    private Button btn_search_video;
    private Button btn_add;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        et_videoLink = (EditText) findViewById(R.id.et_videoLink);
        et_explain = (EditText) findViewById(R.id.et_explain);
        btn_search_video = (Button) findViewById(R.id.btn_search_video);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_search_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_videoLink == null){
                    Toast.makeText(getApplicationContext(), "비디오를 첨부해주세요.", Toast.LENGTH_SHORT).show();
                } else if(et_explain == null){
                    Toast.makeText(getApplicationContext(), "설명을 작성해주세요.", Toast.LENGTH_SHORT).show();
                } else{
                    //첨부하는 코드
                    Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}