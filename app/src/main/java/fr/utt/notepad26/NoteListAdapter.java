package fr.utt.notepad26;

import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>{
    private List<NoteListEntry> noteList;

    public class NoteListViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle, noteDate;

        public NoteListViewHolder(View view) {
            super(view);
            noteTitle = view.findViewById(R.id.noteTitle);
            noteDate = view.findViewById(R.id.noteDate);
        }
    }


    public NoteListAdapter(List<NoteListEntry> noteList) {
        this.noteList = noteList;
    }

    @Override
    public NoteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new NoteListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteListViewHolder holder, int position) {
        NoteListEntry note = noteList.get(position);
        holder.noteTitle.setText(note.getName());
        holder.noteDate.setText(note.getNoteShortDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
