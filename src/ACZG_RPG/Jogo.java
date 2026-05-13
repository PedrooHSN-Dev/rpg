package ACZG_RPG;

import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.phases.*;
import ACZG_RPG.ui.InterfaceUsuario;

import java.util.ArrayList;
import java.util.List;

public class Jogo {
    private final EstadoJogo estado;
    private final InterfaceUsuario ui;
    private final List<Fase> fases;
    private final Loja loja;
    private final MissaoSecundaria1 missaoSecundaria1;
    private final MissaoSecundaria2 missaoSecundaria2;

    public Jogo() {
        this.estado = new EstadoJogo();
        this.ui = new InterfaceUsuario();
        this.loja = new Loja();
        this.missaoSecundaria1 = new MissaoSecundaria1();
        this.missaoSecundaria2 = new MissaoSecundaria2();

        this.fases = new ArrayList<>();
        this.fases.add(new Fase1());
        this.fases.add(new Fase2());
        this.fases.add(new Fase3());
        this.fases.add(new Fase4());
        this.fases.add(new Fase5());
    }

    public void iniciar() {
        ui.imprimir("==================================================");
        ui.imprimir("       A HISTORIA EPICA DE SIR HOT DOG");
        ui.imprimir("==================================================");

        int indiceFaseAtual = 0;

        while (indiceFaseAtual < fases.size() && estado.isVivo()) {
            if (estado.isPulouParaFinal() || estado.possuiItem(Item.SEND_PEARL)) {
                indiceFaseAtual = fases.size() - 1;
                estado.setPulouParaFinal(false);
                estado.removerItem(Item.SEND_PEARL);
            }

            Fase faseAtual = fases.get(indiceFaseAtual);

            ui.imprimir("\n==================================================");
            ui.imprimir(estado.obterBarraStatus());
            ui.imprimir("==================================================");
            ui.imprimir("1. Avancar para: " + faseAtual.getNome());
            ui.imprimir("2. Visitar a Loja de Itens Magicos");
            ui.imprimir("3. Ver Inventario");

            int maxOpcao = 3;
            if (indiceFaseAtual == 3) {
                ui.imprimir("4. Missao Secundaria 1 (Disponivel)");
                maxOpcao = 4;
            } else if (indiceFaseAtual == 4) {
                ui.imprimir("4. Missao Secundaria 2 (Disponivel)");
                maxOpcao = 4;
            }

            ui.imprimir("0. Desistir da missao");

            int escolha = ui.lerInteiro("O que deseja fazer?", 0, maxOpcao);

            switch (escolha) {
                case 1:
                    boolean sucesso = faseAtual.executar(estado, ui);
                    if (sucesso) {
                        indiceFaseAtual++;
                    } else {
                        estado.receberDano(estado.getPontosVidaMaxima());
                    }
                    break;
                case 2:
                    loja.abrir(estado, ui);
                    break;
                case 3:
                    exibirInventario();
                    break;
                case 4:
                    if (indiceFaseAtual == 3) {
                        missaoSecundaria1.iniciar(estado, ui);
                    } else if (indiceFaseAtual == 4) {
                        missaoSecundaria2.iniciar(estado, ui);
                    }
                    break;
                case 0:
                    if (ui.lerSimNao("Tem certeza que quer abandonar o mundo nas maos de Glozium?")) {
                        ui.imprimirNarrativa("Voce desistiu da missao. Glozium venceu.");
                        estado.receberDano(estado.getPontosVidaMaxima());
                    }
                    break;
            }
        }
    }

    private void exibirInventario() {
        ui.imprimir("\n--- O Seu Inventario ---");
        for (Item item : estado.getInventario()) {
            ui.imprimir("- " + item.getNome() + ": " + item.getDescricao());
        }
        ui.pausar();
    }
}