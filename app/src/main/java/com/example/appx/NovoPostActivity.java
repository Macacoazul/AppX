package com.example.appx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

/**
 * Tela para escrever um novo post. O texto é devolvido à MainActivity por meio de um Intent.
 */
public class NovoPostActivity extends AppCompatActivity {

    public static final String EXTRA_TEXTO = "texto_post";
    private static final int LIMITE_CARACTERES = 280;

    private EditText campoTexto;
    private MaterialButton btnPostar;
    private CircularProgressIndicator progresso;
    private TextView contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT), SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_novo_post);
        // Inclui o teclado (ime) para a barra de ferramentas subir junto com ele
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        campoTexto = findViewById(R.id.campoTexto);
        btnPostar = findViewById(R.id.btnPostar);
        progresso = findViewById(R.id.progresso);
        contador = findViewById(R.id.contador);

        Formatador.aplicarAvatar(findViewById(R.id.avatarNovoPost), DadosApp.MINHAS_INICIAIS, DadosApp.MINHA_COR);

        campoTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                atualizarEstado();
            }
        });

        btnPostar.setOnClickListener(v -> postar());
        findViewById(R.id.btnCancelar).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        View.OnClickListener emBreve = v -> Toast.makeText(this,
                getString(R.string.em_breve, v.getContentDescription()), Toast.LENGTH_SHORT).show();
        findViewById(R.id.btnImagem).setOnClickListener(emBreve);
        findViewById(R.id.btnEnquete).setOnClickListener(emBreve);
        findViewById(R.id.btnLocal).setOnClickListener(emBreve);
        findViewById(R.id.btnMaisPosts).setOnClickListener(emBreve);
        findViewById(R.id.btnRascunhos).setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.em_breve, getString(R.string.rascunhos)),
                        Toast.LENGTH_SHORT).show());

        atualizarEstado();

        // Abre o teclado automaticamente
        campoTexto.requestFocus();
        campoTexto.post(() -> WindowCompat.getInsetsController(getWindow(), campoTexto)
                .show(WindowInsetsCompat.Type.ime()));
    }

    /** Habilita o botão Postar e atualiza o círculo de contagem de caracteres. */
    private void atualizarEstado() {
        String texto = campoTexto.getText().toString();
        int tamanho = texto.length();
        int restante = LIMITE_CARACTERES - tamanho;
        boolean valido = !texto.trim().isEmpty() && restante >= 0;

        btnPostar.setEnabled(valido);
        btnPostar.setAlpha(valido ? 1f : 0.5f);

        progresso.setProgressCompat(Math.min(tamanho, LIMITE_CARACTERES), true);
        int cor;
        if (restante < 0) {
            cor = ContextCompat.getColor(this, R.color.x_vermelho);
        } else if (restante <= 20) {
            cor = ContextCompat.getColor(this, R.color.x_amarelo);
        } else {
            cor = ContextCompat.getColor(this, R.color.x_azul);
        }
        progresso.setIndicatorColor(cor);

        contador.setVisibility(restante <= 20 ? View.VISIBLE : View.GONE);
        contador.setText(String.valueOf(restante));
        contador.setTextColor(restante < 0
                ? ContextCompat.getColor(this, R.color.x_vermelho)
                : ContextCompat.getColor(this, R.color.x_cinza));
    }

    private void postar() {
        Intent dados = new Intent();
        dados.putExtra(EXTRA_TEXTO, campoTexto.getText().toString().trim());
        setResult(RESULT_OK, dados);
        finish();
    }
}
