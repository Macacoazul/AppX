package com.example.appx;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Tela Explorar: campo de busca, abas de categorias e lista de assuntos do momento.
 */
public class ExplorarActivity extends AppCompatActivity {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private EditText campoBusca;
    private LinearLayout containerAbas;
    private AssuntoAdapter adapter;
    private String categoriaAtual = DadosApp.CATEGORIA_PARA_VOCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT), SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_explorar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        Formatador.aplicarAvatar(findViewById(R.id.avatarExplorar), DadosApp.MINHAS_INICIAIS, DadosApp.MINHA_COR);

        campoBusca = findViewById(R.id.campoBusca);
        containerAbas = findViewById(R.id.containerAbas);

        ListView lista = findViewById(R.id.listaAssuntos);
        adapter = new AssuntoAdapter(this, new ArrayList<>());
        lista.setAdapter(adapter);
        lista.setEmptyView(findViewById(R.id.semResultados));
        lista.setOnItemClickListener((parent, view, position, id) -> {
            Assunto assunto = adapter.getItem(position);
            if (assunto != null) {
                Toast.makeText(this, getString(R.string.buscando, assunto.getTitulo()), Toast.LENGTH_SHORT).show();
            }
        });

        campoBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar();
            }
        });

        findViewById(R.id.btnConfigExplorar).setOnClickListener(v ->
                Toast.makeText(this, R.string.configuracoes, Toast.LENGTH_SHORT).show());

        BarraNavegacao.configurar(this, BarraNavegacao.EXPLORAR, this::focarBusca);

        criarAbas();
        selecionarCategoria(DadosApp.CATEGORIA_PARA_VOCE);
    }

    /** Cria dinamicamente uma TextView para cada categoria. */
    private void criarAbas() {
        int espaco = (int) (16 * getResources().getDisplayMetrics().density);
        for (String categoria : DadosApp.CATEGORIAS) {
            TextView aba = new TextView(this);
            aba.setText(categoria);
            aba.setTextSize(15);
            aba.setGravity(Gravity.CENTER);
            aba.setPadding(espaco, 0, espaco, 0);
            aba.setTag(categoria);
            aba.setOnClickListener(v -> selecionarCategoria(categoria));
            containerAbas.addView(aba, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private void selecionarCategoria(String categoria) {
        categoriaAtual = categoria;
        for (int i = 0; i < containerAbas.getChildCount(); i++) {
            TextView aba = (TextView) containerAbas.getChildAt(i);
            boolean selecionada = categoria.equals(aba.getTag());
            aba.setTextColor(ContextCompat.getColor(this, selecionada ? R.color.x_texto : R.color.x_cinza));
            aba.setTypeface(Typeface.DEFAULT, selecionada ? Typeface.BOLD : Typeface.NORMAL);
            aba.setBackgroundResource(selecionada ? R.drawable.bg_aba_selecionada : 0);
        }
        filtrar();
    }

    /** Filtra os assuntos pela categoria selecionada e pelo texto digitado na busca. */
    private void filtrar() {
        String busca = campoBusca.getText().toString().trim().toLowerCase(PT_BR);
        List<Assunto> resultado = new ArrayList<>();
        for (Assunto assunto : DadosApp.getAssuntos()) {
            boolean todasCategorias = DadosApp.CATEGORIA_PARA_VOCE.equals(categoriaAtual)
                    || DadosApp.CATEGORIA_EM_ALTA.equals(categoriaAtual);
            boolean categoriaOk = todasCategorias || assunto.getCategoria().equals(categoriaAtual);
            boolean buscaOk = busca.isEmpty()
                    || assunto.getTitulo().toLowerCase(PT_BR).contains(busca)
                    || assunto.getCategoria().toLowerCase(PT_BR).contains(busca);
            if (categoriaOk && buscaOk) {
                resultado.add(assunto);
            }
        }
        if (DadosApp.CATEGORIA_EM_ALTA.equals(categoriaAtual)) {
            resultado.sort((a, b) -> Integer.compare(b.getQuantidadePosts(), a.getQuantidadePosts()));
        }
        adapter.clear();
        adapter.addAll(resultado);
    }

    private void focarBusca() {
        campoBusca.requestFocus();
        WindowCompat.getInsetsController(getWindow(), campoBusca).show(WindowInsetsCompat.Type.ime());
    }
}
