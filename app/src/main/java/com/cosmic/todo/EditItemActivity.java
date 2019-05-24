package com.cosmic.todo;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {
    public static final String ITEM_NAME = "todo-item";
    TodoDBFunctions dbfunctions;
    EditText etTitle;
    EditText etDate;
    EditText etNotes;
    TodoBean tb ;
    Calendar myCalendar;
    public static final String TAG = "EditItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_edit_item);
        setContentView(R.layout.activity_edit_item);
        dbfunctions = new TodoDBFunctions(getApplicationContext());
        tb = (TodoBean) getIntent().getSerializableExtra(ITEM_NAME);
        etTitle = (EditText) findViewById(R.id.editTitle);
        etTitle.setText(tb.getTitle_todo());

        etNotes = (EditText) findViewById(R.id.editNotes);
        etNotes.setText(tb.getNotes());

        etDate = (EditText) findViewById(R.id.editDate);
        etDate.setFocusable(false);
        etDate.setText(tb.getDate());
        myCalendar = Calendar.getInstance();

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                DatePickerDialog pickerDialog = new DatePickerDialog(EditItemActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000 );
                pickerDialog.show();

            }
        });



    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void finishFunc(View v){

        if(etTitle.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.emptyTitle),Toast.LENGTH_SHORT).show();
        }
        else {
            UpdateTodoTask updateTask = new UpdateTodoTask();
            tb.setTitle_todo(etTitle.getText().toString());
            tb.setNotes(etNotes.getText().toString());
            tb.setDate(etDate.getText().toString());
            updateTask.execute(tb);
            finish();
        }
    }

    public class UpdateTodoTask extends AsyncTask<TodoBean,Void,Boolean> {


        @Override
        protected Boolean doInBackground(TodoBean... params) {
            return dbfunctions.updateTodo(params[0]);
        }


        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if(result)
                Toast.makeText(getApplicationContext(),"Updated Todo",Toast.LENGTH_SHORT);
            else
                Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT);
        }
    }
}
