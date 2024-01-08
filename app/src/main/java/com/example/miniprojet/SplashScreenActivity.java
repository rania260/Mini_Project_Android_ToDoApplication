package com.example.miniprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Redirection vers la page principale "MainActivity" après un délai de 2 secondes.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Démarrer la page
                // Récupération de l'utilisateur actuellement connecté à Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                // Vérification de l'existence d'un utilisateur connecté
                if (currentUser==null){
                    // Si aucun utilisateur n'est connecté, rediriger vers la page de connexion (ConnexionActivity2)
                    startActivity(new Intent(SplashScreenActivity.this, ConnexionActivity2.class));
                }else{
                    // Si un utilisateur est connecté, rediriger vers la page principale (MainActivity)
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
                // Fermeture de l'activité SplashScreenActivity
                finish();
            }
        };
        // création d'un Handler post delayed: pour exécuter les instructions avec un certain delai (2sc)
        new Handler().postDelayed(runnable, 2000);
    }
}