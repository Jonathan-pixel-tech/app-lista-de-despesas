package com.example.android.applistatarefa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity2 extends AppCompatActivity {
    private EditText edtTarefa;
    private Button btnSalvar;
    private EditText edtData;
    private EditText edtValor;
    private SQLiteDatabase bd;
    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtTarefa = findViewById(R.id.edtTarefa);
        edtData = findViewById(R.id.edtData);
        edtValor = findViewById(R.id.edtValor);
        btnSalvar = findViewById(R.id.btnSalvar);

        try {
            bd = openOrCreateDatabase("tarefas", MODE_PRIVATE, null);
        }catch (Exception e){
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            tarefa = (Tarefa) bundle.getSerializable("tarefa");
            edtTarefa.setText(tarefa.getNome());
            edtData.setText(tarefa.getData());
            edtValor.setText(tarefa.getValor().toString());
        }
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edtTarefa.getText().toString();
                String data = edtData.getText().toString();
                String valor = edtValor.getText().toString();
                String virgula = ",";
                if (tarefa != null){
                    bd.execSQL("UPDATE tarefas SET nome = ('" + nome + "')" +
                            "WHERE id = " + tarefa.getId());
                    Toast.makeText(getApplicationContext(), "Tarefa alterada com sucesso.", Toast.LENGTH_SHORT).show();
                }
                else{
                    bd.execSQL("INSERT INTO tarefas (nome, data, valor) " +
                            "VALUES ('" + nome + "' "+ virgula +" '"+ data +"' "+virgula+" '"+valor+"' )");
                    Toast.makeText(getApplicationContext(), "Tarefa gravada com sucesso.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

}