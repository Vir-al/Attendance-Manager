package me.viral.personalattendancemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Riv_al on 4/29/2018.
 */



public class AttendanceListviewAdapter extends ArrayAdapter<attendanceContainer> {

    TextView commonTextview;
    ImageButton mButton;

    public AttendanceListviewAdapter(Context context, int resource) {
        super(context, resource);
    }

//    to conver the single integer into double digit string eg. 8 => "08"
    public String convertToDoubleDigit(int number){
        if(number < 10){
            switch (number){
                case 0:
                    return "00";
                case 1:
                    return "01";
                case 2:
                    return "02";
                case 3:
                    return "03";
                case 4:
                    return "04";
                case 5:
                    return "05";
                case 6:
                    return "06";
                case 7:
                    return "07";
                case 8:
                    return "08";
                case 9:
                    return "09";
            }
        }

        return String.valueOf(number);
    }

//    to change the background of percentage eg. green or red
    public void changePercentageBackground(TextView textView,int percentage,int minRequired){
        if(percentage < minRequired ){
            textView.setBackgroundResource(R.drawable.below_required_background);
        } else {
            textView.setBackgroundResource(R.drawable.above_required_background);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subject_row,parent,false);
        }

        attendanceContainer c = getItem(position);

        commonTextview = (TextView) convertView.findViewById(R.id.subjectName);
        commonTextview.setText(c.getSubjectName());

        commonTextview = (TextView) convertView.findViewById(R.id.minRequiredLabel);
        commonTextview.setText("Minimum required : " + c.getMinRequired());

        commonTextview = (TextView) convertView.findViewById(R.id.semesterLabel);
        commonTextview.setText(c.getSemester());

        commonTextview = (TextView) convertView.findViewById(R.id.updateStatus);
        commonTextview.setText(c.getDateTime());


        commonTextview = (TextView) convertView.findViewById(R.id.percentage);
        commonTextview.setText(c.getPercentage() + "%");
        changePercentageBackground(commonTextview,Integer.parseInt(c.getPercentage()),c.getMinRequired());

        commonTextview = (TextView) convertView.findViewById(R.id.predictLecture);
        commonTextview.setText(c.getPredictLecture());

        commonTextview = (TextView) convertView.findViewById(R.id.attendedCount);
        commonTextview.setText(convertToDoubleDigit(c.getPresent()));

        commonTextview = (TextView) convertView.findViewById(R.id.bunkedCount);
        commonTextview.setText(convertToDoubleDigit(c.getAbsent()));

        mButton = (ImageButton) convertView.findViewById(R.id.incAttended);
        mButton.setTag(c.getSubjectId());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getContext());

                Date currentDate = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
                SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");

                String[] result = db.incrementAttendance("attended", Integer.parseInt(view.getTag().toString()), df.format(currentDate).toString(),tf.format(currentDate).toString());
                RelativeLayout countContainer = (RelativeLayout) view.getParent();
                TextView attended = (TextView) countContainer.findViewById(R.id.attendedCount);
                attended.setText(convertToDoubleDigit(Integer.parseInt(result[0])));

                RelativeLayout parentContainer = (RelativeLayout) view.getParent().getParent().getParent().getParent().getParent();
                TextView percentage = (TextView) parentContainer.findViewById(R.id.percentage);
                percentage.setText(result[1] + "%");
                changePercentageBackground(percentage,Integer.parseInt(result[1]),Integer.parseInt(result[2]));

                TextView dateTime = (TextView) parentContainer.findViewById(R.id.updateStatus);
                dateTime.setText(result[3]);

            }
        });

        mButton = (ImageButton) convertView.findViewById(R.id.incBunked);
        mButton.setTag(c.getSubjectId());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getContext());

                Date currentDate = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
                SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");

                String[] result = db.incrementAttendance("bunked", Integer.parseInt(view.getTag().toString()), df.format(currentDate).toString(),tf.format(currentDate).toString());
                RelativeLayout relativeLayout = (RelativeLayout) view.getParent();
                TextView bunked = (TextView) relativeLayout.findViewById(R.id.bunkedCount);
                bunked.setText(convertToDoubleDigit(Integer.parseInt(result[0])));

                RelativeLayout parentContainer = (RelativeLayout) view.getParent().getParent().getParent().getParent().getParent();
                TextView percentage = (TextView) parentContainer.findViewById(R.id.percentage);
                percentage.setText(result[1] + "%");
                changePercentageBackground(percentage,Integer.parseInt(result[1]),Integer.parseInt(result[2]));

                TextView dateTime = (TextView) parentContainer.findViewById(R.id.updateStatus);
                dateTime.setText(result[3]);

            }
        });

        return convertView;
    }
}
