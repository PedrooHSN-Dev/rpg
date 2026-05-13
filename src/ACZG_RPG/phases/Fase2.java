package ACZG_RPG.phases;

import ACZG_RPG.battle.MotorBatalha;
import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.model.Monstro;
import ACZG_RPG.ui.InterfaceUsuario;

public class Fase2 implements Fase {

    @Override
    public String getNome() {
        return "Cavernas de Faturamentus";
    }

    @Override
    public boolean executar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirNarrativa("Existe sempre um preco a se pagar pela cura do corpo e da alma, apos a floresta do atendimentus seu preco e calculado de acordo com o tipo de contrato divino que voce tem.");
        ui.imprimirNarrativa("Avancando pela caverna, iluminada apenas pelo brilho de sua tocha, Hot Dog distinguiu ruidos metalicos distantes. Ao chegar a uma clareira banhada pela luz emanada por minerios incomuns, ele se deparou com tres passagens e um enigma.");

        ui.imprimirFala("Voz Ecoante", "Do enfermo vem o inicio, do registro o meio, do pagamento o fim do anseio. Gira sem parar nos saloes do curar, que ciclo e esse a sustentar?");
        ui.imprimir("A) O Ciclum Receitatus Hospitalis");
        ui.imprimir("B) O Rito dos Curandeiros Eternos");
        ui.imprimir("C) A Roda da Vida e da Cura");

        String escolhaEnigma = ui.lerEntrada("Sua resposta (A, B ou C):").toUpperCase();
        int vidaMonstroCaverna = 6;

        if (escolhaEnigma.equals("S") || escolhaEnigma.equals("SALSICHINHA")) {
            if (estado.isSalsichinhaVivo()) {
                ui.imprimirNarrativa("Salsichinha ignora as tres portas, fareja uma parede falsa e revela uma passagem secreta com um portal brilhante.");
                boolean pularFase = ui.lerSimNao("Deseja atravessar o portal e pular diretamente para a Torre de Transmissao?");
                if (pularFase) {
                    estado.setPulouParaFinal(true);
                    ui.imprimirNarrativa("Voce foi teletransportado em seguranca.");
                    return true;
                }
            } else {
                ui.imprimirNarrativa("Voce tenta assobiar para seu parceiro, mas lembra que ele nao esta mais com voce. Sua hesitacao custou caro.");
                escolhaEnigma = "ERRO";
            }
        }

        if (escolhaEnigma.equals("B")) {
            ui.imprimirNarrativa("Voce segue em frente, porem, cai em uma armadilha e sofre 3 de dano.");
            estado.receberDano(3);
            if (!estado.isVivo()) {
                ui.imprimirNarrativa("Os ferimentos foram fatais. Voce morreu nas cavernas.");
                return false;
            }
        } else if (escolhaEnigma.equals("C")) {
            ui.imprimirNarrativa("A resposta ecoa incorretamente, fortalecendo a presenca sombria da caverna.");
            vidaMonstroCaverna += 7;
        } else if (escolhaEnigma.equals("A")) {
            ui.imprimirNarrativa("O caminho se abre em seguranca.");
        } else if (!escolhaEnigma.equals("S") && !escolhaEnigma.equals("SALSICHINHA")) {
            ui.imprimirNarrativa("Sua hesitacao ativou a armadilha. Voce sofre 3 de dano.");
            estado.receberDano(3);
            if (!estado.isVivo()) {
                return false;
            }
        }

        ui.imprimirNarrativa("Hot Dog alcanca um novo local, onde os Ancioes do Faturamento trabalham sem dar atencao aos arredores. De repente, ele encontra o monstro da caverna, um cavaleiro infernal forjado por Glozium, afiando sua espada.");
        ui.imprimirFala("Glozium Administratus", "Voce e o heroi deste ano? nao me parece grande coisa. Nao sou muito de conversa, venha lutar, irei te fatiar e servir pros lacaios de meu mestre!");
        ui.imprimirFala("Hot Dog", "Algumas piadas ruins podem ate me fazer rir, voce e uma dessas... se prepare!");

        Monstro monstro = new Monstro("Glozium Administratus", vidaMonstroCaverna, 2);
        MotorBatalha batalha = new MotorBatalha(estado, monstro, ui, false);

        boolean venceu = batalha.iniciar();

        if (venceu) {
            ui.imprimirFala("Anciao faturador", "Muito obrigado, mas to ocupado demais para agradecimentos longos, tome o artefato sagrado e siga em frente.");
            estado.adicionarItem(Item.FATURAMENTUS);
            return true;
        } else {
            ui.imprimirFala("Anciao faturador", "Heroi fraco, nem para cumprir o trabalho dele, estamos perdidos.");
            ui.imprimirNarrativa("O mundo foi destruido por Glozium, uma fatalidade terrivel. Fim de jogo!");
            return false;
        }
    }
}