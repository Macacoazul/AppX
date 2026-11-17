package com.example.appx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Adapter da lista de assuntos do momento (tela Explorar).
 */
public class AssuntoAdapter extends ArrayAdapter<Assunto> {

    private final LayoutInflater inflater;

    public AssuntoAdapter(@NonNull Context context, @NonNull List<Assunto> assuntos) {
        super(context, 0, assuntos);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_assunto, parent, false);
        }
        Assunto assunto = getItem(position);
        if (assunto != null) {
            TextView categoria = convertView.findViewById(R.id.categoriaAssunto);
            TextView titulo = convertView.findViewById(R.id.tituloAssunto);
            TextView posts = convertView.findViewById(R.id.postsAssunto);

            categoria.setText((position + 1) + " · " + assunto.getCategoria() + " · Assunto do Momento");
            titulo.setText(assunto.getTitulo());
            posts.setText(Formatador.contar(assunto.getQuantidadePosts()) + " posts");
        }
        return convertView;
    }
}
