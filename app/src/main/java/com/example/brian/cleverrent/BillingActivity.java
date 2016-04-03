package com.example.brian.cleverrent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.*;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;
import com.stripe.android.model.*;

import java.util.ArrayList;

import com.example.brian.cleverrent.AddCreditCardActivity.CreditCard;

public class BillingActivity extends AppCompatActivity {

    CreditCard[] creditCards = null;
    CreditCard selectedCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Billing");


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
                if (selectedCard != null) {
                    Card card = new Card(
                            selectedCard.getCardNumber(),
                            selectedCard.getExpMonth(),
                            selectedCard.getExpYear(),
                            selectedCard.getCvc());
                    Stripe stripe = null;
                    try {
                        stripe = new Stripe("pk_test_GCGdXvHzOEh3Be215XE3uPhH");
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }
                    if (stripe != null) {
                        stripe.createToken(card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    System.out.println(token);
                                }
                                public void onError(Exception error) {
                                    // Show localized error message
                                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        );
                    }

                    Dialog dialog = new Dialog(BillingActivity.this);
                    dialog.setContentView(R.layout.billing_submit_confirmation);
                    dialog.show();  //<-- See This!

                    Button confirmationButton = (Button) dialog.findViewById(R.id.confirmationButton);
                    confirmationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Close the activity
                            finish();
                        }
                    });
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        String cards = prefs.getString("CreditCards", null);
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        creditCards = gson.fromJson(cards, CreditCard[].class);
        RadioGroup creditCardRadioGroup = (RadioGroup) findViewById(R.id.creditCardRadioGroup);
        creditCardRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedCard = creditCards[checkedId-1];
            }
        });
        RelativeLayout creditLayout = (RelativeLayout) findViewById(R.id.creditCardView);

        if (creditCards != null) {
            for (int i = 0; i < creditCards.length; i++) {
                RadioButton card = new RadioButton(this);
                card.setText(creditCards[i].getCardNumber());
                creditCardRadioGroup.addView(card);
            }
        }
    }

}

