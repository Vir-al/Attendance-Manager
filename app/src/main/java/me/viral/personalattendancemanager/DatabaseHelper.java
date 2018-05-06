package me.viral.personalattendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Riv_al on 4/28/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String DBNAME = "AttendanceManager";
    private Context context;

//    table subject
    private final String SUBJECT_TABLENAME = "subjects";
    private final String SUBJECT_ID = "id";
    private final String SUBJECT_NAME = "name";
    private final String SUBJECT_SEMESTER = "semester";
    private final String SUBJECT_MINREQUIRED = "min_required";

//    table attendance
    private final String ATTENDANCE_TABLENAME = "attendance";
    private final String ATTENDANCE_ID = "id";
    private final String ATTENDANCE_SUBJECT_ID = "sub_id";
    private final String ATTENDANCE_DATE = "date";
    private final String ATTENDANCE_TIME = "time";
    private final String ATTENDANCE_PRESENT = "present";
    private final String ATTENDANCE_ABSENT = "absent";



    public DatabaseHelper(Context context) {
        super(context, "AttendanceManager", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        for subject
        String query = "CREATE TABLE " + SUBJECT_TABLENAME + " ("+ SUBJECT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +SUBJECT_NAME+" TEXT,"
                +SUBJECT_SEMESTER+" TEXT,"+SUBJECT_MINREQUIRED+" INTEGER)";
        sqLiteDatabase.execSQL(query);
//        for attendance
        query = "CREATE TABLE " + ATTENDANCE_TABLENAME + " ("+ ATTENDANCE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +ATTENDANCE_DATE+" TEXT,"
                +ATTENDANCE_TIME+" TEXT,"
                +ATTENDANCE_SUBJECT_ID+" INTEGER,"+ATTENDANCE_ABSENT+" INTEGER,"+ATTENDANCE_PRESENT+" INTEGER)";
        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        onCreate(sqLiteDatabase);
    }

    public boolean addSubject(String name, String semester, int minRequired, int present, int absent, String date, String time){

        SQLiteDatabase dbWrite = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUBJECT_NAME,name);
        values.put(SUBJECT_SEMESTER,semester);
        values.put(SUBJECT_MINREQUIRED,minRequired);
        long id = dbWrite.insert(SUBJECT_TABLENAME,null,values);

        values.clear();

        values.put(ATTENDANCE_SUBJECT_ID,id);
        values.put(ATTENDANCE_PRESENT,present);
        values.put(ATTENDANCE_ABSENT,absent);
        values.put(ATTENDANCE_DATE,date);
        values.put(ATTENDANCE_TIME,time);
        long result = dbWrite.insert(ATTENDANCE_TABLENAME,null,values);

        dbWrite.close();
        return result > 0;
    }

    public boolean updateSubject(int subId, String name, String semester, String minRequired, String present, String absent, String date, String time){

        SQLiteDatabase dbWrite = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUBJECT_NAME,name);
        values.put(SUBJECT_SEMESTER,semester);
        values.put(SUBJECT_MINREQUIRED,minRequired);
        long id = dbWrite.update(SUBJECT_TABLENAME,values,SUBJECT_ID+"="+subId,null);

        values.clear();

        values.put(ATTENDANCE_PRESENT,present);
        values.put(ATTENDANCE_ABSENT,absent);
        values.put(ATTENDANCE_DATE,date);
        values.put(ATTENDANCE_TIME,time);
        long result = dbWrite.update(ATTENDANCE_TABLENAME,values,ATTENDANCE_SUBJECT_ID+"="+subId,null);

        dbWrite.close();
        return result > 0;
    }

    public int calculatePercentage(int presentTemp, int absentTemp){
        int percentage = 0;
        if((absentTemp + presentTemp) != 0){
            percentage = (int) Math.floor(((double) presentTemp / (absentTemp + presentTemp)) * 100);
        }
        return percentage;
    }

    public ArrayList<attendanceContainer> getSubjectWithAttendance(){
        ArrayList<attendanceContainer>  arrayContainer = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();


        String querySub = "SELECT * FROM " + SUBJECT_TABLENAME ;
        String queryAtt;

        Cursor cursorSub = db.rawQuery(querySub,null);

        while(cursorSub.moveToNext()){
            int subId = cursorSub.getInt(cursorSub.getColumnIndex(SUBJECT_ID));
            queryAtt = "SELECT * FROM " + ATTENDANCE_TABLENAME + " WHERE " + ATTENDANCE_SUBJECT_ID + "='" + subId + "'";
            Cursor cursorAtt = db.rawQuery(queryAtt,null);

            if(cursorAtt.moveToFirst()) {

                attendanceContainer container = new attendanceContainer();
                //            feed data to subject container
                container.setSubjectId(cursorSub.getInt(cursorSub.getColumnIndex(SUBJECT_ID)));
                container.setSubjectName(cursorSub.getString(cursorSub.getColumnIndex(SUBJECT_NAME)));
                container.setSemester("Sem : " + cursorSub.getString(cursorSub.getColumnIndex(SUBJECT_SEMESTER)));
                container.setMinRequired(cursorSub.getInt(cursorSub.getColumnIndex(SUBJECT_MINREQUIRED)));
                container.setDateTime("Last updated on "
                        + cursorAtt.getString(cursorAtt.getColumnIndex(ATTENDANCE_DATE))
                        + ", "
                        + cursorAtt.getString(cursorAtt.getColumnIndex(ATTENDANCE_TIME))
                );

                int absentTemp = cursorAtt.getInt(cursorAtt.getColumnIndex(ATTENDANCE_ABSENT));
                int presentTemp = cursorAtt.getInt(cursorAtt.getColumnIndex(ATTENDANCE_PRESENT));
                int percentage = calculatePercentage(presentTemp,absentTemp);


                container.setAbsent(absentTemp);
                container.setPresent(presentTemp);
                container.setPercentage(String.valueOf(percentage));

                //            calculate how many lectures can bunk or attend
                container.setPredictLecture("Minimum lecture to attend : " + 0);
                arrayContainer.add(container);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }
            cursorAtt.close();
        }

        db.close();
        cursorSub.close();

        return arrayContainer;
    }

    public String[] incrementAttendance(String attendanceStatus,int subjectId, String date, String time){
        String[] result = new String[4];
        SQLiteDatabase dbWrite = getWritableDatabase();
        SQLiteDatabase dbRead = getReadableDatabase();


        String queryAtt = "SELECT " + ATTENDANCE_PRESENT + " , " + ATTENDANCE_ABSENT + " FROM " + ATTENDANCE_TABLENAME + " WHERE " + ATTENDANCE_SUBJECT_ID + "='" + subjectId + "'"  ;
        Cursor  cursorAtt = dbRead.rawQuery(queryAtt,null);
        cursorAtt.moveToFirst();
        int present = cursorAtt.getInt(cursorAtt.getColumnIndex(ATTENDANCE_PRESENT));
        int absent = cursorAtt.getInt(cursorAtt.getColumnIndex(ATTENDANCE_ABSENT));
        cursorAtt.close();

        String querySub = "SELECT " + SUBJECT_MINREQUIRED + " FROM " + SUBJECT_TABLENAME + " WHERE " + SUBJECT_ID + "='" + subjectId + "'"  ;
        Cursor  cursorSub = dbRead.rawQuery(querySub,null);
        cursorSub.moveToFirst();
        result[2] = String.valueOf(cursorSub.getInt(cursorSub.getColumnIndex(SUBJECT_MINREQUIRED)));
        cursorSub.close();

        ContentValues values = new ContentValues();
        values.put(ATTENDANCE_DATE,date);
        values.put(ATTENDANCE_TIME,time);
        result[3] = "Last updated on " + date + ", " + time;
        switch(attendanceStatus){
            case "attended":
//
                values.put(ATTENDANCE_PRESENT,++present);
                dbWrite.update(ATTENDANCE_TABLENAME,values,ATTENDANCE_SUBJECT_ID+"="+subjectId,null);
                result[0] = String.valueOf(present);
                result[1] = String.valueOf(calculatePercentage(present,absent));
                return result;

            case "bunked":

                values.put(ATTENDANCE_ABSENT,++absent);
                dbWrite.update(ATTENDANCE_TABLENAME,values,ATTENDANCE_SUBJECT_ID+"="+subjectId,null);
                result[0] = String.valueOf(absent);
                result[1] = String.valueOf(calculatePercentage(present,absent));
                return result;

            default:

                return result;
        }
    }

    public void removeAll(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "DELETE FROM " + SUBJECT_TABLENAME;
        sqLiteDatabase.execSQL(query);
        query = "DELETE FROM " + ATTENDANCE_TABLENAME;
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public void removeSubject(int subjectId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SUBJECT_TABLENAME,SUBJECT_ID+"="+subjectId,null);
        db.delete(ATTENDANCE_TABLENAME,ATTENDANCE_SUBJECT_ID+"="+subjectId,null);
    }
}
