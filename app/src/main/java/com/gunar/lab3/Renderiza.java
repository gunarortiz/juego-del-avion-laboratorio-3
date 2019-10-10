package com.gunar.lab3;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.MotionEvent;

public class Renderiza extends GLSurfaceView implements Renderer {
    private int alto;
    private int ancho;
    private float x;
    int sw = 1;
    int sw1 = 1;
    int sw2 = 1;
    int p = 0;
    float disx = 0.4f, disy = 0.3f;
    private Avion dino;
    private Avion muerte;
    private Avion ene1;
    private Avion camino;
    //private Avion disparo;
    float dx = 3, dy = 6.3f;
    float dxx;
    float dx1 = -3, dy1 = 9.3f;
    float dxx1;
    float dx2 = -3, dy2 = 20;
    float dxx2 = 0;
    float inc = 0.05f;


    private long inicio, fin, duracion;
    private float tiempo_real;
    private float tiempoMovimiento;
    private final float PERIODO_MOVIMIENTO = 0.1f;

    private float despTexturaX;
    Context contexto;

    public Renderiza(Context contexto) {
        super(contexto);
        this.contexto = contexto;
        this.setRenderer(this);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {

        dino = new Avion(gl, contexto, "avion.png");
        dino.setVertices(-0.7f, -0.7f, 1f, 1f);
        dino.setCoord_Textura(0, 0, 1f, 1);

        muerte = new Avion(gl, contexto, "explotar.png");
        muerte.setVertices(-0.7f, -0.7f, 1f, 1f);
        muerte.setCoord_Textura(0, 0, 1, 1);

        ene1 = new Avion(gl, contexto, "ene.png");
        ene1.setVertices(-0.4f, -0.3f, 0.4f, 0.3f);
        ene1.setCoord_Textura(0, 0, 1, 1);

        camino = new Avion(gl, contexto, "camino.png");
        camino.setVertices(-4, -6, 4, 6);
        camino.setCoord_Textura(0, 0, 1, 1);

        camino.muestra(gl);
        despTexturaX = 0;
        inicio = System.currentTimeMillis();
        tiempoMovimiento = PERIODO_MOVIMIENTO;
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glClearColor(0, 172, 224, 0);
    }

    public void muestraDino(GL10 gl) {

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, 0);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        dino.muestra(gl);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, ene1.getCodigoTextura());

        gl.glLoadIdentity();
        gl.glTranslatef(dx, dy, 0);
        ene1.muestra(gl);

        if (sw == 1) {
            dx = dx - 0.1f;
        } else {
            dx = dx + 0.1f;
        }
        if (dx > dxx + 1.5f) {
            sw = 1;
        }
        if (dx < dxx - 1.5f) {
            sw = 0;
        }

        if (dy < -6.3f) {
            dy = 6.3f;
            dx = x;
            dxx = x;
            inc = inc + 0.005f;
        }
        dy = dy - inc;
        if (seSobreponen(dino, ene1, dx, dy)) {
            p = 1;
            dy = 6.3f;
            dx = x - 2;
            dxx = x - 2;
        }
        gl.glBindTexture(GL10.GL_TEXTURE_2D, ene1.getCodigoTextura());
//--segunda fila--//
        gl.glLoadIdentity();
        gl.glTranslatef(dx1, dy1, 0);
        ene1.muestra(gl);

        if (sw1 == 1) {
            dx1 = dx1 - 0.1f;
        } else {
            dx1 = dx1 + 0.1f;
        }
        if (dx1 > dxx1 + 1.5f) {
            sw1 = 1;
        }
        if (dx1 < dxx1 - 1.5f) {
            sw1 = 0;
        }

        if (dy1 < -6.3f) {
            dy1 = 9.3f;
            dx1 = x;
            dxx1 = x;
            inc = inc + 0.005f;
        }
        dy1 = dy1 - inc;
        if (seSobreponen(dino, ene1, dx1, dy1)) {
            p = 1;
            dy1 = 9.3f;
            dx1 = x;
            dxx1 = x;
        }

        if (p == 0) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, dino.getCodigoTextura());
            gl.glLoadIdentity();
            gl.glTranslatef(x, -5, 0);

            muestraDino(gl);
        } else {
            if (p == 30) {
                p = 0;
            } else {
                p++;
            }
            gl.glBindTexture(GL10.GL_TEXTURE_2D, muerte.getCodigoTextura());
            gl.glLoadIdentity();
            gl.glTranslatef(x, -5, 0);
            muerte.muestra(gl);
        }
        if (inc >= 0.25) {
            inc = 0.05f;//cambio
        }
        fin = System.currentTimeMillis();
        duracion = fin - inicio;
        tiempo_real = duracion / 1000f;
        inicio = fin;

        /* Incremento y limite*/
        tiempoMovimiento = tiempoMovimiento - tiempo_real;
        if (tiempoMovimiento < 0.001) {
            tiempoMovimiento = PERIODO_MOVIMIENTO;
            /* Incrementa los desplazamientos en la textura*/
            despTexturaX = despTexturaX + 1 / 2f;
            // en u (o s)
            if (despTexturaX > 7f / 8) {
                despTexturaX = 0;
            }
        }
        requestRender();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        /* Obtiene el ancho y el alto de la pantalla */
        ancho = w;
        alto = h;
        /* Ventana de despliegue */
        gl.glViewport(0, 0, w, h);
        /* Matriz de Proyecci�n */
        gl.glMatrixMode(GL10.GL_PROJECTION);
        /* Inicializa la Matriz de Proyecci�n */
        gl.glLoadIdentity();
        /* Proyecci�n paralela */
        GLU.gluOrtho2D(gl, -4, 4, -6, 6);
        /* Matriz del Modelo-Vista */
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        /* Inicializa la Matriz del Modelo-Vista */
        gl.glLoadIdentity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        x = (e.getX() * (8 / (float) ancho)) - 4;
        return true;
    }

    public boolean seSobreponen(Avion r1, Avion r2, float rdx, float rdy) {
        return (x < rdx + 0.4f * 2 && x + 0.7f * 2 > rdx &&
                -5 < rdy + 0.3f * 2 && -5 + 0.7f * 2 > rdy);

    }
}
