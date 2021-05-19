package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import javax.lang.model.element.NestingKind;

public class Jogo extends ApplicationAdapter {
    //Criação das variáveis necessárias para a aplicação
    private float larguraDispositivo;
    private float alturaDispositivo;
    private float posicaoInicialVerticalPassaro = 0; //Posição inicial do pássaro
    private float variacao = 0; //Serve para que ocorra a animação dos pássaros
    private float posicaoCanoHorizontal;
    private float posicaoCanoVertical;
    private float espacoEntreCanos;

    private int pontos = 0;
    private int gravidade = 0; //Gravidade forçando para que o pássaro caia

    private Texture[] passaros; //Array
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private SpriteBatch batch;

    BitmapFont textoPontuacao;
    private boolean passouCano = false; //Informa se o passarinho passou ou não por dentro do cano
    private Random random; //Altura da abertura dos canos de forma aleatória

    private ShapeRenderer shapeRenderer; //Desenha, renderiza o formato da figura
    private Circle circuloPassaro; //Circle collider do pássaro
    private Rectangle retanguloCanoCima; //Cria collider no cano de cima
    private Rectangle retanguloCanoBaixo; //Cria collider no cano de baixo

	@Override
	public void create () { // Puxa as informações e constrói as variáveis necessárias
	    inicializaTexturas();
	    inicializaObjetos();

	}

    private void inicializaObjetos() {

	    random = new Random();
        batch = new SpriteBatch();

        larguraDispositivo = Gdx.graphics.getWidth(); //Variável largura
        alturaDispositivo = Gdx.graphics.getHeight();  //Variável altura
        posicaoInicialVerticalPassaro = alturaDispositivo / 2; //Pega o tamanho da tela e posiciona o pássaro exatamente na metade dela, independente do tamanho da tela do celular
        posicaoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 350;

        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(10);

        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoCima = new Rectangle();
        retanguloCanoBaixo = new Rectangle();

    }

    private void inicializaTexturas() {
        passaros = new Texture[3]; //Array com tamanho das sprites pra animação
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        fundo = new Texture("fundo.png"); //Asset do fundo
        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");

    }

    @Override
	public void render () { //Renderização dos assets na tela
	    //Métodos
	    verificaEstadoJogo();
	    desenharTexturas();
	    detectarColisao();
	    validarPontos();
	}

    private void detectarColisao() {
	    circuloPassaro.set(50 + passaros[0].getWidth() / 2,
                posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() /2);
       //Parte de baixo do cano desenhada
	    retanguloCanoBaixo.set(posicaoCanoHorizontal,
                alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(),
                canoBaixo.getHeight());
         //Parte de cima do cano desenhada
	    retanguloCanoCima.set(posicaoCanoHorizontal,
                alturaDispositivo / 2 - canoTopo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoTopo.getWidth(),
                canoTopo.getHeight());

	    boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
        boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

        if(bateuCanoBaixo || bateuCanoCima){
            Gdx.app.log("Log", "Bateu");
        }

	}


    private void validarPontos() {
	    if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){
	        if(!passouCano){
	            pontos++;
	            passouCano = true;
            }
        }
    }

    private void verificaEstadoJogo() {
	    posicaoCanoHorizontal -=Gdx.graphics.getDeltaTime() * 200;
	    if(posicaoCanoHorizontal <- canoBaixo.getHeight()){
	        posicaoCanoHorizontal = larguraDispositivo;
	        posicaoCanoVertical = random.nextInt(400 ) - 200; //randomização dos canos na tela
            passouCano = false;
        }
        boolean toqueTela = Gdx.input.justTouched(); //Booleano para se o usuário toca ou não na tela
        if (Gdx.input.justTouched()){ //Caso o usuário toque na tela
            gravidade = -25; //Gravidade é atribuída e o pássaro é forçado para baixo
        }
        if(posicaoInicialVerticalPassaro > 0 || toqueTela)
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
        variacao += Gdx.graphics.getDeltaTime() * 10; //Utiliza DeltaTime para realizar a variação do tempo
        if (variacao > 3) //Se variação maior que três, ela volta a zerar
            variacao = 0; //Primeira sprite do pássaro

        gravidade++;

    }

    private void desenharTexturas() { //Impressão das texturas na tela
        batch.begin(); //Início

        //Configuração da movimentação do pássaro
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro); //Posicionamento do pássaro
        batch.draw(canoBaixo,posicaoCanoHorizontal,
                alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
        batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
        textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);
        batch.end(); //Fim da parte de renderização
    }

    @Override
	public void dispose () {

	}
}
