package ACZG_RPG.phases;

import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.ui.InterfaceUsuario;

import java.util.Random;

public class MissaoSecundaria1 {
    public void iniciar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirFala("Comerciante", "Olha so, e o filho do Sandubinha, como voce esta garoto? Vejo que finalmente partiu em sua jornada contra Glozium.");
        ui.imprimirFala("Hot Dog", "Ola, eu to de boas, quero arrebentar com esse monstro o quanto antes.");
        ui.imprimirFala("Comerciante", "Hahaha como seu pai e sua mae.");
        ui.imprimirFala("Hot Dog", "La ele, vaaapu.");
        ui.imprimirFala("Comerciante", "Ei, estou com meu filho Beringelo na carroca e preciso deixar ele sozinho por um tempo para negociar com alguns vendedores. Diz ai, nao esta interessado em cuidar dele em troca de um elixir de vida? Deixar ele com um heroi lendario vai me fazer ficar calmo haha.");

        boolean aceitar = ui.lerSimNao("Deseja cuidar do filho do comerciante?");
        if (!aceitar) {
            return;
        }

        ui.imprimirNarrativa("O heroi entra na carroca para cuidar do filho do comerciante, esta cheio de produtos interessantes.");
        ui.imprimirFala("Beringelo", "Ei, que tal a gente jogar pedra, papel e tesoura para passar o tempo?");

        int vitoriasJogador = 0;
        int vitoriasBeringelo = 0;
        Random gerador = new Random();
        String[] opcoes = {"PEDRA", "PAPEL", "TESOURA"};

        for (int i = 1; i <= 4; i++) {
            ui.imprimirNarrativa("Rodada " + i);
            String escolhaJogador = "";
            while (!escolhaJogador.equals("PEDRA") && !escolhaJogador.equals("PAPEL") && !escolhaJogador.equals("TESOURA")) {
                escolhaJogador = ui.lerEntrada("Escolha Pedra, Papel ou Tesoura:").toUpperCase();
            }
            String escolhaBeringelo = opcoes[gerador.nextInt(3)];

            ui.imprimirNarrativa("Beringelo escolheu: " + escolhaBeringelo);

            if (escolhaJogador.equals(escolhaBeringelo)) {
                ui.imprimirNarrativa("Empate.");
            } else if ((escolhaJogador.equals("PEDRA") && escolhaBeringelo.equals("TESOURA")) ||
                    (escolhaJogador.equals("PAPEL") && escolhaBeringelo.equals("PEDRA")) ||
                    (escolhaJogador.equals("TESOURA") && escolhaBeringelo.equals("PAPEL"))) {
                ui.imprimirNarrativa("Voce venceu a rodada.");
                vitoriasJogador++;
            } else {
                ui.imprimirNarrativa("Beringelo venceu a rodada.");
                vitoriasBeringelo++;
            }
        }

        if (vitoriasJogador >= vitoriasBeringelo) {
            ui.imprimirFala("Beringelo", "Uou, voce tem muita sorte! Vai derrotar Glozium certamente!");
        } else {
            ui.imprimirFala("Beringelo", "Que merda em, espero que voce tenha mais sorte que isso na batalha contra Glozium, looser!!");
        }

        ui.imprimirFala("Comerciante", "Obrigado por cuidar do meu filho, aqui esta o pagamento.");
        ui.imprimirNarrativa("O heroi ganha o elixir de vida que da mais 10 pontos de vida.");
        estado.aumentarVidaMaxima(10);
    }
}