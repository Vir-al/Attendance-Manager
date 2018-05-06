package me.viral.personalattendancemanager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditSubject extends AppCompatActivity {

    final public static String EXTRA_EDIT_SUBJECT = "extra_edit_subject";
    private TextView name;
    private TextView semester;
    private TextView minRequired;
    private TextView attended;
    private TextView bunked;

    private Button edit;
    private Button remove;
    private static DatabaseHelper db;
    private int subjectiId;
    ImageView backButton;
    private ImageButton actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        Bundle extras = getIntent().getExtras();
        String[] extraContent = extras.getStringArray(EXTRA_EDIT_SUBJECT);
        subjectiId = Integer.parseInt(extraContent[0]);

        name = (TextView) findViewById(R.id.subjectName);
        name.setText(extraContent[1]);

        semester = (TextView) findViewById(R.id.semester);
        semester.setText(extraContent[2]);

        minRequired= (TextView) findViewById(R.id.minRequired);
        minRequired.setText(extraContent[3]);

        attended= (TextView) findViewById(R.id.attended);
        attended.setText(extraContent[4]);

        bunked = (TextView) findViewById(R.id.bunked);
        bunked.setText(extraContent[5]);

        db = new DatabaseHelper(EditSubject.this);

        edit = (Button) findViewById(R.id.editSubject);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditSubject.this);
                dialog.setContentView(R.layout.remove_dialog);
                TextView textView = (TextView) dialog.findViewById(R.id.dialogTitle);
                textView.setText("Are you sure to update the subject ?");
                ImageButton yes = (ImageButton) dialog.findViewById(R.id.btn_yes);
                ImageButton no = (ImageButton) dialog.findViewById(R.id.btn_no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date currentDate = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
                        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");
                        db.updateSubject(subjectiId,
                                        name.getText().toString(),
                                        semester.getText().toString(),
                                        minRequired.getText().toString(),
                                        attended.getText().toString(),
                                        bunked.getText().toString(),
                                        df.format(currentDate).toString(),
                                        tf.format(currentDate).toString());
                        finish();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        remove = (Button) findViewById(R.id.removeSubject);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditSubject.this);
                dialog.setContentView(R.layout.remove_dialog);
                ImageButton yes = (ImageButton) dialog.findViewById(R.id.btn_yes);
                ImageButton no = (ImageButton) dialog.findViewById(R.id.btn_no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.removeSubject(subjectiId);
                        finish();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
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
        Intent intent = new Intent(EditSubject.this,creditsHelpFeedbackActivity.class);
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
