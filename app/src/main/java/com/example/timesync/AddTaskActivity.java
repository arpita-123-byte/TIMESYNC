package com.example.timesync;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private CardView btnClass, btnExam, btnLab, btnAssignment, btnPresentation;
    private CardView subjectSelector, topicInput, btnAddTaskFinal;
    private EditText etSubject, etTopic;
    private TextView dateField, timeField;
    private View navHome, navCalendar, navAdd, navTasks, navMenu;

    private Calendar selectedDate = Calendar.getInstance();
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initViews();
        setupListeners();
        setupNavigation();
        
        // Default to Class category
        setCategorySelected(btnClass);
    }

    private void initViews() {
        btnClass = findViewById(R.id.btnClass);
        btnExam = findViewById(R.id.btnExam);
        btnLab = findViewById(R.id.btnLab);
        btnAssignment = findViewById(R.id.btnAssignment);
        btnPresentation = findViewById(R.id.btnPresentation);

        subjectSelector = findViewById(R.id.subjectSelector);
        topicInput = findViewById(R.id.topicInput);
        etSubject = findViewById(R.id.etSubject);
        etTopic = findViewById(R.id.etTopic);
        dateField = findViewById(R.id.dateField);
        timeField = findViewById(R.id.timeField);
        btnAddTaskFinal = findViewById(R.id.btnAddTaskFinal);

        navHome = findViewById(R.id.navHome);
        navCalendar = findViewById(R.id.navCalendar);
        navAdd = findViewById(R.id.navAdd);
        navTasks = findViewById(R.id.navTasks);
        navMenu = findViewById(R.id.navMenu);
    }

    private void setupListeners() {
        // Category selection
        btnClass.setOnClickListener(v -> setCategorySelected(btnClass));
        btnExam.setOnClickListener(v -> setCategorySelected(btnExam));
        btnLab.setOnClickListener(v -> setCategorySelected(btnLab));
        btnAssignment.setOnClickListener(v -> setCategorySelected(btnAssignment));
        btnPresentation.setOnClickListener(v -> setCategorySelected(btnPresentation));

        // Subject selection
        subjectSelector.setOnClickListener(v -> {
            // Focus on the edit text
            etSubject.requestFocus();
        });

        // Topic input
        topicInput.setOnClickListener(v -> {
            // Focus on the edit text
            etTopic.requestFocus();
        });

        // Date picker
        dateField.setOnClickListener(v -> showDatePicker());

        // Time picker
        timeField.setOnClickListener(v -> showTimePicker());

        // Add task button
        btnAddTaskFinal.setOnClickListener(v -> {
            if (validateInputs()) {
                saveTask();
                finish(); // Return to previous screen
            }
        });
    }

    private boolean validateInputs() {
        String subject = etSubject.getText().toString().trim();
        String topic = etTopic.getText().toString().trim();
        
        if (subject.isEmpty()) {
            Toast.makeText(this, "Please enter a subject", Toast.LENGTH_SHORT).show();
            etSubject.requestFocus();
            return false;
        }
        
        if (topic.isEmpty()) {
            Toast.makeText(this, "Please enter a topic/chapter name", Toast.LENGTH_SHORT).show();
            etTopic.requestFocus();
            return false;
        }
        
        return true;
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitiesActivity.class);
            startActivity(intent);
            finish();
        });

        navCalendar.setOnClickListener(v -> {
            Toast.makeText(this, "Calendar", Toast.LENGTH_SHORT).show();
        });

        navTasks.setOnClickListener(v -> {
            Toast.makeText(this, "Tasks", Toast.LENGTH_SHORT).show();
        });

        navMenu.setOnClickListener(v -> {
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
        });
    }

    private void setCategorySelected(CardView selectedCard) {
        // Reset all categories to default appearance
        resetAllCategories();
        
        // Highlight selected category
        selectedCard.setCardElevation(8f); // Increase elevation for selected category
        
        // Set the selected category
        if (selectedCard == btnClass) {
            selectedCategory = "Class";
        } else if (selectedCard == btnExam) {
            selectedCategory = "Exam";
        } else if (selectedCard == btnLab) {
            selectedCategory = "Lab";
        } else if (selectedCard == btnAssignment) {
            selectedCategory = "Assignment";
        } else if (selectedCard == btnPresentation) {
            selectedCategory = "Presentation";
        }
    }

    private void resetAllCategories() {
        btnClass.setCardElevation(0f);
        btnExam.setCardElevation(0f);
        btnLab.setCardElevation(0f);
        btnAssignment.setCardElevation(0f);
        btnPresentation.setCardElevation(0f);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, monthOfYear);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateLabel();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDate.set(Calendar.MINUTE, minute);
                    updateTimeLabel();
                },
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void updateDateLabel() {
        String dateFormat = "EEE d, MMMM, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateField.setText(sdf.format(selectedDate.getTime()));
    }

    private void updateTimeLabel() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        timeField.setText(sdf.format(selectedDate.getTime()));
    }

    private void saveTask() {
        // Get input values
        String subject = etSubject.getText().toString().trim();
        String topic = etTopic.getText().toString().trim();
        
        // Here you would save the task to your database or shared preferences
        // This is just a placeholder that shows a toast message
        String message = "New " + selectedCategory + " task added: " + subject + " - " + topic;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
} 