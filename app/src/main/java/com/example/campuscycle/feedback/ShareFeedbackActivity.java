package com.example.campuscycle.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campuscycle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShareFeedbackActivity extends AppCompatActivity {

    private EditText editFeedback;
    private Button btnSend;
    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share_feedback);

        // Apply insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editFeedback = findViewById(R.id.editFeedback);
        btnSend = findViewById(R.id.btnSend);

        // Reference to "feedback" node
        feedbackRef = FirebaseDatabase.getInstance().getReference("feedback");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    private void sendFeedback() {
        String message = editFeedback.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show();
            return;
        }

        // Push message into database
        String id = feedbackRef.push().getKey();
        if (id != null) {
            feedbackRef.child(id).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Feedback sent", Toast.LENGTH_SHORT).show();
                        editFeedback.setText("");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
