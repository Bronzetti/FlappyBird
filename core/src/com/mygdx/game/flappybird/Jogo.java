package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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

    // Variáveis de textura
    private SpriteBatch batch;
    private Texture[] passaros; //Array
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private Texture gameOver;

    //Variáveis de desenho/colisão
    private ShapeRenderer shapeRenderer; //Desenha, renderiza o formato da figura
    private Circle circuloPassaro; //Circle collider do pássaro
    private Rectangle retanguloCanoCima; //Cria collider no cano de cima
    private Rectangle retanguloCanoBaixo; //Cria collider no cano de baixo

    //Variáveis da largura da tela e posicionamento dos itens
    private float larguraDispositivo;
    private float alturaDispositivo;
    private float posicaoInicialVerticalPassaro = 0; //Posição inicial do pássaro
    private float variacao = 0; //Serve para que ocorra a animação dos pássaros
    private float posicaoCanoHorizontal;
    private float posicaoCanoVertical;
    private float posicaoHorizontalPassaro = 0;
    private float espacoEntreCanos;
    private int pontos = 0; //Contagem de pontos ao cruzar os canos. Inicialmente com valor nulo.
    private int pontuacaoMaxima = 0;
    private int estadoJogo = 0;
    private float gravidade = 2; //Gravidade forçando para que o pássaro caia
    private boolean passouCano = false; //Variável que informa se o passarinho passou ou não por dentro do cano
    private Random random; //Altura da abertura dos canos de forma aleatória

    BitmapFont textoPontuacao; //Imprime na tela a pontuação
    BitmapFont textoReiniciar; //Imprime na tela o texto de reiniciar, quando o jogo entrar no estado == 2
    BitmapFont textoMelhorPontuacao; //Imprime na tela o máximo de pontos feitos

    Sound somVoando; //Som das asas do pássaro batendo
    Sound somColisao; //Som atribuído quando o pássaro colidir com um cano
    Sound somPontuacao; //Som atribuído quando o pássaro passar com um cano, sem que haja colisão

    Preferences preferencias;

	@Override
	public void create () { // Puxa as informações e constrói as variáveis necessárias no Método Create.
	    inicializaTexturas(); //Método no qual inicializa as texturas que serão necessárias
	    inicializaObjetos(); //Método no qual inicializa os objeto que serão utilizados
	}

    @Override
    public void render () { //Método de Renderização dos assets na tela. Atualiza de tempo e tempo e é este Método que verifica o estado atual do jogo
        //Métodos
        verificaEstadoJogo(); //Método que verifica a validação da aplicação
        desenharTexturas(); //Método responsável por desenhar as texturas aplicadas
        detectarColisao(); //Método responsável pelas colisões
        validarPontos(); //Método que faz a contagem dos pontos e fica responsável pelo placar
    }

    private void inicializaObjetos() {

        batch = new SpriteBatch();
	    random = new Random();

        larguraDispositivo = Gdx.graphics.getWidth(); //Variável largura
        alturaDispositivo = Gdx.graphics.getHeight();  //Variável altura
        posicaoInicialVerticalPassaro = alturaDispositivo / 2; //Pega o tamanho da tela e posiciona o pássaro exatamente na metade dela, independente do tamanho da tela do celular
        posicaoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 350;

        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(10);

        textoReiniciar = new BitmapFont();
        textoReiniciar.setColor(Color.GREEN);
        textoReiniciar.getData().setScale(2);

        textoMelhorPontuacao = new BitmapFont();
        textoMelhorPontuacao.setColor(Color.RED);
        textoMelhorPontuacao.getData().setScale(2);

        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoCima = new Rectangle();
        retanguloCanoBaixo = new Rectangle();

        somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
        somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
        somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));

        preferencias = Gdx.app.getPreferences("FlappyBird");
        pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);



    }

    private void inicializaTexturas() {
        passaros = new Texture[3]; //Array com tamanho das sprites pra animação
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        fundo = new Texture("fundo.png"); //Asset do fundo
        canoBaixo = new Texture("cano_baixo_maior.png"); //Asset cano baixo
        canoTopo = new Texture("cano_topo_maior.png"); //Asset cano cima
        gameOver = new Texture("game_over.png"); //Asset Game Over

    }

    private void detectarColisao() {

	    circuloPassaro.set(50 + passaros[0].getWidth() / 2,
                posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() /2);

       //Parte de baixo do cano desenhada
	    retanguloCanoBaixo.set(posicaoCanoHorizontal,
                alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight());

         //Parte de cima do cano desenhada
        retanguloCanoCima.set(posicaoCanoHorizontal,
                alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight());

	    boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
        boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

        if(bateuCanoBaixo || bateuCanoCima){
            if(estadoJogo == 1){
                somColisao.play(); //Som de batida
                estadoJogo = 2; //Imprime o game over na tela
            }
        }
	}

    private void validarPontos() {
	    if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){
	        if(!passouCano){
	            pontos++;
	            passouCano = true;
	            somPontuacao.play(); //Ao passar pelo cano, é atribuído um som
            }
        }
        variacao += Gdx.graphics.getDeltaTime() * 10; //Utiliza DeltaTime para realizar a variação do tempo
        if (variacao > 3) //Se variação maior que três, ela volta a zerar
            variacao = 0; //Primeira sprite do pássaro
    }

    private void verificaEstadoJogo() {
        //Toque na tela
        boolean toqueTela = Gdx.input.justTouched(); //Booleano para se o usuário toca ou não na tela
	    if(estadoJogo == 0){ //Iniciou jogo, estado zero, canos não aparecem na tela

            if (toqueTela){ //Caso o usuário toque na tela
                gravidade = -15; //Gravidade é atribuída e o pássaro é forçado para baixo
                estadoJogo = 1; //E o estado do jogo será 1
                somVoando.play();
            }

        }else if (estadoJogo == 1){ //Ao entrar no estado 1:

            if (toqueTela){ //Ao tocar na tela
                gravidade = -15; //O pássaro dará pequenos pulos
                somVoando.play();
            }

            //Movimentação dos canos será iniciada
            posicaoCanoHorizontal -=Gdx.graphics.getDeltaTime() * 200;
            if(posicaoCanoHorizontal <- canoBaixo.getHeight()){
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVertical = random.nextInt(400 ) - 200; //randomização dos canos na tela
                passouCano = false;
            }

            if(posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade; //Pássaro ficará "flutuando" na gravidade

            gravidade++; //Gravidade sendo aplicado dentro do estado do jogo

        } else if(estadoJogo == 2){ //Estado 2 = caso haja colisão o passarinho e jogo então, ficarão estáticos.

	        if(pontos > pontuacaoMaxima){
               pontuacaoMaxima = pontos;
               preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
            }

	        posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500; //Pássaro bate no cano e é jogado para trás

	        if(toqueTela){ //Resetando o projeto para que haja o reinício do jogo
	            estadoJogo = 0;
	            pontos = 0;
	            gravidade = 0;
	            posicaoHorizontalPassaro = 0;
	            posicaoInicialVerticalPassaro = alturaDispositivo / 2;
	            posicaoCanoHorizontal = larguraDispositivo;

            }

        }
    }

    private void desenharTexturas() { //Impressão das texturas na tela
        batch.begin(); //Início da parte de renderização

        //Desenho das texturas feitos entre o batch.begin e batch.end
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaros[(int) variacao], 50 + posicaoHorizontalPassaro, posicaoInicialVerticalPassaro); //Posicionamento do pássaro
        batch.draw(canoBaixo,posicaoCanoHorizontal,
                alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
        batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
        textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

        if(estadoJogo == 2){ //Se o jogo entrar no estado 2, ou seja, se houver colisão do passarinho em algum cano:

            //OBS: Os valores aqui atribuídos de subtração (-200, -280), foram escolhidos justamente
            // para que ficassem no centro da tela do meu dispositivo no qual estava utilizando como emulador, um J7 Neo.

            batch.draw(gameOver, larguraDispositivo /2 - gameOver.getWidth() / 2 , alturaDispositivo /2);
            textoReiniciar.draw(batch, "TOQUE NA TELA PARA INICIAR!", //Desenha centralizadamente  o texo de tocar na tela
                    larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);
            textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUAÇÃO FOI:" + pontuacaoMaxima + "PONTOS!", //Desenha certralizadamente o texto do record pessoal de pontuação
                    larguraDispositivo / 2 - 280, alturaDispositivo / 2 - gameOver.getHeight() * 2);
        }
        batch.end(); //Fim da parte de renderização
    }

    @Override
	public void dispose () {
	}
}
