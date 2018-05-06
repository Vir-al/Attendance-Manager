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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final public static String CREDIT_HELP_FEEDBACK = "extra_action_menu";

    private ImageButton addSubjectCircle;
    private Button addSubjectRectangle;
    private ArrayList<attendanceContainer> arrayList;
    private ImageButton actionMenu;

    AttendanceListviewAdapter adapter;
    ListView listView;


    DatabaseHelper db = new DatabaseHelper(MainActivity.this);

    protected void updateList(){

        db = new DatabaseHelper(MainActivity.this);
        arrayList = db.getSubjectWithAttendance();

        if (!arrayList.isEmpty()) {

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.defaultMessage);
            linearLayout.setVisibility(View.GONE);
            ImageView imageView = (ImageView) findViewById(R.id.defaultImage);
            imageView.setVisibility(View.GONE);
            imageView = (ImageView) findViewById(R.id.home_background);
            imageView.setVisibility(View.GONE);
            addSubjectCircle.setVisibility(View.GONE);

            ListView listView = (ListView) findViewById(R.id.subjectListview);
            listView.setVisibility(View.VISIBLE);
            addSubjectRectangle.setVisibility(View.VISIBLE);

            adapter = new AttendanceListviewAdapter(MainActivity.this,R.layout.subject_row);
            listView = (ListView) findViewById(R.id.subjectListview);
            String temp = "";
            for(attendanceContainer container : arrayList){
                adapter.add(container);
//                temp += container.getSubjectName() + " \n";
//                temp += container.getPresent() + " \n";
//                temp += container.getAbsent() + " \n";
//                temp += container.getPredictLecture() + " \n";
//                temp += container.getPercentage() + " \n";
//                temp += container.getMinRequired() + " \n";
//                temp += container.getDateTime() + " \n";
//                temp += container.getSemester() + " \n\n";

            }

//            TextView t = (TextView) findViewById(R.id.temp);
//            t.setText(temp);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this,EditSubject.class);
                    attendanceContainer container = (attendanceContainer) adapterView.getAdapter().getItem(i);
                    String[] sem = container.getSemester().split(" ");
                    String[] extra =   {String.valueOf(container.getSubjectId()),
                                        container.getSubjectName(),
                                        sem[2],
                                        String.valueOf(container.getMinRequired()),
                                        String.valueOf(container.getPresent()),
                                        String.valueOf(container.getAbsent())};
                    intent.putExtra(EditSubject.EXTRA_EDIT_SUBJECT,extra);
                    startActivity(intent);
//                    Toast.makeText(MainActivity.this, " ", Toast.LENGTH_SHORT).show();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final attendanceContainer container = (attendanceContainer) adapterView.getAdapter().getItem(i);
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.remove_dialog);
                    ImageButton yes = (ImageButton) dialog.findViewById(R.id.btn_yes);
                    ImageButton no = (ImageButton) dialog.findViewById(R.id.btn_no);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.removeSubject(container.getSubjectId());
                            dialog.dismiss();
                            updateList();
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
                    return true;
                }
            });
        } else {

            ListView listView = (ListView) findViewById(R.id.subjectListview);
            listView.setVisibility(View.GONE);
            addSubjectRectangle.setVisibility(View.GONE);

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.defaultMessage);
            linearLayout.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) findViewById(R.id.defaultImage);
            imageView.setVisibility(View.VISIBLE);
            addSubjectCircle.setVisibility(View.VISIBLE);
            imageView = (ImageView) findViewById(R.id.home_background);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addSubjectCircle = (ImageButton) findViewById(R.id.add_subject_before);
        addSubjectCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddSubject.class);
                startActivity(intent);
            }
        });

        addSubjectRectangle = (Button) findViewById(R.id.add_subject_after);
        addSubjectRectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddSubject.class);
                startActivity(intent);
            }
        });

        updateList();

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
        Intent intent = new Intent(MainActivity.this,creditsHelpFeedbackActivity.class);
        switch(item.getItemId()){
            case R.id.menu_credits :
                intent.putExtra(CREDIT_HELP_FEEDBACK,"credits");
                startActivity(intent);
                break;
            case R.id.menu_feedback :
                intent.putExtra(CREDIT_HELP_FEEDBACK,"feedback");
                startActivity(intent);
                break;
            case R.id.menu_help :
                intent.putExtra(CREDIT_HELP_FEEDBACK,"help");
                startActivity(intent);
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }
}
