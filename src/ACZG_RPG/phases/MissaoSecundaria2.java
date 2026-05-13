package ACZG_RPG.phases;

import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.ui.InterfaceUsuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MissaoSecundaria2 {
    public void iniciar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirFala("Camponesa", "Algo esta roubando minhas galinhas, penso que seja um monstro de Glozium.");
        ui.imprimirFala("Camponesa", "Por favor, me ajude, aniquile esse monstro, se fizer isso, te dou um elixir de vida que tenho guardado comigo... e algo mais se tiver interessado...");

        boolean aceitar = ui.lerSimNao("Deseja ajudar a camponesa?");
        if (!aceitar) {
            return;
        }

        ui.imprimirFala("Hot Dog", "Ha ha se acalme, uma moca tao bonita nao pode sair por ai oferecendo esse tipo de coisa.");
        ui.imprimirFala("Hot Dog", "Voce parece mesmo desesperada, eu sou um heroi, vou te ajudar com esse monstro em troca apenas do elixir, me mostre o galinheiro.");
        ui.imprimirFala("Salsichinha", "(E muita aura slc)");
        ui.imprimirNarrativa("Ao seguir o rastro do monstro, ele descobre que na realidade e um senhor bem velho, que alega estar roubando devido a fome.");
        ui.imprimirNarrativa("Ele fica com pena do senhor e baixa a guarda, o senhor lanca um feitico sobre Hot Dog e diz que para ele sair, tera que o vencer em um jogo.");
        ui.imprimirFala("Velhote", "E um jogo da memoria usando cartas numeradas, vou te mostrar elas por um tempo e voce deve me indicar quais os pares.");

        List<String> cartas = new ArrayList<>(List.of("A", "A", "B", "B", "C", "C", "D", "D", "E", "E"));
        Collections.shuffle(cartas);

        ui.imprimirNarrativa("Memorize os pares abaixo:");
        for (int i = 0; i < cartas.size(); i++) {
            System.out.print("[" + i + ": " + cartas.get(i) + "] ");
        }
        System.out.println();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (int i = 0; i < 50; i++) {
            System.out.println();
        }

        ui.imprimirNarrativa("O tempo esgotou-se. As cartas foram escondidas.");

        boolean venceu = true;
        List<Integer> cartasReveladas = new ArrayList<>();

        for (int par = 0; par < 5; par++) {
            int primeiraEscolha = ui.lerInteiro("Escolha o indice da primeira carta (0 a 9):", 0, 9);
            int segundaEscolha = ui.lerInteiro("Escolha o indice da segunda carta (0 a 9):", 0, 9);

            if (primeiraEscolha == segundaEscolha || cartasReveladas.contains(primeiraEscolha) || cartasReveladas.contains(segundaEscolha) || !cartas.get(primeiraEscolha).equals(cartas.get(segundaEscolha))) {
                venceu = false;
                break;
            } else {
                cartasReveladas.add(primeiraEscolha);
                cartasReveladas.add(segundaEscolha);
                ui.imprimirNarrativa("Par correto.");
            }
        }

        if (venceu) {
            ui.imprimirNarrativa("Hot Dog se libertou, derrotou o velho do mal e o levou as autoridades.");
            ui.imprimirFala("Camponesa", "Muito obrigado heroi! Aqui esta seu elixir... e... e um beijo.");
            ui.imprimirNarrativa("O heroi ganha o elixir de vida que da mais 10 pontos de vida.");
            ui.imprimirNarrativa("E ganha +1 ponto de vida pelo beijo.");
            estado.aumentarVidaMaxima(10);
            estado.curar(1);
        } else {
            ui.imprimirNarrativa("O velho foge amaldicoando o Heroi.");
            ui.imprimirFala("Camponesa", "E uma pena, mas pelo menos agora sei que e um velho e nao um monstro de Glozium, vou procurar outro guerreiro que preste, voce e beta demais! Ate outra hora.");
            ui.imprimirNarrativa("O heroi perde 4 pontos de vida por causa da maldicao do velho (-4).");
            ui.imprimirNarrativa("E menos 1 ponto, por que foi moggado (-1).");
            ui.imprimirFala("Salsichinha", "(E o que sobra pro betinha, brutal, it's over)");
            estado.receberDano(5);
        }
    }
}