package ACZG_RPG.battle;

import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.model.Monstro;
import ACZG_RPG.ui.InterfaceUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MotorBatalha {
    private final EstadoJogo estado;
    private final Monstro monstro;
    private final InterfaceUsuario ui;
    private final boolean batalhaWorm;
    private final Random gerador;

    private int numeroSecretoJogador;
    private int posicaoWorm;
    private boolean wormExposto;
    private boolean monstroAtordoado;
    private Item ultimoItemUtilizadoJogador;
    private int turnosRecuperacaoVanish = 0;

    public MotorBatalha(EstadoJogo estado, Monstro monstro, InterfaceUsuario ui, boolean batalhaWorm) {
        this.estado = estado;
        this.monstro = monstro;
        this.ui = ui;
        this.batalhaWorm = batalhaWorm;
        this.gerador = new Random();
        this.monstroAtordoado = false;

        if (batalhaWorm) {
            this.posicaoWorm = gerador.nextInt(monstro.getPontosVidaMaxima()) + 1;
            this.wormExposto = false;
        }
    }

    public boolean iniciar() {
        this.numeroSecretoJogador = gerador.nextInt(estado.getPontosVidaMaxima()) + 1;
        ui.imprimirNarrativa("Numero secreto sorteado para Hot Dog: " + numeroSecretoJogador);
        ui.pausar();

        while (estado.isVivo() && monstro.isVivo()) {
            imprimirCabecalhoBatalha();
            turnoJogador();

            if (!monstro.isVivo()) {
                ui.imprimirNarrativa("Voce derrotou " + monstro.getNome() + "!");
                estado.vencerBatalha();
                estado.adicionarEnergiaSimulador();
                return true;
            }

            if (monstroAtordoado) {
                ui.imprimirNarrativa("O monstro esta atordoado e pulou o turno!");
                monstroAtordoado = false;
            } else {
                executarTurnoMonstro();
            }

            if (!estado.isVivo()) {
                ui.imprimirNarrativa("Hot Dog foi derrotado.");
                return false;
            }
        }
        return false;
    }

    private void imprimirCabecalhoBatalha() {
        ui.imprimir("\n==================================================");
        ui.imprimir(estado.obterBarraStatus());
        ui.imprimir("Inimigo: " + monstro.getNome() + " - HP: " + monstro.getPontosVida() + "/" + monstro.getPontosVidaMaxima());
        if (batalhaWorm) {
            ui.imprimir("Status Worm: " + (wormExposto ? "Acima do solo" : "Enterrado"));
        }
        ui.imprimir("==================================================");
    }

    private void turnoJogador() {
        ui.imprimir("\nSeu turno:");
        exibirMenuItens();

        boolean acaoValida = false;
        while (!acaoValida) {
            String escolha = ui.lerEntrada("Selecione o numero do item ou [S]:").toUpperCase();

            if (escolha.equals("S")) {
                estado.alternarSalsichinhaAtaque();
                ui.imprimirNarrativa("Postura do Salsichinha alterada.");
                imprimirCabecalhoBatalha();
                exibirMenuItens();
            } else {
                try {
                    int indice = Integer.parseInt(escolha) - 1;
                    if (indice >= 0 && indice < estado.getInventario().size()) {
                        Item itemSelecionado = estado.getInventario().get(indice);
                        estado.setItemEquipado(itemSelecionado);
                        acaoValida = true;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        ui.imprimirNarrativa("Item equipado: " + estado.getItemEquipado().getNome());
        String acao = ui.lerEntrada("Deseja [A]tacar ou [D]esistir da missao?").toUpperCase();

        if (acao.equals("A")) {
            ultimoItemUtilizadoJogador = estado.getItemEquipado();
            int dano = processarItemJogador(estado.getItemEquipado());

            if (estado.isSalsichinhaAtivoParaAtaque() && estado.getItemEquipado() != Item.VANISH) {
                ui.imprimirNarrativa("Salsichinha ajudou no ataque! (+1 de dano)");
                dano += 1;
            }

            if (dano > 0) {
                monstro.receberDano(dano);
                ui.imprimirNarrativa(monstro.getNome() + " recebeu " + dano + " pontos de dano.");
            }
        } else if (acao.equals("D")) {
            estado.receberDano(estado.getPontosVidaMaxima());
        }
    }

    private void exibirMenuItens() {
        ui.imprimir("Inventario Disponivel:");
        List<Item> itens = estado.getInventario();
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            String extra = "";
            if (item == Item.TREVO_AUTORIZACAO) extra = " (" + estado.getUsosRestantesTrevo() + " usos)";
            ui.imprimir((i + 1) + ". " + item.getNome() + " (" + item.getDescricao() + ")" + extra);
        }
        ui.imprimir("S. Alternar postura de ataque do Salsichinha");
    }

    private int processarItemJogador(Item item) {
        switch (item) {
            case ESPADA_SIMPLES: return calcularDano(1, monstro.getNumeroSecreto());
            case GUIA_ATENDIMENTO: return calcularDano(2, monstro.getNumeroSecreto());
            case FATURAMENTUS: return processarFaturamentus();
            case AZAH_TRANSMISSAO: return processarAzahTransmissao();
            case COLAR_ESTATUA: return processarColarEstatua();
            case ESPADA_ZG: return calcularDano(40, monstro.getNumeroSecreto());
            case BOMBA_MAGICA:
                if (batalhaWorm) processarBombaWorm();
                else processarBombaNormal();
                return 0;
            case VANISH:
                if (!estado.isUsouVanishBatalhaAtual() && estado.isSalsichinhaVivo()) {
                    ui.imprimirNarrativa("Salsichinha late poderosamente e usa VANISH! A energia de ataque do inimigo caiu para zero.");
                    turnosRecuperacaoVanish = monstro.getQuantidadeSorteiosTurno();
                    estado.setUsouVanishBatalhaAtual(true);
                    estado.setSalsichinhaAtivoParaAtaque(false);
                    ui.imprimirNarrativa("Salsichinha ficou exausto apos a habilidade. Ele nao pode mais realizar ataques combinados nesta batalha.");
                } else {
                    ui.imprimirNarrativa("O Vanish ja foi usado nesta batalha ou Salsichinha nao esta apto a lutar.");
                }
                return 0;
            default:
                return calcularDano(1, monstro.getNumeroSecreto());
        }
    }

    private int calcularDano(int quantidadeSorteios, int numeroAlvo) {
        if (batalhaWorm && !wormExposto) {
            ui.imprimirNarrativa("O Worm esta enterrado. E necessario usar a Bomba Magica antes de atacar com esta arma.");
            return 0;
        }

        List<Integer> sorteios = realizarSorteios(quantidadeSorteios, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados: " + sorteios);

        if (estado.getUsosRestantesTrevo() > 0 && !sorteios.contains(numeroAlvo)) {
            boolean usarTrevo = ui.lerSimNao("Seu ataque vai falhar. Deseja usar o Trevo da Autorizacao para garantir o acerto? (Restam " + estado.getUsosRestantesTrevo() + " usos)");
            if (usarTrevo) {
                estado.decrementarUsoTrevo();
                sorteios.add(numeroAlvo);
                ui.imprimirNarrativa("A magia do trevo guiou a sua arma diretamente para o alvo.");
            }
        }

        long acertos = sorteios.stream().filter(n -> n == numeroAlvo).count();
        int dano = (int) (acertos * numeroAlvo);

        if (acertos > 0) {
            ui.imprimirNarrativa("Numero secreto atingido " + acertos + " vez(es). Dano calculado: " + dano);
        } else {
            ui.imprimirNarrativa("O ataque nao atingiu o numero secreto.");
        }
        return dano;
    }

    private int processarFaturamentus() {
        if (batalhaWorm && !wormExposto) {
            ui.imprimirNarrativa("O Worm esta enterrado. E necessario usar a Bomba Magica antes de atacar.");
            return 0;
        }

        List<Integer> sorteios = realizarSorteios(4, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados: " + sorteios);

        long acertos = sorteios.stream().filter(n -> n == monstro.getNumeroSecreto()).count();

        if (acertos == 0) {
            ui.imprimirNarrativa("O ataque nao atingiu o numero secreto.");
            ui.imprimirNarrativa("Penalidade Faturamentus ativada. O proximo ataque do monstro causara +2 de dano.");
            estado.setPenalidadeFaturamentus(2);
            return 0;
        }

        int dano = (int) (acertos * monstro.getNumeroSecreto());
        ui.imprimirNarrativa("Numero secreto atingido " + acertos + " vez(es). Dano calculado: " + dano);
        return dano;
    }

    private int processarAzahTransmissao() {
        if (batalhaWorm && !wormExposto) {
            ui.imprimirNarrativa("O Worm esta enterrado. E necessario usar a Bomba Magica antes de atacar.");
            return 0;
        }

        List<Integer> sorteios = realizarSorteios(11, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados: " + sorteios);
        estado.setUltimoSorteioAzah(sorteios.get(sorteios.size() - 1));

        long acertos = sorteios.stream().filter(n -> n == monstro.getNumeroSecreto()).count();

        if (acertos == 0) {
            ui.imprimirNarrativa("O ataque falhou. A penalidade da Capa Azah foi ativada para o proximo turno do monstro!");
            estado.setPenalidadeAzah(true);
            return 0;
        }

        int dano = (int) (acertos * monstro.getNumeroSecreto());
        ui.imprimirNarrativa("Numero secreto atingido " + acertos + " vez(es). Dano calculado: " + dano);
        return dano;
    }

    private int processarColarEstatua() {
        if (batalhaWorm && !wormExposto) {
            ui.imprimirNarrativa("O Worm esta enterrado.");
            return 0;
        }

        ui.imprimirNarrativa("O Colar da Estatua drena sua forca vital (-3 HP) para liberar poder.");
        estado.receberDano(3);
        if (!estado.isVivo()) return 0;

        return calcularDano(11, monstro.getNumeroSecreto());
    }

    private void processarBombaNormal() {
        if (estado.getErrosBomba() >= 3) {
            ui.imprimirNarrativa("A bomba instavel explodiu em suas maos antes do lancamento! (-2 HP)");
            estado.receberDano(2);
            if (!estado.isVivo()) return;
        }

        List<Integer> sorteios = realizarSorteios(9, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Sorteios da Bomba Magica: " + sorteios);

        int limiteAcerto = (int) (monstro.getPontosVidaMaxima() * 0.8);
        boolean acertou = sorteios.stream().anyMatch(n -> n <= limiteAcerto);

        if (acertou) {
            ui.imprimirNarrativa("A Bomba Magica atordoou o monstro!");
            monstroAtordoado = true;
            estado.resetarErrosBomba();
        } else {
            estado.adicionarErroBomba();
            ui.imprimirNarrativa("A bomba errou o alvo. (Erro " + estado.getErrosBomba() + ")");
        }
    }

    private void processarBombaWorm() {
        if (estado.getErrosBomba() >= 3) {
            ui.imprimirNarrativa("A bomba instavel explodiu em suas maos antes do lancamento! (-2 HP)");
            estado.receberDano(2);
            if (!estado.isVivo()) return;
        }

        List<Integer> sorteios = realizarSorteios(9, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Sorteios da Bomba Magica buscando o Worm (Posicao atual: " + posicaoWorm + "): " + sorteios);

        if (sorteios.contains(posicaoWorm)) {
            ui.imprimirNarrativa("A explosao desenterrou o Worm. Ele esta vulneravel a ataques diretos.");
            wormExposto = true;
            estado.resetarErrosBomba();
        } else {
            ui.imprimirNarrativa("A bomba explodiu no local errado.");
            estado.adicionarErroBomba();
        }
    }

    private void executarTurnoMonstro() {
        ui.imprimir("\nTurno do Inimigo:");

        if (monstro.getNome().equals("Sandubinha reverse") && ultimoItemUtilizadoJogador != null && ultimoItemUtilizadoJogador != Item.SALSICHINHA && ultimoItemUtilizadoJogador != Item.VANISH) {
            executarAtaqueCopia();
            return;
        }

        processarAtaqueMonstro(monstro.getQuantidadeSorteiosTurno());

        if (batalhaWorm && wormExposto) {
            if (ultimoItemUtilizadoJogador != Item.BOMBA_MAGICA) {
                wormExposto = false;
                posicaoWorm = gerador.nextInt(monstro.getPontosVidaMaxima()) + 1;
                ui.imprimirNarrativa("O Worm enterrou-se novamente em uma nova posicao.");
            }
        }
    }

    private void executarAtaqueCopia() {
        ui.imprimirNarrativa("Sandubinha reverse copia o seu ultimo movimento!");
        int sorteiosCopia = 1;
        switch (ultimoItemUtilizadoJogador) {
            case GUIA_ATENDIMENTO: sorteiosCopia = 2; break;
            case FATURAMENTUS: sorteiosCopia = 4; break;
            case AZAH_TRANSMISSAO:
            case COLAR_ESTATUA: sorteiosCopia = 11; break;
            case ESPADA_ZG: sorteiosCopia = 40; break;
            case BOMBA_MAGICA: sorteiosCopia = 9; break;
        }
        processarAtaqueMonstro(sorteiosCopia);
    }

    private void processarAtaqueMonstro(int quantidadeSorteios) {
        int sorteiosAtuais = quantidadeSorteios;
        if (turnosRecuperacaoVanish > 0) {
            sorteiosAtuais = quantidadeSorteios - turnosRecuperacaoVanish;
            if (sorteiosAtuais < 0) sorteiosAtuais = 0;
            ui.imprimirNarrativa("O monstro esta sob efeito do Vanish e so conseguiu realizar " + sorteiosAtuais + " sorteios.");
            turnosRecuperacaoVanish--;
        }

        if (sorteiosAtuais == 0) {
            ui.imprimirNarrativa("O inimigo nao tem forcas para atacar neste turno.");
            return;
        }

        List<Integer> sorteios = realizarSorteios(sorteiosAtuais, estado.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados pelo inimigo: " + sorteios);

        long acertos = sorteios.stream().filter(n -> n == numeroSecretoJogador).count();

        if (acertos > 0 && estado.possuiItem(Item.SIMULADOR_GLOZIUM) && estado.getEnergiaSimuladorGlozium() > 0 && estado.isSalsichinhaVivo()) {
            boolean usarSimulador = ui.lerSimNao("O Simulador de Glozium apitou! Voce vai sofrer dano. Deseja usar 1 energia para esquivar do ataque?");
            if (usarSimulador) {
                estado.consumirEnergiaSimulador();
                numeroSecretoJogador = gerador.nextInt(estado.getPontosVidaMaxima()) + 1;
                ui.imprimirNarrativa("Simulador ativado! Voce saltou da trajetoria e seu numero secreto foi alterado para " + numeroSecretoJogador + ".");
                acertos = 0;
            }
        }

        if (acertos > 0) {
            int danoRecebido;
            if (estado.isPenalidadeAzah()) {
                danoRecebido = Math.max(0, estado.getPontosVida() - estado.getUltimoSorteioAzah());
                estado.setPenalidadeAzah(false);
            } else {
                danoRecebido = (int) (acertos * numeroSecretoJogador);
            }

            danoRecebido += estado.getPenalidadeFaturamentus();
            estado.setPenalidadeFaturamentus(0);

            estado.receberDano(danoRecebido);
            ui.imprimirNarrativa("O ataque foi bem sucedido. Voce recebeu " + danoRecebido + " pontos de dano.");
        } else {
            ui.imprimirNarrativa("O inimigo errou o ataque.");
            estado.setPenalidadeAzah(false);
        }
    }

    private List<Integer> realizarSorteios(int quantidade, int limite) {
        List<Integer> sorteios = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            sorteios.add(gerador.nextInt(limite) + 1);
        }
        return sorteios;
    }
}