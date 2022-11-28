package com.example.android.applistatarefa;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;
    private RecyclerView rvTarefas;
    private SQLiteDatabase bd;
    private List<Tarefa> listaTarefas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criarBd();
        criarTabela();

        fabAdd = findViewById(R.id.fabAdd);
        rvTarefas = findViewById(R.id.rvTarefas);

        rvTarefas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvTarefas.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvTarefas.setHasFixedSize(true);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,
                        MainActivity2.class));
            }
        });

        rvTarefas.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        rvTarefas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Tarefa tarefa = listaTarefas.get(position);
                                // Toast.makeText(MainActivity.this, "Item pressionado: " + tarefa.getNome(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                intent.putExtra("tarefa", tarefa);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Tarefa tarefa = listaTarefas.get(position);
                                bd.execSQL("DELETE FROM tarefas WHERE id = " + tarefa.getId());
                                listar();
                                TarefaAdapter adapter = new TarefaAdapter(listaTarefas);
                                rvTarefas.setAdapter(adapter);
                                Toast.makeText(MainActivity.this, "Tarefa excluida.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    public void criarBd(){
        try {
            bd = openOrCreateDatabase("tarefas", MODE_PRIVATE, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void criarTabela(){
        try {
            bd.execSQL("CREATE TABLE IF NOT EXISTS tarefas (id INTEGER PRIMARY KEY autoincrement, nome VARCHAR)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listar() {
        try {
            String consulta = "SELECT id, nome FROM tarefas";
            Cursor cursor = bd.rawQuery(consulta, null);
            int indiceId = cursor.getColumnIndex("id");
            int indiceNome = cursor.getColumnIndex("nome");
            cursor.moveToFirst();
            listaTarefas.clear();
            while (cursor != null) {
                int id = cursor.getInt(indiceId);
                String nome = cursor.getString(indiceNome);
                Tarefa tarefa = new Tarefa (id, nome);
                listaTarefas.add(tarefa);
                cursor.moveToNext();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        listar();
        TarefaAdapter adapter = new TarefaAdapter(listaTarefas);
        rvTarefas.setAdapter(adapter);
    }
}