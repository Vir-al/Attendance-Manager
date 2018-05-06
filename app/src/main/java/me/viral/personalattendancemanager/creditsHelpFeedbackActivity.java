package me.viral.personalattendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class creditsHelpFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        switch(extras.getString(MainActivity.CREDIT_HELP_FEEDBACK)){
            case "credits":
                setContentView(R.layout.credits);
                TextView textView = (TextView) findViewById(R.id.textView2);
                textView.setText("Made with " + new String(Character.toChars(0x2764)));
                break;
            case "feedback":
                setContentView(R.layout.feedback);
                break;
            case "help":
                setContentView(R.layout.help);
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
