package com.example.appx;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * Adapter que transforma cada objeto Post em uma linha (item_post.xml) da ListView.
 */
public class PostAdapter extends ArrayAdapter<Post> {

    private final LayoutInflater inflater;
    private final int corCinza;
    private final int corRosa;
    private final int corVerde;
    private final int corAzul;

    public PostAdapter(@NonNull Context context, @NonNull List<Post> posts) {
        super(context, 0, posts);
        inflater = LayoutInflater.from(context);
        corCinza = ContextCompat.getColor(context, R.color.x_cinza);
        corRosa = ContextCompat.getColor(context, R.color.x_rosa);
        corVerde = ContextCompat.getColor(context, R.color.x_verde);
        corAzul = ContextCompat.getColor(context, R.color.x_azul);
    }

    /** Guarda as referências das Views para não chamar findViewById a cada rolagem. */
    private static class ViewHolder {
        final TextView avatar, nome, info, texto;
        final TextView qtdRespostas, qtdReposts, qtdCurtidas, qtdVisualizacoes;
        final ImageView verificado, btnMais, imagem, iconeRepost, iconeCurtida, btnSalvar, btnCompartilhar;
        final View btnResponder, btnRepostar, btnCurtir, btnVisualizacoes;

        ViewHolder(View v) {
            avatar = v.findViewById(R.id.avatar);
            nome = v.findViewById(R.id.nome);
            info = v.findViewById(R.id.info);
            texto = v.findViewById(R.id.texto);
            qtdRespostas = v.findViewById(R.id.qtdRespostas);
            qtdReposts = v.findViewById(R.id.qtdReposts);
            qtdCurtidas = v.findViewById(R.id.qtdCurtidas);
            qtdVisualizacoes = v.findViewById(R.id.qtdVisualizacoes);
            verificado = v.findViewById(R.id.verificado);
            btnMais = v.findViewById(R.id.btnMais);
            imagem = v.findViewById(R.id.imagem);
            iconeRepost = v.findViewById(R.id.iconeRepost);
            iconeCurtida = v.findViewById(R.id.iconeCurtida);
            btnSalvar = v.findViewById(R.id.btnSalvar);
            btnCompartilhar = v.findViewById(R.id.btnCompartilhar);
            btnResponder = v.findViewById(R.id.btnResponder);
            btnRepostar = v.findViewById(R.id.btnRepostar);
            btnCurtir = v.findViewById(R.id.btnCurtir);
            btnVisualizacoes = v.findViewById(R.id.btnVisualizacoes);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_post, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Post post = getItem(position);
        if (post != null) {
            preencher(holder, post);
        }
        return convertView;
    }

    private void preencher(ViewHolder h, Post post) {
        Context context = getContext();

        Formatador.aplicarAvatar(h.avatar, post.getIniciais(), post.getCorAvatar());
        h.nome.setText(post.getNome());
        h.verificado.setVisibility(post.isVerificado() ? View.VISIBLE : View.GONE);
        h.info.setText(post.getUsuario() + " · " + Formatador.tempoRelativo(post.getCriadoEm()));
        h.texto.setText(Formatador.destacar(post.getTexto(), corAzul));

        if (post.getImagemRes() != 0) {
            h.imagem.setVisibility(View.VISIBLE);
            h.imagem.setImageResource(post.getImagemRes());
        } else {
            h.imagem.setVisibility(View.GONE);
        }

        h.qtdRespostas.setText(Formatador.contar(post.getRespostas()));
        h.qtdVisualizacoes.setText(Formatador.contar(post.getVisualizacoes()));
        atualizarRepost(h, post);
        atualizarCurtida(h, post);
        atualizarSalvo(h, post);

        // Eventos de clique de cada ação
        h.btnCurtir.setOnClickListener(v -> {
            post.alternarCurtida();
            atualizarCurtida(h, post);
            if (post.isCurtido()) Formatador.pular(h.iconeCurtida);
        });

        h.btnRepostar.setOnClickListener(v -> {
            post.alternarRepost();
            atualizarRepost(h, post);
            if (post.isRepostado()) Formatador.pular(h.iconeRepost);
        });

        h.btnSalvar.setOnClickListener(v -> {
            post.alternarSalvo();
            atualizarSalvo(h, post);
            Toast.makeText(context, post.isSalvo() ? R.string.adicionado_salvos : R.string.removido_salvos,
                    Toast.LENGTH_SHORT).show();
        });

        h.btnResponder.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalhePostActivity.class);
            intent.putExtra(DetalhePostActivity.EXTRA_ID_POST, post.getId());
            intent.putExtra(DetalhePostActivity.EXTRA_FOCAR_RESPOSTA, true);
            context.startActivity(intent);
        });

        h.btnVisualizacoes.setOnClickListener(v -> Toast.makeText(context,
                context.getString(R.string.visualizacoes_toast, Formatador.contarSempre(post.getVisualizacoes())),
                Toast.LENGTH_SHORT).show());

        h.btnCompartilhar.setOnClickListener(v -> Formatador.compartilhar(context, post));

        h.btnMais.setOnClickListener(v ->
                Toast.makeText(context, R.string.mais_opcoes, Toast.LENGTH_SHORT).show());
    }

    private void atualizarCurtida(ViewHolder h, Post post) {
        int cor = post.isCurtido() ? corRosa : corCinza;
        h.iconeCurtida.setImageResource(post.isCurtido() ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
        h.iconeCurtida.setImageTintList(ColorStateList.valueOf(cor));
        h.qtdCurtidas.setTextColor(cor);
        h.qtdCurtidas.setText(Formatador.contar(post.getCurtidas()));
    }

    private void atualizarRepost(ViewHolder h, Post post) {
        int cor = post.isRepostado() ? corVerde : corCinza;
        h.iconeRepost.setImageTintList(ColorStateList.valueOf(cor));
        h.qtdReposts.setTextColor(cor);
        h.qtdReposts.setText(Formatador.contar(post.getReposts()));
    }

    private void atualizarSalvo(ViewHolder h, Post post) {
        h.btnSalvar.setImageResource(post.isSalvo() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        h.btnSalvar.setImageTintList(ColorStateList.valueOf(post.isSalvo() ? corAzul : corCinza));
    }
}
