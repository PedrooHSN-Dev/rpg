package ACZG_RPG.phases;

import ACZG_RPG.battle.MotorBatalha;
import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.model.Monstro;
import ACZG_RPG.ui.InterfaceUsuario;

public class Fase4 implements Fase {

    @Override
    public String getNome() {
        return "Torre de Contas a Receber";
    }

    @Override
    public boolean executar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirNarrativa("A terrivel torre e onde seres magicos recebem o pagamento por seu arduo trabalho. Dizem que a ruina perto da torre era a cidade mais proxima do grito, terra natal dos Analyticaes di Glosium.");

        boolean decidiuVoar = false;
        if (estado.possuiItem(Item.AZAH_TRANSMISSAO)) {
            decidiuVoar = ui.lerSimNao("Voce possui a capa Azah Transmissao. Deseja usa-la para voar ate a torre?");
        }

        if (!decidiuVoar) {
            ui.imprimirNarrativa("A caminhada ardua ate a torre consumiu suas forcas. Voce perdeu 4 pontos de vida.");
            estado.receberDano(4);
            if (!estado.isVivo()) {
                ui.imprimirNarrativa("Voce nao resistiu a viagem e desvaneceu antes de chegar a torre.");
                return false;
            }
        } else {
            ui.imprimirNarrativa("A capa lhe concedeu a habilidade de voo, permitindo chegar a torre sem desgaste fisico.");
        }

        ui.imprimirNarrativa("Hot Dog chega ao primeiro andar da torre. Seu mascote sente uma energia magica mortifera vindo do topo. Sem duvidas, Glozium estava la.");
        ui.imprimirNarrativa("A parede se abre e sai dela o penultimo monstro a ser enfrentado.");
        ui.imprimirFala("Hot Dog", "Uma estatua na forma de meu pai quando jovem, por que?");
        ui.imprimirFala("Sandubinha reverse", "Entao voce chegou ate aqui, sou a estatua do ultimo heroi, voce e forte mas sera que consegue derrotar um heroi lendario?");

        Monstro monstro = new Monstro("Sandubinha reverse", 30, 8);
        MotorBatalha batalha = new MotorBatalha(estado, monstro, ui, false);

        boolean venceu = batalha.iniciar();

        if (venceu) {
            ui.imprimirFala("Sandubinha reverse", "...");
            ui.imprimirFala("Hot Dog", "E assustador olhar para essa estatua despedacada e ver a forma do meu pai.");
            estado.adicionarItem(Item.COLAR_ESTATUA);
            return true;
        } else {
            ui.imprimirFala("Sandubinha reverse", "A vitoria e minha!");
            ui.imprimirNarrativa("O mundo foi destruido por Glozium, uma fatalidade terrivel. Fim de jogo!");
            return false;
        }
    }
}