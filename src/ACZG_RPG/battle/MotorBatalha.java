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
    private final Random gerador;

    private final int numeroSecretoJogador;
    private Item ultimoItemUtilizadoJogador;

    private final boolean batalhaWorm;
    private boolean wormExposto;
    private int posicaoWorm;

    private boolean monstroAtordoado;
    private boolean modoBerserker;
    private boolean eventoGloziumOcorreu;

    public MotorBatalha(EstadoJogo estado, Monstro monstro, InterfaceUsuario ui, boolean batalhaWorm) {
        this.estado = estado;
        this.monstro = monstro;
        this.ui = ui;
        this.batalhaWorm = batalhaWorm;
        this.gerador = new Random();

        this.numeroSecretoJogador = gerador.nextInt(estado.getPontosVidaMaxima()) + 1;
        this.wormExposto = false;
        this.monstroAtordoado = false;
        this.modoBerserker = false;
        this.eventoGloziumOcorreu = false;

        if (this.batalhaWorm) {
            this.posicaoWorm = gerador.nextInt(monstro.getPontosVidaMaxima()) + 1;
        }
    }

    public boolean iniciar() {
        estado.resetarErrosBomba();
        estado.setPenalidadeFaturamentus(0);
        estado.setPenalidadeAzah(false);

        ui.imprimirNarrativa("Numero secreto sorteado para Hot Dog: " + numeroSecretoJogador);
        ui.pausar();

        while (monstro.isVivo() && estado.isVivo()) {
            if (verificarEventoGlozium()) {
                if (!estado.isVivo()) {
                    return false;
                }
            }

            exibirStatus();

            if (!executarTurnoJogador()) {
                return false;
            }

            if (monstro.isVivo()) {
                if (monstroAtordoado) {
                    ui.imprimirNarrativa(monstro.getNome() + " esta atordoado e perde o turno.");
                    monstroAtordoado = false;
                } else {
                    executarTurnoMonstro();
                }
            }
        }

        if (estado.isVivo()) {
            ui.imprimirNarrativa("Voce derrotou " + monstro.getNome() + "!");
            estado.vencerBatalha();
            return true;
        } else {
            ui.imprimirNarrativa("Hot Dog foi derrotado.");
            return false;
        }
    }

    private boolean verificarEventoGlozium() {
        if (monstro.getNome().equals("Glozium") && !estado.isSalsichinhaVivo() && !eventoGloziumOcorreu) {
            if (monstro.getPontosVida() <= monstro.getPontosVidaMaxima() * 0.8) {
                eventoGloziumOcorreu = true;
                ui.imprimirNarrativa("Glozium interrompe o combate e invoca as gargulas captchas.");
                ui.imprimirFala("Glozium", "Desista de tudo, entregue a espada e seu companheiro vivera. Recuse e ele morrera.");

                boolean desistir = ui.lerSimNao("Voce deseja desistir para salvar o Salsichinha?");
                if (desistir) {
                    ui.imprimirNarrativa("Glozium liberta heroi e mascote, que sobrevivem desonrados.");
                    ui.imprimirNarrativa("Glozium destroi o mundo. Fim de jogo.");
                    estado.receberDano(estado.getPontosVidaMaxima() * 99);
                } else {
                    ui.imprimirNarrativa("Glozium elimina Salsichinha em frente aos olhos de Hot Dog.");
                    modoBerserker = true;
                    estado.aumentarVidaMaxima(50);
                    ui.imprimirNarrativa("Furia incontrolavel! Modo Berserker ativado: Dano triplicado e 50 pontos de vida ganhos.");
                }
                return true;
            }
        }
        return false;
    }

    private void exibirStatus() {
        ui.imprimir("\n==================================================");
        ui.imprimir(estado.obterBarraStatus());
        ui.imprimir("Inimigo: " + monstro.getNome() + " - HP: " + monstro.getPontosVida() + "/" + monstro.getPontosVidaMaxima());
        if (batalhaWorm) {
            ui.imprimir("Status Worm: " + (wormExposto ? "Acima do solo" : "Enterrado"));
        }
        ui.imprimir("==================================================");
    }

    private boolean executarTurnoJogador() {
        ui.imprimir("\nSeu turno:");
        escolherItemEquipamento();

        if (!estado.isVivo()) {
            return true;
        }

        if (batalhaWorm && !wormExposto && estado.getItemEquipado() != Item.BOMBA_MAGICA) {
            ui.imprimirNarrativa("O Worm esta enterrado. E necessario usar a Bomba Magica antes de atacar.");
            return true;
        }

        String acao = ui.lerEntrada("Deseja [A]tacar ou [D]esistir da missao?").toUpperCase();
        if (acao.equals("D")) {
            if (ui.lerSimNao("Tem certeza que deseja abandonar a missao?")) {
                return false;
            }
        }

        Item item = estado.getItemEquipado();
        ultimoItemUtilizadoJogador = item;
        int danoCausado = processarItemJogador(item);

        if (estado.isSalsichinhaAtivoParaAtaque() && danoCausado > 0) {
            danoCausado += 1;
            ui.imprimirNarrativa("Salsichinha ataca e causa 1 de dano adicional.");
        }

        if (modoBerserker && danoCausado > 0) {
            danoCausado *= 3;
            ui.imprimirNarrativa("O dano foi triplicado pela furia do Berserker.");
        }

        if (danoCausado > 0) {
            monstro.receberDano(danoCausado);
            ui.imprimirNarrativa(monstro.getNome() + " recebeu " + danoCausado + " pontos de dano.");
        }

        return true;
    }

    private void escolherItemEquipamento() {
        List<Item> inventario = estado.getInventario();
        ui.imprimir("Inventario Disponivel:");
        for (int i = 0; i < inventario.size(); i++) {
            ui.imprimir((i + 1) + ". " + inventario.get(i).getNome() + " (" + inventario.get(i).getDescricao() + ")");
        }
        if (estado.isSalsichinhaVivo()) {
            ui.imprimir("S. Alternar postura de ataque do Salsichinha");
        }

        String escolha = ui.lerEntrada("Selecione o numero do item ou [S]:").toUpperCase();
        if (escolha.equals("S") && estado.isSalsichinhaVivo()) {
            estado.alternarSalsichinhaAtaque();
            ui.imprimirNarrativa("Postura do Salsichinha atualizada.");
        } else {
            try {
                int indice = Integer.parseInt(escolha) - 1;
                if (indice >= 0 && indice < inventario.size()) {
                    estado.setItemEquipado(inventario.get(indice));
                    ui.imprimirNarrativa("Item equipado: " + inventario.get(indice).getNome());
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private int processarItemJogador(Item item) {
        switch (item) {
            case ESPADA_SIMPLES:
                return calcularDano(1, monstro.getNumeroSecreto());
            case GUIA_ATENDIMENTO:
                return calcularDano(2, monstro.getNumeroSecreto());
            case FATURAMENTUS:
                return processarFaturamentus();
            case AZAH_TRANSMISSAO:
                return processarAzahTransmissao();
            case COLAR_ESTATUA:
                return processarColarEstatua();
            case ESPADA_ZG:
                return calcularDano(40, monstro.getNumeroSecreto());
            case BOMBA_MAGICA:
                if (batalhaWorm) {
                    processarBombaWorm();
                } else {
                    processarBombaNormal();
                }
                return 0;
            default:
                return calcularDano(1, monstro.getNumeroSecreto());
        }
    }

    private int calcularDano(int quantidadeSorteios, int numeroAlvo) {
        List<Integer> sorteios = realizarSorteios(quantidadeSorteios, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados: " + sorteios);

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
        int dano = calcularDano(4, monstro.getNumeroSecreto());
        if (dano == 0) {
            estado.setPenalidadeFaturamentus(estado.getPenalidadeFaturamentus() + 2);
            ui.imprimirNarrativa("Penalidade Faturamentus ativada. O proximo ataque do monstro causara +2 de dano.");
        }
        return dano;
    }

    private int processarAzahTransmissao() {
        List<Integer> sorteios = realizarSorteios(11, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados com Azah: " + sorteios);

        long acertos = sorteios.stream().filter(n -> n == monstro.getNumeroSecreto()).count();
        int ultimoSorteado = sorteios.get(sorteios.size() - 1);
        estado.setUltimoSorteioAzah(ultimoSorteado);

        if (acertos > 0) {
            estado.setPenalidadeAzah(false);
            return (int) (acertos * monstro.getNumeroSecreto());
        } else {
            estado.setPenalidadeAzah(true);
            ui.imprimirNarrativa("Falha no ataque Azah. Se o monstro acertar, o dano sera calculado com base no ultimo numero sorteado (" + ultimoSorteado + ").");
            return 0;
        }
    }

    private int processarColarEstatua() {
        estado.receberDano(3);
        ui.imprimirNarrativa("O uso do Colar consumiu 3 pontos de vida da sua forca vital.");
        return calcularDano(11, monstro.getNumeroSecreto());
    }

    private void processarBombaNormal() {
        int tentativas = Math.max(1, (int) (monstro.getPontosVidaMaxima() * 0.8));
        List<Integer> sorteios = realizarSorteios(tentativas, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Sorteios da Bomba Magica: " + sorteios);

        if (sorteios.contains(monstro.getNumeroSecreto())) {
            monstroAtordoado = true;
            estado.resetarErrosBomba();
            ui.imprimirNarrativa("A explosao atingiu o alvo. O monstro foi atordoado.");
        } else {
            estado.adicionarErroBomba();
            if (estado.getErrosBomba() >= 3) {
                estado.receberDano(2);
                ui.imprimirNarrativa("A bomba falhou consecutivamente e causou 2 de dano a voce.");
            } else {
                ui.imprimirNarrativa("A bomba errou o alvo.");
            }
        }
    }

    private void processarBombaWorm() {
        int tentativas = Math.max(1, (int) (monstro.getPontosVidaMaxima() * 0.8));
        List<Integer> sorteios = realizarSorteios(tentativas, monstro.getPontosVidaMaxima());
        ui.imprimirNarrativa("Sorteios da Bomba Magica buscando o Worm (Posicao atual: " + posicaoWorm + "): " + sorteios);

        if (sorteios.contains(posicaoWorm)) {
            wormExposto = true;
            estado.resetarErrosBomba();
            ui.imprimirNarrativa("A explosao desenterrou o Worm. Ele esta vulneravel a ataques diretos.");
        } else {
            estado.adicionarErroBomba();
            posicaoWorm = gerador.nextInt(monstro.getPontosVidaMaxima()) + 1;
            if (estado.getErrosBomba() >= 3) {
                estado.receberDano(2);
                ui.imprimirNarrativa("Falha critica com a bomba. Dano de 2 pontos recebido.");
            } else {
                ui.imprimirNarrativa("A bomba explodiu no local errado.");
            }
        }
    }

    private void executarTurnoMonstro() {
        ui.imprimir("\nTurno do Inimigo:");

        if (monstro.getNome().equals("Sandubinha reverse") && ultimoItemUtilizadoJogador != null && ultimoItemUtilizadoJogador != Item.SALSICHINHA) {
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
        ui.imprimirNarrativa("A estatua replica as propriedades do seu ultimo item.");

        int sorteiosCopia = 1;
        switch (ultimoItemUtilizadoJogador) {
            case GUIA_ATENDIMENTO:
                sorteiosCopia = 2;
                break;
            case FATURAMENTUS:
                sorteiosCopia = 4;
                break;
            case AZAH_TRANSMISSAO:
            case COLAR_ESTATUA:
                sorteiosCopia = 11;
                break;
            case ESPADA_ZG:
                sorteiosCopia = 40;
                break;
            case BOMBA_MAGICA:
                ui.imprimirNarrativa("A estatua utilizou a magia de atordoamento contra voce.");
                ui.imprimirNarrativa("Voce perdeu temporariamente sua capacidade de atacar.");
                return;
            default:
                sorteiosCopia = 1;
                break;
        }
        processarAtaqueMonstro(sorteiosCopia);
    }

    private void processarAtaqueMonstro(int quantidadeSorteios) {
        List<Integer> sorteios = realizarSorteios(quantidadeSorteios, estado.getPontosVidaMaxima());
        ui.imprimirNarrativa("Numeros sorteados pelo inimigo: " + sorteios);

        long acertos = sorteios.stream().filter(n -> n == numeroSecretoJogador).count();

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

    private List<Integer> realizarSorteios(int quantidade, int valorMaximo) {
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            lista.add(gerador.nextInt(valorMaximo) + 1);
        }
        return lista;
    }
}