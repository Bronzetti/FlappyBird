package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	SpriteBatch batch;
	Texture passaro; //Pega o asset do pássaro
	Texture fundo; //Pega o asset do fundo


	//Criação das variáveis para largura e altura do aparelho
	private float larguraDispositivo;
	private float alturaDispositivo;

	private int	movimentaY = 0; //Variável para movimentação no eixo Y com início nulo
	private int movimentaX = 0;//Variável para movimentação no eixo X com início nulo

	
	@Override
	public void create () { // Puxa as informações e constrói as variáveis necessárias
		batch = new SpriteBatch();
		fundo = new Texture ("fundo.png");  //Asset do fundo
		passaro = new Texture("passaro.png"); //Asset do pássaro

		larguraDispositivo = Gdx.graphics.getWidth(); //Variável largura
		alturaDispositivo = Gdx.graphics.getHeight(); //Variável altura
	}

	@Override
	public void render () { //Renderização dos assets na tela
		//ScreenUtils.clear(1, 0, 0, 1);

		batch.begin(); //Início

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo); //Desenha o fundo de acordo com o tamanho da tela do aparelho
		batch.draw(passaro, movimentaX, movimentaY); //Desenha o pássaro

		movimentaX++; //Movimenta para frente
		//movimentaY++;

		batch.end(); //Fim
	}
	
	@Override
	public void dispose () {

	}
}
