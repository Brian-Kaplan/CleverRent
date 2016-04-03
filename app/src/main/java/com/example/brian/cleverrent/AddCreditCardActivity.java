package com.example.brian.cleverrent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AddCreditCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Credit Card");

        Button saveBtn = (Button) findViewById(R.id.saveCardButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cardNameEditText = (EditText) findViewById(R.id.cardNameEditText);
                EditText cardNumberEditText = (EditText) findViewById(R.id.cardNumberEditText);
                EditText securityCodeEditText = (EditText) findViewById(R.id.securityCodeEditText);
                EditText zipCodeEditText = (EditText) findViewById(R.id.zipCodeEditText);
                EditText cardProfileNameEditText = (EditText) findViewById(R.id.cardProfileNameEditText);
                EditText expirationMonthEditText = (EditText) findViewById(R.id.expirationMonthEditText);
                EditText expirationYearEditText = (EditText) findViewById(R.id.expirationYearEditText);

                CreditCard newCard = new CreditCard(
                        cardNameEditText.getText().toString(),
                        cardNumberEditText.getText().toString(),
                        securityCodeEditText.getText().toString(),
                        Integer.parseInt(expirationMonthEditText.getText().toString()),
                        Integer.parseInt(expirationYearEditText.getText().toString()));

                //Get a List of the cards and add the new one
                SharedPreferences prefs = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                String cards = prefs.getString("CreditCards", null);
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                CreditCard[] list  = gson.fromJson(cards, CreditCard[].class);
                if (list == null) {
                    list = new CreditCard[1];
                    list[0] = newCard;
                } else {
                    CreditCard[] newList = new CreditCard[list.length+1];
                    for (int i=0; i< list.length; i++){
                        newList[i] = list[i];
                    }
                    newList[list.length] = newCard;
                    list = newList;
                }

                String serialized = gson.toJson(list);
                SharedPreferences.Editor e = prefs.edit();
                e.putString("CreditCards", serialized);
                e.commit();
                finish();
            }
        });
    }

    public class CreditCard {
        private String cardName;
        private String cardNumber;
        private String cvc;
        private int expMonth;
        private int expYear;

        public CreditCard() {}

        public CreditCard(String cardName, String cardNumber, String cvc, int expMonth, int expYear) {
            this.cardName = cardName;
            this.cardNumber = cardNumber;
            this.cvc = cvc;
            this.expMonth = expMonth;
            this.expYear = expYear;
        }

        public String getCardName() {
            return cardName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public String getCvc() {
            return cvc;
        }

        public int getExpMonth() {
            return expMonth;
        }

        public int getExpYear() {
            return expYear;
        }
    }

}
