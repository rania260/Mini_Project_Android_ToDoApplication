package com.example.miniprojet;

import com.google.firebase.Timestamp;
// Classe modèle représentant une note
public class Note {
    // Attributs de la note
    String title;
    String content;
    Timestamp timestamp;
// Constructeur, getter and setter
    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}