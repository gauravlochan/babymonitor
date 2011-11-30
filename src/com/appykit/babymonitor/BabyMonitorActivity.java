package com.appykit.babymonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BabyMonitorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    

		// Create a button click listener for the Transmitter button.
		Button btnTx = (Button) findViewById(R.id.txButton);
		btnTx.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BabyMonitorActivity.this.getApplication(), TransmitterActivity.class);
				startActivity(intent);
			}
		});
		
		// Create a button click listener for the Receiver button.
		Button btnRx = (Button) findViewById(R.id.rxButton);
		btnRx.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BabyMonitorActivity.this.getApplication(), ReceiverActivity.class);
				startActivity(intent);
			}
		});
    }
}