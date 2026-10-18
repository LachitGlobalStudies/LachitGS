package com.adnan.lgstudies.Activity;


import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.QuestionsAndAnswer;
import com.adnan.lgstudies.Adapters.UserResponseAndResult;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

public class TestStartActivity extends AppCompatActivity {
    private CircularProgressBar countdownProgressBar;
    private RadioGroup options;
    private TextView questionText, totalQuestions,countdownTextView;
    RadioButton A_option, B_option, C_option, D_option;
    private Button nextQuestion, finalSubmit, clearResponse, previous_button;
    private DatabaseReference databaseReference,testRank,time_duration;
    private List<QuestionsAndAnswer> questions;
    private int currentQuestionIndex;
    private int score = 0;
    private List<UserResponseAndResult> userResponses;
    private CountDownTimer countDownTimer;
    ImageView questionImage;
    private long totalTimeInMillis =0; // 30 minutes (adjust as needed)
String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_test_start);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
        // Initialize UI elements
        options = findViewById(R.id.all_Options);
        questionText = findViewById(R.id.question_text);
        questionImage=findViewById(R.id.image_question);
        totalQuestions = findViewById(R.id.total_question);
        countdownTextView=findViewById(R.id.countdown_textview);
        nextQuestion = findViewById(R.id.next_btn);
        finalSubmit = findViewById(R.id.submit_test);
        clearResponse = findViewById(R.id.clear_response);
        previous_button = findViewById(R.id.previous_btn);
        countdownProgressBar=findViewById(R.id.progress_circular);
        A_option = findViewById(R.id.optionA);
        B_option = findViewById(R.id.optionB);
        C_option = findViewById(R.id.optionC);
        D_option = findViewById(R.id.optionD);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Test Series")
                .child(getIntent().getStringExtra("batch_name")).child(getIntent().getStringExtra("test_series"))
                .child("Questions"); // Typo in "Questions"

        questions = new ArrayList<>();
        userResponses = new ArrayList<>();
        currentQuestionIndex = 0;
        score = 0;
        time_duration=FirebaseDatabase.getInstance().getReference().child("Test Series")
                .child(getIntent().getStringExtra("batch_name")).child(getIntent().getStringExtra("test_series"));
        time_duration.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("time duration")){
                    totalTimeInMillis=snapshot.child("time duration").getValue(long.class);
                    startCountdownTimer();
                }else {
                    totalTimeInMillis=60*60*1000;
                    startCountdownTimer();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Set up event listeners
        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedOption = findViewById(options.getCheckedRadioButtonId());
                if (selectedOption != null) {
                    String answerText = selectedOption.getText().toString().substring(2);
                    QuestionsAndAnswer current_question = questions.get(currentQuestionIndex);
                    String correct_answer = current_question.getCorrectAnswer().toString();

                    UserResponseAndResult currentResponse = userResponses.get(currentQuestionIndex);

                    if (answerText.equals(correct_answer)) {
                        Object tag = selectedOption.getTag();
                        String selectedAnswer = tag.toString();
                        // Update the existing response in the list
                        currentResponse.setCharacter(selectedAnswer);
                        currentResponse.setObtained_mark(1);
                    } else {
                        Object tag = selectedOption.getTag();
                        String selectedAnswer = tag.toString();
                        // Update the existing response in the list
                        currentResponse.setCharacter(selectedAnswer);
                        currentResponse.setObtained_mark(0);
                    }
                }
            }
        });


        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousQuestion();
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextQuestion();
            }
        });

        finalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubmitConfirmationDialog();
            }
        });

        clearResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                options.clearCheck();
            }
        });

        // Load questions from Firebase
        loadQuestionsFromFirebase();
    }

    private void loadQuestionsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionsAndAnswer question = dataSnapshot.getValue(QuestionsAndAnswer.class);
                        questions.add(question);
                    }
                    for (int i = 0; i < questions.size(); i++) {
                        UserResponseAndResult data = new UserResponseAndResult(i, "", 0);
                        userResponses.add(data);
                    }
                    // Display the first question when data is loaded
                    showQuestion(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TestStartActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showQuestion(int questionIndex) {
        if (questionIndex < questions.size()) {
            currentQuestionIndex = questionIndex;
            QuestionsAndAnswer currentQuestion = questions.get(questionIndex);
            questionText.setText(currentQuestion.getQuestion());
            totalQuestions.setText("Question " + (questionIndex + 1) + " of " + questions.size());
            A_option.setText("A "+currentQuestion.getOptionA());
            B_option.setText("B "+currentQuestion.getOptionB());
            C_option.setText("C "+currentQuestion.getOptionC());
            D_option.setText("D "+currentQuestion.getOptionD());
            Glide.with(getApplicationContext()).load(currentQuestion.getQuestion_image()).into(questionImage);

            // Load the user's previous response for this question
            UserResponseAndResult userResponse = userResponses.get(questionIndex);
            String selectedResponse = userResponse.getCharacter();

            if (!selectedResponse.isEmpty()) {
                RadioButton selectedRadioButton = options.findViewWithTag(selectedResponse);
                if (selectedRadioButton != null) {
                    selectedRadioButton.setChecked(true);
                }
            } else {
                options.clearCheck();
            }
        } else {
            // Quiz completed
            questionText.setText("Quiz completed!");
        }
    }

    private void submitTest() {
        int totalScore = 0;
        for (UserResponseAndResult userResponse : userResponses) {
            totalScore += userResponse.getObtained_mark();
        }


        testRank=FirebaseDatabase.getInstance().getReference().child("Test Rank").child(getIntent().getStringExtra("batch_name")).child(getIntent().getStringExtra("test_series")).child(mobile);
        String push_key=testRank.push().getKey().toString();
        testRank.child(push_key).setValue(totalScore);
        Intent intent=new Intent(TestStartActivity.this,ResultActivity.class);
        intent.putExtra("batch_name",getIntent().getStringExtra("batch_name"));
        intent.putExtra("test_series",getIntent().getStringExtra("test_series"));
        intent.putExtra("total_score",+totalScore);
        startActivity(intent);
        finish();
    }


    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            // Save the user's response before moving to the previous question
            saveUserResponse(currentQuestionIndex);

            previous_button.setEnabled(true);
            nextQuestion.setEnabled(true);
            showQuestion(currentQuestionIndex - 1);
        } else {
            previous_button.setEnabled(false);
            nextQuestion.setEnabled(true);
        }
    }

    private void saveUserResponse(int questionIndex) {
        RadioButton selectedOption = findViewById(options.getCheckedRadioButtonId());
        if (selectedOption != null) {
            String selectedAnswer = selectedOption.getTag().toString();
            UserResponseAndResult userResponse = userResponses.get(questionIndex);
            userResponse.setCharacter(selectedAnswer);
        } else {
            // Handle the case where no option is selected if necessary
        }
    }


    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            // Save the user's response before moving to the next question
            saveUserResponse(currentQuestionIndex);

            nextQuestion.setEnabled(true);
            previous_button.setEnabled(true);
            showQuestion(currentQuestionIndex + 1);
        } else {
            previous_button.setEnabled(true);
            nextQuestion.setEnabled(false);
            Toast.makeText(this, "No more questions!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showSubmitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to submit the test?");

        // Calculate the number of questions checked and remaining
        int questionsChecked = 0;
        for (UserResponseAndResult response : userResponses) {
            if (!response.getCharacter().isEmpty()) {
                questionsChecked++;
            }
        }
        int questionsRemaining = questions.size() - questionsChecked;

        builder.setMessage("You have checked =" + questionsChecked +" questions\n Remaining Question = " + questionsRemaining + " questions");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the submission here
                submitTest();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer text with the remaining time
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                // Update a TextView with the remaining time
                countdownTextView.setText(timeLeftFormatted);
                int progress = (int) (millisUntilFinished * 100 / totalTimeInMillis);
                countdownProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                countdownProgressBar.setVisibility(View.INVISIBLE);
                // Handle the timer expiration here (e.g., submit the test)
                countdownTextView.setText("Time's up!");
                // You can also trigger the submission of the test here if needed
                submitTest();
            }
        };
        countDownTimer.start();
    }


}
