package com.jfam.leido;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Pantalla de introducción animada del Universo Aurelia
 * Muestra una animación de 2.5 segundos antes del Splash
 */
public class AureliaIntroActivity extends AppCompatActivity {

    private static final int DURACION_INTRO = 2500; // 2.5 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aurelia_intro);

        iniciarAnimaciones();

        // Navegar al Splash después de la animación
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(AureliaIntroActivity.this, SplashActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, DURACION_INTRO);
    }

    /**
     * Inicia todas las animaciones sincronizadas
     */
    private void iniciarAnimaciones() {
        View circuloExterior = findViewById(R.id.circuloExterior);
        View circuloMedio = findViewById(R.id.circuloMedio);
        View circuloInterior = findViewById(R.id.circuloInterior);
        View txtAurelia = findViewById(R.id.txtAurelia);
        View txtUniverso = findViewById(R.id.txtUniverso);
        View estrella1 = findViewById(R.id.estrella1);
        View estrella2 = findViewById(R.id.estrella2);
        View estrella3 = findViewById(R.id.estrella3);
        View estrella4 = findViewById(R.id.estrella4);

        // Animación 1: Rotación del círculo exterior
        ObjectAnimator rotacionExterior = ObjectAnimator.ofFloat(circuloExterior, "rotation", 0f, 360f);
        rotacionExterior.setDuration(2500);
        rotacionExterior.setInterpolator(new LinearInterpolator());
        rotacionExterior.start();

        // Animación 2: Rotación inversa del círculo medio
        ObjectAnimator rotacionMedio = ObjectAnimator.ofFloat(circuloMedio, "rotation", 360f, 0f);
        rotacionMedio.setDuration(2000);
        rotacionMedio.setInterpolator(new LinearInterpolator());
        rotacionMedio.start();

        // Animación 3: Pulso del círculo interior
        ObjectAnimator pulsoScale = ObjectAnimator.ofFloat(circuloInterior, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator pulsoScaleY = ObjectAnimator.ofFloat(circuloInterior, "scaleY", 1f, 1.2f, 1f);
        AnimatorSet pulso = new AnimatorSet();
        pulso.playTogether(pulsoScale, pulsoScaleY);
        pulso.setDuration(1500);

        pulso.setInterpolator(new AccelerateDecelerateInterpolator());
        pulso.start();

        // Animación 4: Fade-in del texto AURELIA
        ObjectAnimator fadeAurelia = ObjectAnimator.ofFloat(txtAurelia, "alpha", 0f, 1f);
        fadeAurelia.setStartDelay(500);
        fadeAurelia.setDuration(1000);
        fadeAurelia.start();

        // Animación 5: Fade-in del subtítulo
        ObjectAnimator fadeUniverso = ObjectAnimator.ofFloat(txtUniverso, "alpha", 0f, 1f);
        fadeUniverso.setStartDelay(800);
        fadeUniverso.setDuration(800);
        fadeUniverso.start();

        // Animación 6: Estrellas apareciendo con delay
        animarEstrella(estrella1, 300);
        animarEstrella(estrella2, 600);
        animarEstrella(estrella3, 900);
        animarEstrella(estrella4, 1200);
    }

    /**
     * Anima una estrella con fade-in y twinkle
     */
    private void animarEstrella(View estrella, long delay) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(estrella, "alpha", 0f, 1f);
        fadeIn.setStartDelay(delay);
        fadeIn.setDuration(400);

        ObjectAnimator twinkle = ObjectAnimator.ofFloat(estrella, "alpha", 1f, 0.3f, 1f);
        twinkle.setStartDelay(delay + 400);
        twinkle.setDuration(800);
        twinkle.setRepeatCount(2);

        fadeIn.start();
        twinkle.start();
    }
}