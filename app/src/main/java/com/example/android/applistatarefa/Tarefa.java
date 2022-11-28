package com.example.android.applistatarefa;

import java.io.Serializable;

public class Tarefa implements Serializable {
    private int id;
    private String nome;
    private String data;
    private Double valor;

    public Tarefa() {
    }

    public Tarefa(int id, String nome, String data, Double valor) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.valor = valor;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
