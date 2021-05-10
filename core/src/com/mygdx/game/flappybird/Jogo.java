package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	/*SpriteBatch batch;
	Texture passaro; //Pega o asset do pássaro
	Texture fundo; //Pega o asset do fundo
	//Criação das variáveis para largura e altura do aparelho
	private float larguraDispositivo;
	private float alturaDispositivo;
	private int	movimentaY = 0; //Variável para movimentação no eixo Y com início nulo
	private int movimentaX = 0;//Variável para movimentação no eixo X com início nulo*/

    private int movimentaY = 0;
    private int movimentaX = 0;
    private SpriteBatch batch;
    private Texture[] passaros; //Array
    private Texture fundo;

    private float larguraDispositivo;
    private float alturaDispositivo;
    private float variacao = 0; //Serve para que ocorra a animação dos pássaros
    private float gravidade = 0; //Gravidade forçando para que o pássaro caia
    private float posicaoInicialVerticalPassaro = 0; //Posição inicial do pássaro


	
	@Override
	public void create () { // Puxa as informações e constrói as variáveis necessárias
		/*batch = new SpriteBatch();
		fundo = new Texture ("fundo.png");  //Asset do fundo
		passaro = new Texture("passaro.png"); //Asset do pássaro
		larguraDispositivo = Gdx.graphics.getWidth(); //Variável largura
		alturaDispositivo = Gdx.graphics.getHeight(); //Variável altura*/

        batch = new SpriteBatch();
        passaros = new Texture[3]; //Array com tamanho das sprites pra animação
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");
        fundo = new Texture("fundo.png");

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        posicaoInicialVerticalPassaro = alturaDispositivo / 2; //Pega o tamanho da tela e posiciona o pássaro exatamente na metade dela, independente do tamanho da tela do celular

	}

	@Override
	public void render () { //Renderização dos assets na tela
		//ScreenUtils.clear(1, 0, 0, 1);

		batch.begin(); //Início

		/*batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo); //Desenha o fundo de acordo com o tamanho da tela do aparelho
		batch.draw(passaro, movimentaX, movimentaY); //Desenha o pássaro
		movimentaX++; //Movimenta para frente
		//movimentaY++;*/
         if (variacao > 3)
             variacao = 0; //Primeira sprite do pássaro

        boolean toqueTela = Gdx.input.justTouched(); //Booleano para se o usuário toca ou não na tela
        if (Gdx.input.justTouched()){ //Caso o usuário toque na tela
            gravidade = -25; //Gravidade é atribuída e o pássaro é forçado para baixo
        }

        if(posicaoInicialVerticalPassaro > 0 || toqueTela)
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
        //Configuração da movimentação do pássaro
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaros[(int) variacao], 30, posicaoInicialVerticalPassaro); //Posicionamento do pássaro

        variacao += Gdx.graphics.getDeltaTime() * 10; //Utiliza DeltaTime para realizar a variação do tempo
        //Contadores
        gravidade++;
        movimentaY++;
        movimentaX++;
		batch.end(); //Fim da parte de renderização
	}
	
	@Override
	public void dispose () {

	}
}
