package com.example.brian.cleverrent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Billing");


        TextView accountOptions = (TextView) findViewById(R.id.bankAccountOptionsLabel);
        accountOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BillingActivity.this, AddBankAccountActivity.class);
                startActivity(intent);
            }
        });

        TextView creditCardOptions = (TextView) findViewById(R.id.creditCardOptionsLabel);
        creditCardOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BillingActivity.this, AddCreditCardActivity.class);
                startActivity(intent);
            }
        });


        Button button = (Button) findViewById(R.id.payButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(BillingActivity.this);
                dialog.setContentView(R.layout.billing_submit_confirmation);
                dialog.show();  //<-- See This!

                Button confirmationButton = (Button) dialog.findViewById(R.id.confirmationButton);
                confirmationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Close the activity
                        BillingActivity.this.finish();
                    }
                });

            }
        });

        String[] accountNames = {"Checking", "Savings"};
        String[] creditCardNames = {"Card 1", "Card 2"};
        
        RadioGroup accountRadioGroup = (RadioGroup) findViewById(R.id.accountRadioGroup);
        RadioGroup creditCardRadioGroup = (RadioGroup) findViewById(R.id.creditCardRadioGroup);
        RelativeLayout accountLayout = (RelativeLayout) findViewById(R.id.bankAccountView);
        RelativeLayout creditLayout = (RelativeLayout) findViewById(R.id.creditCardView);

        for (int i=0; i<accountNames.length; i++){
            RadioButton account = new RadioButton(this);
            RadioButton card = new RadioButton(this);
            card.setText(creditCardNames[i]);
            account.setText(accountNames[i]);
            accountRadioGroup.addView(account, i);
            creditCardRadioGroup.addView(card, i);
            if (i > 1) {
                accountLayout.getLayoutParams().height += 55;
                creditLayout.getLayoutParams().height += 55;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }

}

