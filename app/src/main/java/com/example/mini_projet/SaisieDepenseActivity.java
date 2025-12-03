package com.example.mini_projet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SaisieDepenseActivity extends AppCompatActivity {

    private EditText montantEditText, descriptionEditText, dateEditText;
    private RadioGroup categorieRadioGroup;
    private CheckBox importantCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie);

        montantEditText = findViewById(R.id.montantEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        categorieRadioGroup = findViewById(R.id.categorieRadioGroup);
        importantCheckBox = findViewById(R.id.importantCheckBox);

        Button validerButton = findViewById(R.id.validerButton);
        Button razButton = findViewById(R.id.razButton);

        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    Toast.makeText(SaisieDepenseActivity.this,
                            getString(R.string.donnees_enregistrees), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SaisieDepenseActivity.this, ResultatActivity.class);
                    intent.putExtra("montant", montantEditText.getText().toString());
                    intent.putExtra("description", descriptionEditText.getText().toString());
                    intent.putExtra("date", dateEditText.getText().toString());

                    int selectedId = categorieRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = findViewById(selectedId);
                    intent.putExtra("categorie", radioButton.getText().toString());
                    intent.putExtra("important", importantCheckBox.isChecked());

                    startActivityForResult(intent, 1);
                }
            }
        });

        razButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });
    }

    private boolean validateForm() {
        if (montantEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.champ_obligatoire), Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double montant = Double.parseDouble(montantEditText.getText().toString());
            if (montant <= 0) {
                Toast.makeText(this, getString(R.string.montant_positif), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Montant invalide", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (categorieRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, getString(R.string.select_categorie), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void resetForm() {
        montantEditText.setText("");
        descriptionEditText.setText("");
        dateEditText.setText("");
        categorieRadioGroup.clearCheck();
        importantCheckBox.setChecked(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                double total = data.getDoubleExtra("total", 0);
                String message = data.getStringExtra("message");

                Toast.makeText(this,
                        "Total: " + total + "€ - " + message,
                        Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Opération annulée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}