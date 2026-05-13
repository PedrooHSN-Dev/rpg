package ACZG_RPG.phases;

import ACZG_RPG.battle.MotorBatalha;
import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.model.Monstro;
import ACZG_RPG.ui.InterfaceUsuario;

public class Fase3 implements Fase {

    @Override
    public String getNome() {
        return "Vila da Transmissao";
    }

    @Override
    public boolean executar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirNarrativa("Uma vila magica que recebe cobrancas vindas das cavernas de Faturamentus. Os moradores entregam as mensagens na Torre.");
        ui.imprimirNarrativa("Hot Dog e bem recebido em sua chegada. Ele e convidado para um jantar ritualistico com comidas tipicas.");
        ui.imprimirNarrativa("Enquanto Salsichinha saboreava um delicioso bife de calango, os moradores contavam a Hot Dog que o monstro a ser enfrentado e um ser que se enterra. Para ataca-lo, ele tera que desenterrar o monstro e usar sua espada.");
        ui.imprimirNarrativa("Os moradores lhe entregam um novo item.");

        estado.adicionarItem(Item.BOMBA_MAGICA);

        ui.imprimirNarrativa("Logo, um monstro em forma de verme surge do chao.");
        ui.imprimirFala("Worm da transmissao", "Voce ja tem sua arma. Fui amigo do guardiao dessas terras e transmitia as mensagens a torre apos seu falecimento. Mas Glozium me tornou seu escravo, sou obrigado a lutar com toda furia. Assim como foi antes, livre-me do sofrimento.");
        ui.imprimirFala("Hot Dog", "Voce tambem esta preso a um destino que nao escolheu, vou te libertar!");

        Monstro monstro = new Monstro("Worm da transmissao", 12, 3);
        MotorBatalha batalha = new MotorBatalha(estado, monstro, ui, true);

        boolean venceu = batalha.iniciar();

        if (venceu) {
            ui.imprimirFala("Worm", "Muito obrigado, todo heroi evitava essa batalha e seguia para a torre, agora sou livre!");
            ui.imprimirFala("Hot Dog", "Evitava? E da para seguir sem lutar? Como assim?");
            estado.adicionarItem(Item.AZAH_TRANSMISSAO);
            return true;
        } else {
            ui.imprimirFala("Worm", "Sinto muito heroi, que sua alma seja livre.");
            ui.imprimirNarrativa("O mundo foi destruido por Glozium, uma fatalidade terrivel. Fim de jogo!");
            return false;
        }
    }
}