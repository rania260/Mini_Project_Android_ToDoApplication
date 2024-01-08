package com.example.miniprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialisation des éléments de l'interface utilisateur
        addNoteBtn =findViewById(R.id.floatingActionButton);
        recyclerView=findViewById(R.id.recycleView);
        menuBtn=findViewById(R.id.imageButton2);
        // Configuration des écouteurs d'événements: btn add note et menu qui contient logout
        addNoteBtn.setOnClickListener(v-> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)) );
        menuBtn.setOnClickListener(v ->showMenu() );
        // Configuration du RecyclerView
        setupRecycleView();
    }
    // Méthode pour afficher le menu: contient seulement logout pour déconnécter et rediriger vers connexion activity
    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // if we click on logout ==> direction connexion activity
                if (item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, ConnexionActivity2.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    // Méthode pour configurer le RecyclerView
    void setupRecycleView(){
        Query query;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Construction de la requête pour obtenir les notes de l'utilisateur actuel, triées par timestamp: temps de création
        query = FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes").orderBy("timestamp", Query.Direction.DESCENDING);
        // Configuration des options pour FirestoreRecyclerAdapter
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        // Configuration du RecyclerView avec un LinearLayoutManager et un adaptateur (NoteAdapter)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Commence à écouter les modifications dans la base de données Firestore
        noteAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Arrête d'écouter les modifications lorsque l'activité est arrêtée
        noteAdapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Notification à l'adaptateur lorsque l'activité reprend le focus
        noteAdapter.notifyDataSetChanged();
    }

}