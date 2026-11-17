package com.example.appx;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Funções utilitárias de formatação e pequenas ações reaproveitadas pelas telas.
 */
public final class Formatador {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final Pattern HASHTAG_OU_MENCAO = Pattern.compile("[#@][\\p{L}\\p{N}_]+");

    private Formatador() {
    }

    /** Formata números no estilo do X: 950, 2,1 mil, 45 mil, 1,4 mi. Zero vira texto vazio. */
    public static String contar(int n) {
        if (n <= 0) return "";
        if (n < 1000) return String.valueOf(n);
        if (n < 10_000) return decimal(n / 1000.0) + " mil";
        if (n < 1_000_000) return (n / 1000) + " mil";
        if (n < 10_000_000) return decimal(n / 1_000_000.0) + " mi";
        return (n / 1_000_000) + " mi";
    }

    /** Igual a contar(), mas mostra "0" em vez de vazio. */
    public static String contarSempre(int n) {
        return n <= 0 ? "0" : contar(n);
    }

    private static String decimal(double valor) {
        double arredondado = Math.floor(valor * 10) / 10.0;
        if (arredondado >= 100 || arredondado == Math.floor(arredondado)) {
            return String.valueOf((long) arredondado);
        }
        return String.format(Locale.US, "%.1f", arredondado).replace('.', ',');
    }

    /** "agora", "25min", "3h" ou a data, conforme o tempo decorrido. */
    public static String tempoRelativo(long momento) {
        long minutos = (System.currentTimeMillis() - momento) / 60_000L;
        if (minutos < 1) return "agora";
        if (minutos < 60) return minutos + "min";
        long horas = minutos / 60;
        if (horas < 24) return horas + "h";
        return new SimpleDateFormat("d 'de' MMM", PT_BR).format(new Date(momento));
    }

    /** Data completa exibida na tela de detalhes do post. */
    public static String dataCompleta(long momento) {
        return new SimpleDateFormat("HH:mm · d 'de' MMM 'de' yyyy", PT_BR).format(new Date(momento));
    }

    /** Pinta #hashtags e @menções com a cor informada. */
    public static CharSequence destacar(String texto, int cor) {
        SpannableString resultado = new SpannableString(texto);
        Matcher matcher = HASHTAG_OU_MENCAO.matcher(texto);
        while (matcher.find()) {
            resultado.setSpan(new ForegroundColorSpan(cor), matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return resultado;
    }

    /** Monta a "foto de perfil": círculo colorido com as iniciais. */
    public static void aplicarAvatar(TextView avatar, String iniciais, int cor) {
        avatar.setText(iniciais);
        avatar.setBackgroundTintList(ColorStateList.valueOf(cor));
    }

    /** Pequena animação de "pulo" usada ao curtir/repostar. */
    public static void pular(View view) {
        view.animate().scaleX(1.35f).scaleY(1.35f).setDuration(110)
                .withEndAction(() -> view.animate().scaleX(1f).scaleY(1f).setDuration(110).start())
                .start();
    }

    /** Abre a folha de compartilhamento do Android (Intent implícita). */
    public static void compartilhar(Context context, Post post) {
        Intent envio = new Intent(Intent.ACTION_SEND);
        envio.setType("text/plain");
        envio.putExtra(Intent.EXTRA_TEXT, post.getNome() + " (" + post.getUsuario() + "): " + post.getTexto());
        context.startActivity(Intent.createChooser(envio, context.getString(R.string.compartilhar_post)));
    }
}
