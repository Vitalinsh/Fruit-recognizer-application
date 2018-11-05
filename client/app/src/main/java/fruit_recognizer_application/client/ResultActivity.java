package fruit_recognizer_application.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String message = getIntent().getStringExtra(MainActivity.RESULT_KEY);

        TextView textView = findViewById(R.id.resultText);
        textView.setText(message);
        setContentView(R.layout.activity_result);
    }
}
