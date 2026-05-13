package ACZG_RPG.phases;

import ACZG_RPG.battle.MotorBatalha;
import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.model.Monstro;
import ACZG_RPG.ui.InterfaceUsuario;

public class Fase1 implements Fase {

    @Override
    public String getNome() {
        return "Floresta do Atendimentus";
    }

    @Override
    public boolean executar(EstadoJogo estado, InterfaceUsuario ui) {
        ui.imprimirNarrativa("Um lugar encantado, dizem que pessoas doentes ou com almas feridas podem ir a floresta para serem curadas.");
        ui.imprimirFala("Sandubinha", "Na floresta de Atendimentus, encontre o ser Processus ele ira te mostrar o monstro a ser enfrentado.");
        ui.imprimirNarrativa("Hot Dog se perde na floresta, mas de repente seu cachorro fareja algo e comeca a correr, o heroi o segue pois confia totalmente em seu querido animal.");
        ui.imprimirNarrativa("Atravessando a densa vegetacao de espinhos que lhes feriam a pele, eles avancam rapidamente ate alcancarem uma clareira, onde se deparam com uma visao aterrorizante.");
        ui.imprimirNarrativa("Processus estava morto! Caido aos pes de uma criatura humanoide com um olhar nojento e arrogante, como um demonio.");

        ui.imprimirFala("Hot Dog", "Nao ha duvidas, voce e o inimigo, como voce feriu o Processus? Nao era para seu poder afetar seres magicos do tipo dele!");
        ui.imprimirFala("Anti-authorizatus", "Nao afeta diretamente, mas interfere no equilibrio, eu impeco o atendimento das almas, eu sou Anti-authorizatus! E mesmo esses seres precisam passar pelo Ciclo de Hospitales. HAHAHA que pena, esse ja era, agora e sua vez e depois matarei esse pirralho assustado nos arbustos, filho desse aqui.");
        ui.imprimirFala("Hot Dog", "Nao se preocupe jovem, irei me vingar pelo amigo de meu pai!");

        Monstro monstro = new Monstro("Anti-authorizatus", 4, 1);
        MotorBatalha batalha = new MotorBatalha(estado, monstro, ui, false);

        boolean venceu = batalha.iniciar();

        if (venceu) {
            ui.imprimirFala("Filho de Processus", "Muito obrigado, tome o artefato Guia de atendimento e va para o proximo desafio, vou sepultar meu pai.");
            estado.adicionarItem(Item.GUIA_ATENDIMENTO);
            return true;
        } else {
            ui.imprimirNarrativa("O filho de Processus chora estridentemente.");
            ui.imprimirNarrativa("O monstro matou o filho de Processus e o mundo foi destruido por Glozium, uma fatalidade terrivel. Fim de jogo.");
            return false;
        }
    }
}