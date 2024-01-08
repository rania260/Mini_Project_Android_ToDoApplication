package com.example.miniprojet;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
// Classe utilitaire Fonction
// On a créé cette classe juste pour faciliter l'écriture et réutiliser ses fonctions facilement.
public class Fonction {
    // Méthode statique pour obtenir la référence de collection pour les notes
    // Cette méthode renvoie la référence à la collection de notes spécifique à l'utilisateur actuel
    static CollectionReference getCollectionReferenceForNotes(){
        // Récupération de l'utilisateur actuellement connecté à Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Renvoie la référence à la collection "my_notes" sous le document de l'utilisateur actuel
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }
    // Méthode statique pour convertir un objet Timestamp en une chaîne de date au format MM/dd/yyyy
    static String timestampToString(Timestamp timestamp){
        // Utilisation de SimpleDateFormat pour formater la date
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
