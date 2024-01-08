package com.example.miniprojet;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteViewHolder> {

    Context context;
    // Constructeur prenant des options FirestoreRecyclerOptions et le contexte
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context =context;
    }
    // Méthode appelée pour lier les données à la vue à une position spécifique
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // Mise à jour des éléments de la vue avec les données de la note
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestamplTextView.setText(Fonction.timestampToString(note.timestamp));
        // Configuration d'un écouteur de clic sur l'élément de la liste
        holder.itemView.setOnClickListener(v -> {
            // Création d'une intention pour ouvrir NoteDetailsActivity avec les données de la note
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            // Obtention de l'ID du document Firestore pour la note à cette position
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            // Démarrage de l'activité
            context.startActivity(intent);
        });
    }
    // Méthode appelée pour créer un ViewHolder lorsqu'il n'existe pas encore
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Création d'une nouvelle vue à partir du fichier de mise en page recycler_note_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        // Retourne un nouveau NoteViewHolder pour la vue créée
        return new NoteViewHolder(view);
    }
    // Classe interne représentant le ViewHolder pour chaque élément de la liste
    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, contentTextView,timestamplTextView;
        // Constructeur prenant une vue représentant un élément de la liste
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialisation des éléments de la vue avec les vues correspondantes dans le fichier de mise en page
            titleTextView= itemView.findViewById(R.id.note_title_text_view);
            contentTextView=itemView.findViewById(R.id.note_content_text_view);
            titleTextView=itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}
