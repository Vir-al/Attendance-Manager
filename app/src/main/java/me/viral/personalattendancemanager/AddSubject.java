package me.viral.personalattendancemanager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSubject extends AppCompatActivity {
    private TextView subjectName;
    private TextView semester;
    private TextView minRequired;
    private TextView attended;
    private TextView bunked;
    private Button addSubject;
    private ImageView backButton;
    private ImageButton actionMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        subjectName = (TextView) findViewById(R.id.subjectName);
        semester = (TextView) findViewById(R.id.semester);
        minRequired= (TextView) findViewById(R.id.minRequired);
        attended = (TextView) findViewById(R.id.attended);
        bunked = (TextView) findViewById(R.id.bunked);

        addSubject = (Button) findViewById(R.id.addSubject);
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(subjectName.getText().toString())) {
                    Drawable d= getResources().getDrawable(R.drawable.empty_field_error);
                    d.setBounds(0, 0,d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    subjectName.setError("Subject name is required!",d);
                }

                else if(TextUtils.isEmpty(semester.getText().toString())) {
                    Drawable d= getResources().getDrawable(R.drawable.empty_field_error);
                    d.setBounds(0, 0,d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    semester.setError("Specify the semester!",d);
                }

                else if(TextUtils.isEmpty(minRequired.getText().toString())) {
                    Drawable d= getResources().getDrawable(R.drawable.empty_field_error);
                    d.setBounds(0, 0,d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    minRequired.setError("Enter Minimum attendance required here!",d);
                }

                else {
                    DatabaseHelper db = new DatabaseHelper(AddSubject.this);
                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
                    SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");
                    boolean result = db.addSubject(
                            subjectName.getText().toString(),
                            semester.getText().toString(),
                            Integer.parseInt(minRequired.getText().toString()),
                            Integer.parseInt(attended.getText().toString()),
                            Integer.parseInt(bunked.getText().toString()),
                            df.format(currentDate).toString(),
                            tf.format(currentDate).toString());
                    if(result){
                        finish();
                    } else {
                        Toast.makeText(AddSubject.this,"Some error occured!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        actionMenu = (ImageButton) findViewById(R.id.action_menu);
        registerForContextMenu(actionMenu);
        actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.showContextMenu();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_setting_menu, menu);
//        menu.setHeaderTitle("Contacts");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = new Intent(AddSubject.this,creditsHelpFeedbackActivity.class);
        switch(item.getItemId()){
            case R.id.menu_credits :
                intent.putExtra(MainActivity.CREDIT_HELP_FEEDBACK,"credits");
                startActivity(intent);
                break;
            case R.id.menu_feedback :
                intent.putExtra(MainActivity.CREDIT_HELP_FEEDBACK,"feedback");
                startActivity(intent);
                break;
            case R.id.menu_help :
                intent.putExtra(MainActivity.CREDIT_HELP_FEEDBACK,"help");
                startActivity(intent);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
