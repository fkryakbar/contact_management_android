package com.example.projectakhir;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnDeleteClickListener {

    private FloatingActionButton fabAddContact;
    private ContactDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fabAddContact = findViewById(R.id.fab_add_contact);
        recyclerView = findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new ContactDatabaseHelper(this);

        dbHelper = new ContactDatabaseHelper(this);
        contactList = new ArrayList<>();

        loadContactsFromDatabase();
        Log.d("debuging", "debug");

        contactAdapter = new ContactAdapter(contactList, this, new ContactAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(Contact contact) {
                showEditContactDialog(contact);
            }
        });
        recyclerView.setAdapter(contactAdapter);

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });
    }

    @Override
    public void onDeleteClick(Contact contact) {
        dbHelper.deleteContact(contact.getId());
        contactList.remove(contact);
        contactAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        EditText editTextPhone = dialogView.findViewById(R.id.edit_text_phone);
        Button buttonAdd = dialogView.findViewById(R.id.button_add);

        AlertDialog alertDialog = builder.create();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (!name.isEmpty() && !phone.isEmpty()) {
                    dbHelper.addContact(name, phone);
                    Toast.makeText(MainActivity.this, "Contact Added", Toast.LENGTH_SHORT).show();
                    loadContactsFromDatabase();
                    contactAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();

                    // Optional: Reload contacts from database

                } else {
                    Toast.makeText(MainActivity.this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }
    private void loadContactsFromDatabase() {
        contactList.clear();
        Cursor cursor = dbHelper.getAllContacts();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                contactList.add(new Contact(id, name, phone));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void showEditContactDialog(final Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_contact, null);
        builder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.update_text_name);
        final EditText editTextPhone = dialogView.findViewById(R.id.update_text_phone);
        Button buttonAdd = dialogView.findViewById(R.id.button_update);

        // Pre-fill the dialog with the contact's current details
        editTextName.setText(contact.getName());
        editTextPhone.setText(contact.getPhone());

        AlertDialog alertDialog = builder.create();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (!name.isEmpty() && !phone.isEmpty()) {
                    dbHelper.updateContact(contact.getId(), name, phone);
                    Toast.makeText(MainActivity.this, "Contact Updated", Toast.LENGTH_SHORT).show();
                    loadContactsFromDatabase();
                    contactAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }


}