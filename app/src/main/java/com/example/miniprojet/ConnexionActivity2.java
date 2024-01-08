package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ConnexionActivity2 extends AppCompatActivity {
    // Déclaration des champs de l'interface utilisateur
    EditText email,password;
    Button btnConnect;
    ProgressBar progBar;
    TextView CreateAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion2);
        // Initialisation des champs de l'interface utilisateur
        email =findViewById(R.id.editTextEmailConn);
        password=findViewById(R.id.editTextPass);
        btnConnect=findViewById(R.id.buttonConnexion);
        progBar=findViewById(R.id.progressBar3);
        CreateAccountTextView=findViewById(R.id.textViewCreate);
        // Configuration des écouteurs d'événements pour les boutons: Connexion et create compte
        btnConnect.setOnClickListener(v -> connexionUser());
        CreateAccountTextView.setOnClickListener(v -> startActivity(new Intent(ConnexionActivity2.this, LoginActivity.class)));
    }
    // Méthode appelée lorsqu'un utilisateur appuie sur le bouton de connexion
    void connexionUser(){
        // Récupération des données saisies par l'utilisateur
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        // Validation des données saisies
        boolean isValidated = validateData(mail,pass);
        if (!isValidated){
            return;
        }
        // Connexion à un compte Firebase
        LoginFirebaseAccount(mail,pass);
    }
    // Méthode pour effectuer la connexion à un compte Firebase
    void LoginFirebaseAccount(String mail,String pass){
        // Instance de FirebaseAuth pour gérer l'authentification Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeProgress(true);
        firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeProgress(false);
                if(task.isSuccessful()){
                    //connexion avec succés
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainActivity si l'e-mail est vérifié
                        startActivity(new Intent(ConnexionActivity2.this, MainActivity.class));
                        finish();
                    }else{
                        // Affiche un message si l'e-mail n'est pas vérifié
                        Toast.makeText(ConnexionActivity2.this,"Email not verified, please verify your email",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //if failed connexion
                    Toast.makeText(ConnexionActivity2.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Méthode pour changer la visibilité de la ProgressBar
    void changeProgress(boolean inProgress){
        if(inProgress){
            // Si en cours, affiche la ProgressBar et masque le bouton de connexion
            progBar.setVisibility(View.VISIBLE);
            btnConnect.setVisibility(View.GONE);
        }else{
            // Sinon, masque la ProgressBar et affiche le bouton de connexion
            progBar.setVisibility(View.GONE);
            btnConnect.setVisibility(View.VISIBLE);
        }
    }

    //Create une méthode pour la validation des données entre par l'utilisateur
    boolean validateData(String mail, String pass){

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Email invalid");
            return false;
        }
        return true;
    }
}