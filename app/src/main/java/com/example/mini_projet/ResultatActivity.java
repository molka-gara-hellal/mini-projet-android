package com.example.mini_projet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResultatActivity extends AppCompatActivity {

    private TextView recapitulatifTextView, resultatTextView;
    private double montant;
    private String categorie, description, date;
    private boolean important;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        recapitulatifTextView = findViewById(R.id.recapitulatifTextView);
        resultatTextView = findViewById(R.id.resultatTextView);

        Button calculerButton = findViewById(R.id.calculerButton);
        Button validerButton = findViewById(R.id.validerRetourButton);
        Button annulerButton = findViewById(R.id.annulerButton);

        Intent intent = getIntent();
        montant = Double.parseDouble(intent.getStringExtra("montant"));
        description = intent.getStringExtra("description");
        date = intent.getStringExtra("date");
        categorie = intent.getStringExtra("categorie");
        important = intent.getBooleanExtra("important", false);

        String recapitulatif = "Récapitulatif :\n" +
                "Montant : " + montant + "€\n" +
                "Catégorie : " + categorie + "\n" +
                "Description : " + description + "\n" +
                "Date : " + date + "\n" +
                "Important : " + (important ? "Oui" : "Non");

        recapitulatifTextView.setText(recapitulatif);

        calculerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculerResultat();
            }
        });

        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("total", montant);

                if (montant > 100) {
                    resultIntent.putExtra("message", "Attention : dépense élevée !");
                } else {
                    resultIntent.putExtra("message", "Dépense raisonnable");
                }

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        annulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("message", "Annulé par l'utilisateur");
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }

    private void calculerResultat() {
        String message;
        double budgetMensuel = 1000; // Budget mensuel de référence

        if (montant > budgetMensuel * 0.3) {
            message = "⚠️ Dépense TRÈS IMPORTANTE (>30% budget)";
        } else if (montant > budgetMensuel * 0.1) {
            message = "📊 Dépense significative (10-30% budget)";
        } else {
            message = "✅ Dépense modérée (<10% budget)";
        }

        if (important) {
            message += "\n🔔 Cette dépense est marquée comme importante";
        }

        resultatTextView.setText(message);
        Toast.makeText(this, "Résultat calculé", Toast.LENGTH_SHORT).show();
    }
}