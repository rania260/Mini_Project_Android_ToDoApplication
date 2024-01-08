package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    TextView deleteNoteTextViewBtn;
    String title,content,docId;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        // Initialisation des éléments de l'interface utilisateur
        titleEditText =findViewById(R.id.NoteTitleText);
        contentEditText=findViewById(R.id.noteContentText);
        saveNoteBtn =findViewById(R.id.imageButton);
        pageTitleTextView = findViewById(R.id.textViewAddNote);
        deleteNoteTextViewBtn =findViewById(R.id.textViewDelete);

        // Récupération des données envoyées par l'activité précédente
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        // Vérification du mode (update ou create)
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }
        // Pré-remplissage des champs avec les données existantes (s'il s'agit d'une modification)
        titleEditText.setText(title);
        contentEditText.setText(content);
        // Configuration de l'interface utilisateur en fonction du mode
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }
        // Configuration des écouteurs d'événements pour les boutons: save and delete
        saveNoteBtn.setOnClickListener(v -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener(v-> deleteNoteFromFirebase());
    }
    // Méthode pour enregistrer une note dans Firebase
    void saveNote(){
        String noteTitle= titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        // Validation du titre
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Title is required");
            return;
        }
        // Création d'un objet Note avec les données saisies
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        // Enregistrement de la note dans Firebase
        saveNoteToFirebase(note);
    }
    //save and update
    //    // Méthode pour enregistrer ou mettre à jour une note dans Firebase
    void saveNoteToFirebase(Note note){
        DocumentReference doc;
        if(isEditMode){
            //// Vérification du mode (update ou create) et obtention de la référence du document
            //si il est en mode update
            doc = Fonction.getCollectionReferenceForNotes().document(docId);
        }else{
            //si il est en mode create
            doc = Fonction.getCollectionReferenceForNotes().document();
        }
        // Enregistrement ou mise à jour de la note dans Firebase
        doc.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Toast.makeText(NoteDetailsActivity.this,"Note added",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //note not added
                    Toast.makeText(NoteDetailsActivity.this,"Failed Adding Note",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Méthode pour supprimer une note de Firebase
    void deleteNoteFromFirebase(){
        DocumentReference doc;
        // Obtention de la référence du document à supprimer
        doc = Fonction.getCollectionReferenceForNotes().document(docId);
        // Suppression de la note de Firebase
        doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is deleted
                    Toast.makeText(NoteDetailsActivity.this,"Note deleted",Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    //note not added
                    Toast.makeText(NoteDetailsActivity.this,"Failed deleting Note",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}