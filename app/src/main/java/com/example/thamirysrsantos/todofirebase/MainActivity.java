package com.example.thamirysrsantos.todofirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.thamirysrsantos.todofirebase.modelo.Tarefa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private ListView listView;
    private List<Tarefa> listaTarefa = new ArrayList<Tarefa>();
    private ArrayAdapter<Tarefa> arrayAdapterTarefa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        conectarBanco();
        eventoBanco();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAdicionar) {
            //Opções a serem feitas ao clicar no botão adicionar
            Intent intent = new Intent(this, AdicionarActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menuFavoritos) {
            //Opções a serem feitas ao clicar no botão favoritos

        }
        return super.onOptionsItemSelected(item);
    }

    public void conectarBanco() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventoBanco() {
        databaseReference.child("Tarefa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           listaTarefa.clear();
           for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
               Tarefa tarefa = snapshot.getValue(Tarefa.class);
               listaTarefa.add(tarefa);
           }
            arrayAdapterTarefa = new ArrayAdapter<Tarefa>(MainActivity.this, R.layout.layout_lista, R.id.tvNomeTarefa, listaTarefa);
            listView.setAdapter(arrayAdapterTarefa);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                //Clique longo
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Tarefa tarefaSelecionada = (Tarefa)parent.getItemAtPosition(position);
                    databaseReference.child("Tarefa").child(tarefaSelecionada.getUid()).removeValue();
                    return false;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                //Clique curto
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Tarefa tarefaSelecionada = (Tarefa)parent.getItemAtPosition(position);
                    Intent intent= new Intent(MainActivity.this, AdicionarActivity.class);
                    intent.putExtra("uid", tarefaSelecionada.getUid());
                    intent.putExtra("nome", tarefaSelecionada.getNome());
                    startActivity(intent);
                }
            });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
