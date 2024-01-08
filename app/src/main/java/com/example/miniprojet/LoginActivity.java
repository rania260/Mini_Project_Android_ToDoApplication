package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {
//On a déclaré la liste des variables des champs de l'interface nécessaire à LoginActivity (création du compte) pour le reste du travail
    EditText email,password,passConfirm;
    Button btnCreate;
    ProgressBar progBar;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// Initialisation des champs de l'interface creation du compte

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        passConfirm = findViewById(R.id.editTextPassConfirm);
        btnCreate =findViewById(R.id.buttonCreate);
        progBar = findViewById(R.id.progressBar);
        loginTextView = findViewById(R.id.textViewLogin);
        // Configuration des écouteurs d'événements pour les boutons Creation et login
        btnCreate.setOnClickListener(v -> createAccount());
        loginTextView.setOnClickListener(v-> finish());


    }
    // Lorsqu'on appuie sur le bouton de create, la méthode createAccount est appelée pour créer le compte.
    void createAccount(){
        // Récupération des données saisies par l'utilisateur
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String confirm = passConfirm.getText().toString();
        // Vérifier la Validation des données saisies
        boolean isValidated = validateData(mail,pass,confirm);
        if (!isValidated){
            return;
        }
        // Si li données son vérifier: Création du compte utilisateur dans Firebase
        createFirebaseAccount(mail,pass);
    }
    // Méthode pour créer un compte utilisateur dans Firebase
    void createFirebaseAccount(String mail,String pass){
        // On va changer la visibilité de la ProgressBar pendant la création du compte a true
        changeProgress(true);
        // Instance de FirebaseAuth pour gérer l'authentification Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //si le compte est crée avec succés on va afficher un message toast
                            Toast.makeText(LoginActivity.this,"Successfully create account,Check email to verify",Toast.LENGTH_SHORT).show();
                            // Envoie un e-mail de vérification à l'utilisateur
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            // Déconnexion de l'utilisateur actuel
                            firebaseAuth.signOut();
                            // Fermeture de l'activité
                            finish();
                        }else{
                            //if la création du compte est échoue, on va afficher un message Toast avec l'erreur
                            Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //méthode pour changer la visibilité du progressBar lors du création du compte
    void changeProgress(boolean inProgress){
        if(inProgress){
            // Si le création en cours, on va afficher la ProgressBar et masque le bouton de création
            progBar.setVisibility(View.VISIBLE);
            btnCreate.setVisibility(View.GONE);
        }else{
            // Sinon, on va masque la ProgressBar et afficher le bouton de création
            progBar.setVisibility(View.GONE);
            btnCreate.setVisibility(View.VISIBLE);
        }
    }

    //Create une méthode pour la validation des données saisies par l'utilisateur
    boolean validateData(String mail, String pass,String confirm){
        // Validation de l'adresse e-mail
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Email invalid");
            return false;
        }
        // Validation de la correspondance des mots de passe
        if (!pass.equals(confirm)){
            passConfirm.setError("Password not matched");
            return false;
        }
        return true;
    }
}