package com.example.projectakhir;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;
    public interface OnDeleteClickListener {
        void onDeleteClick(Contact contact);
    }
    public interface OnEditClickListener {
        void onEditClick(Contact contact);
    }
    public ContactAdapter(List<Contact> contactList, OnDeleteClickListener onDeleteClickListener,OnEditClickListener onEditClickListener) {
        this.contactList = contactList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.textViewName.setText(contact.getName());
        holder.textViewPhone.setText(contact.getPhone());
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClickListener.onEditClick(contact);
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewPhone;
        public ImageButton buttonDelete, buttonEdit;
        public ContactViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.text_view_name);
            textViewPhone = view.findViewById(R.id.text_view_phone);
            buttonEdit = view.findViewById(R.id.button_edit);
            buttonDelete = view.findViewById(R.id.button_delete);
        }
    }
}
