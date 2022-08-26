package com.hfad.myconstraintlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.hfad.myconstraintlayout.data.RadioLab;
import android.widget.EditText;
import com.hfad.myconstraintlayout.data.Radio;
import java.util.UUID;

public class AddRadioActivity extends AppCompatActivity {

    private EditText mEditRadio;
    private EditText mEditTextAddres;
    private String editId;
    private Radio editRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_radio);

         editId=getIntent().getStringExtra("mainActivityEditId");

         mEditRadio=(EditText)findViewById(R.id.editRadioText);
         mEditTextAddres=(EditText)findViewById(R.id.editTextAddres);

         if (editId!=null){
            editRadio = getRadioFromBase(editId);
         }

        Button buttonOk=(Button) findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //запись в базу
                if (editId!=null){

                     editRadioBase(editRadio);

                }else {

                    addToBase();
                }

                finish();
            }
        });

        Button buttonCancel=(Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //выход
                finish();
            }
        });

    }


    private void addToBase(){

        Radio radio = new Radio();
        radio.setName(mEditRadio.getText().toString());
        radio.setAddres(mEditTextAddres.getText().toString());
        radio.setPlayed(false);
        RadioLab.get((AddRadioActivity.this)).addRadio(radio);

    }

    private Radio getRadioFromBase(String ID){

       Radio radio = RadioLab.get((AddRadioActivity.this)).getRadio(UUID.fromString(ID));

       mEditRadio.setText(radio.getName());
       mEditTextAddres.setText(radio.getAddres());

       return radio;
    }

    private void  editRadioBase(Radio radio){

        radio.setName(mEditRadio.getText().toString());
        radio.setAddres(mEditTextAddres.getText().toString());

        RadioLab.get((AddRadioActivity.this)).updateRadio(radio);
    }

}
