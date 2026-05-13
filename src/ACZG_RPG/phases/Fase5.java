package ACZG_RPG.phases;

import ACZG_RPG.battle.MotorBatalha;
import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.model.Monstro;
import ACZG_RPG.ui.InterfaceUsuario;

import java.util.Random;

public class Fase5 implements Fase {

    @Override
    public String getNome() {
        return "O Topo da Torre";
    }

    @Override
    public boolean executar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirNarrativa("Hot Dog sente que no topo da Torre de Contas a Receber reside Glozium.");

        if (estado.possuiItem(Item.GUIA_ATENDIMENTO) &&
                estado.possuiItem(Item.FATURAMENTUS) &&
                estado.possuiItem(Item.BOMBA_MAGICA) &&
                estado.possuiItem(Item.AZAH_TRANSMISSAO) &&
                estado.possuiItem(Item.COLAR_ESTATUA)) {

            boolean forjar = ui.lerSimNao("Voce possui todos os artefatos. Deseja voltar a Hospitalis e forjar a Espada ZG?");
            if (forjar) {
                ui.imprimirNarrativa("Os ferreiros de Zerum Glozium forjam a arma lendaria.");
                estado.adicionarItem(Item.ESPADA_ZG);
                estado.setTemEspadaZG(true);
                ui.imprimirNarrativa("Com a Espada ZG, voce avanca direto para a sala final.");
            }
        }

        if (!estado.isTemEspadaZG()) {
            ui.imprimirNarrativa("Hot Dog escala a torre por fora e e atacado pelo sistema de seguranca.");
            if (!enfrentarGargulas(ui)) {
                ui.imprimirNarrativa("Salsichinha e levado pelas gargulas.");
                estado.matarSalsichinha();
            } else {
                ui.imprimirNarrativa("Captchas quebradas pela aura do heroi.");
            }
        }

        ui.imprimirNarrativa("Hot Dog entra na sala do trono, onde a aura arrepiante de Glozium o aguarda.");

        boolean pular = ui.lerSimNao("Deseja pular o dialogo inicial?");
        if (!pular) {
            ui.imprimirFala("Glozium", "Novamente um rato invadiu meu recinto; desta vez ira servir de alimento para meus escravos.");
            ui.imprimirFala("Hot Dog", "Voce e como meu pai descreveu. Nao cansa de ser terrivelmente molestado por geracoes?");
            ui.imprimirFala("Glozium", "Ha ha ha ha! Que petulante. Chegou a epoca, mas mandaram outro pobre coitado; pessima ideia.");
            ui.imprimirFala("Hot Dog", "Total e permanente, ja me diga onde esta minha mae!");
            ui.imprimirFala("Glozium", "E o que um garoto com cabeca de cachorro quente pode fazer contra um monstro milenar?");
        }

        ui.imprimirNarrativa("Hot Dog lanca sua espada girando no ar que fere o monstro e retorna a sua mao.");
        ui.imprimirFala("Hot Dog", "Nao subestime alguem com cabeca de cachorro quente. Te procurei em cada canto do inferno para reduzir voce a Zero e finalmente resgatar minha mae!");
        ui.imprimirFala("Glozium", "Interessante, aceito seu pedido de batalha, me derrote e voce podera ver sua mae.");

        Monstro glozium = new Monstro("Glozium", 100, 10);
        MotorBatalha batalha = new MotorBatalha(estado, glozium, ui, false);

        boolean venceu = batalha.iniciar();

        if (venceu) {
            finalizarJogo(estado, ui);
            return true;
        } else {
            ui.imprimirFala("Glozium", "Finalmente! um heroi fraco que apenas aumentou meus poderes.");
            ui.imprimirNarrativa("O mundo foi destruido por Glozium, uma fatalidade terrivel. Fim de jogo!");
            return false;
        }
    }

    private boolean enfrentarGargulas(InterfaceUsuario ui) {
        ui.imprimirNarrativa("Batalha contra Gargulas Captchas.");
        String escolha = "";
        while (!escolha.equals("P") && !escolha.equals("I")) {
            escolha = ui.lerEntrada("Escolha [P]ar ou [I]mpar:").toUpperCase();
        }
        int sorteio = new Random().nextInt(10) + 1;
        ui.imprimirNarrativa("O sistema sorteou o numero: " + sorteio);
        boolean par = (sorteio % 2 == 0);
        return (escolha.equals("P") && par) || (escolha.equals("I") && !par);
    }

    private void finalizarJogo(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirNarrativa("Hot Dog golpeia Glozium no peito. O monstro sorri em agonia.");
        ui.imprimirFala("Glozium", "Voce conseguiu... vou cumprir minha promessa, aqui esta sua mae, aproveite seus ultimos momentos.");
        ui.imprimirNarrativa("O corpo de Glozium se desfigura, revelando ser a mae de Hot Dog, possuida pelos poderes sombrios.");
        ui.imprimirFala("Hot Dog", "Nao pode ser... mae? o que foi que eu fiz!");
        ui.imprimirNarrativa("Hot Dog segura a mae nos bracos com a espada ainda em seu peito.");
        ui.imprimirFala("Mae", "Voce apenas cumpriu seu dever filho...");

        if (estado.isTemEspadaZG()) {
            ui.imprimirNarrativa("Quando a esperanca parecia ter se esgotado, a Espada ZG manifestou seu poder milagroso.");
            ui.imprimirNarrativa("Esse poder fundiu-se a mae de Hot Dog, curando-a por completo.");
            ui.imprimirNarrativa("O heroi derrotou Glozium e a paz retornou ao mundo. Ele vivera feliz com sua familia.");
            ui.imprimirNarrativa("Muito obrigado, heroi! Fim de jogo.");
        } else if (!estado.isSalsichinhaVivo()) {
            ui.imprimirNarrativa("A mae de Hot Dog falece, o heroi a repousa gentilmente no chao e se levanta...");
            ui.imprimirNarrativa("O silencio preenche a sala. Glozium foi detido por mais um ano, mas a que custo?");
            ui.imprimirNarrativa("Sob forte pressao mental pelas perdas, e com o sangue da mae nas maos, o heroi, em profunda tristeza e decepcao, caminha silenciosamente ate a janela.");
            ui.imprimirNarrativa("E se joga da torre...");
            ui.imprimirNarrativa("Glozium foi derrotado ao custo mais alto possivel, o mundo seguira normalmente por mais 1 ano. Fim de Jogo.");
        } else {
            ui.imprimirNarrativa("A mae de Hot Dog falece, o heroi a repousa gentilmente no chao.");
            ui.imprimirNarrativa("Mesmo com seu parceiro vivo ao seu lado, a dor do sacrificio e avassaladora.");
            ui.imprimirNarrativa("O heroi se joga da torre. Glozium foi derrotado, mas a jornada encontrou um fim tragico. Fim de Jogo.");
        }
    }
}