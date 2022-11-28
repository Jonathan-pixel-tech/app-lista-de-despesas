package com.example.android.applistatarefa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

                                AlertDialog.Builder dialog = new AlertDialog.Builder( MainActivity.this );

                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Realmente deseja excluir a despesa?");

                                dialog.setCancelable(false);
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Tarefa tarefa = listaTarefas.get(position);
                                        bd.execSQL("DELETE FROM tarefas WHERE id = " + tarefa.getId());
                                        listar();
                                        TarefaAdapter adapter = new TarefaAdapter(listaTarefas);
                                        rvTarefas.setAdapter(adapter);
                                        Toast.makeText(getApplicationContext(), "Tarefa excluida.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(getApplicationContext(), "OK! A tarefa não foi excluida.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.create();
                                dialog.show();



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
            bd.execSQL("ALTER TABLE tarefas ADD COLUMN data VARCHAR");
            bd.execSQL("ALTER TABLE tarefas ADD COLUMN valor REAL");
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public void listar() {
        try {
            String consulta = "SELECT id, nome, data, valor FROM tarefas";
            Cursor cursor = bd.rawQuery(consulta, null);
            int indiceId = cursor.getColumnIndex("id");
            int indiceNome = cursor.getColumnIndex("nome");
            int indiceData = cursor.getColumnIndex("data");
            int indiceValor = cursor.getColumnIndex("valor");

            cursor.moveToFirst();
            listaTarefas.clear();
            while (cursor != null) {
                int id = cursor.getInt(indiceId);
                String nome = cursor.getString(indiceNome);
                String data = cursor.getString(indiceData);
                Double valor = cursor.getDouble(indiceValor);
                Tarefa tarefa = new Tarefa (id, nome, data, valor);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.total, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSalvar:
                Double total = 0.0;
                for(Tarefa t: listaTarefas){
                    total += t.getValor();
                }
                Toast.makeText(getApplicationContext(), total.toString(), Toast.LENGTH_LONG).show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }
}