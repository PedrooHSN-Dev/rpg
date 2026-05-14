package ACZG_RPG.model;

import java.util.ArrayList;
import java.util.List;

public class EstadoJogo {
    private int pontosVida;
    private int pontosVidaMaxima;
    private final List<Item> inventario;
    private Item itemEquipado;

    private boolean salsichinhaVivo;
    private boolean salsichinhaAtivoParaAtaque;

    private int usosRestantesTrevo;
    private int energiaSimuladorGlozium;
    private boolean usouVanishBatalhaAtual;

    private boolean pulouParaFinal;
    private boolean temEspadaZG;

    private int penalidadeFaturamentus;
    private int ultimoSorteioAzah;
    private boolean penalidadeAzah;
    private int errosBomba;

    public EstadoJogo() {
        this.pontosVida = 5;
        this.pontosVidaMaxima = 5;
        this.inventario = new ArrayList<>();
        this.inventario.add(Item.ESPADA_SIMPLES);
        this.inventario.add(Item.SALSICHINHA);
        this.itemEquipado = Item.ESPADA_SIMPLES;

        this.salsichinhaVivo = true;
        this.salsichinhaAtivoParaAtaque = false;

        this.usosRestantesTrevo = 0;
        this.energiaSimuladorGlozium = 1;
        this.usouVanishBatalhaAtual = false;

        this.pulouParaFinal = false;
        this.temEspadaZG = false;

        this.penalidadeFaturamentus = 0;
        this.ultimoSorteioAzah = 0;
        this.penalidadeAzah = false;
        this.errosBomba = 0;
    }

    public int getPontosVida() { return pontosVida; }
    public int getPontosVidaMaxima() { return pontosVidaMaxima; }
    public boolean isVivo() { return pontosVida > 0; }

    public void curar(int valor) {
        pontosVida = Math.min(pontosVidaMaxima, pontosVida + valor);
    }

    public void receberDano(int valor) {
        pontosVida = Math.max(0, pontosVida - valor);
    }

    public void aumentarVidaMaxima(int valor) {
        pontosVidaMaxima += valor;
        pontosVida += valor;
    }

    public void reduzirVidaMaxima(int valor) {
        pontosVidaMaxima -= valor;
        pontosVida = Math.min(pontosVida, pontosVidaMaxima);
    }

    public void vencerBatalha() {
        aumentarVidaMaxima(5);
        curar(5);
    }

    public List<Item> getInventario() { return inventario; }

    public void adicionarItem(Item item) {
        if (!inventario.contains(item)) {
            inventario.add(item);
        }
    }

    public boolean possuiItem(Item item) {
        return inventario.contains(item);
    }

    public void removerItem(Item item) {
        inventario.remove(item);
    }

    public Item getItemEquipado() { return itemEquipado; }
    public void setItemEquipado(Item item) { this.itemEquipado = item; }

    public boolean isSalsichinhaVivo() { return salsichinhaVivo; }

    public void matarSalsichinha() {
        this.salsichinhaVivo = false;
        this.salsichinhaAtivoParaAtaque = false;
    }

    public boolean isSalsichinhaAtivoParaAtaque() {
        return salsichinhaAtivoParaAtaque && salsichinhaVivo;
    }

    public void setSalsichinhaAtivoParaAtaque(boolean ativo) {
        if (salsichinhaVivo) {
            this.salsichinhaAtivoParaAtaque = ativo;
        }
    }

    public void alternarSalsichinhaAtaque() {
        if (salsichinhaVivo) {
            this.salsichinhaAtivoParaAtaque = !this.salsichinhaAtivoParaAtaque;
        }
    }

    public int getUsosRestantesTrevo() { return usosRestantesTrevo; }
    public void setUsosRestantesTrevo(int usosRestantesTrevo) { this.usosRestantesTrevo = usosRestantesTrevo; }
    public void decrementarUsoTrevo() { if (usosRestantesTrevo > 0) usosRestantesTrevo--; }

    public int getEnergiaSimuladorGlozium() { return energiaSimuladorGlozium; }
    public void adicionarEnergiaSimulador() { energiaSimuladorGlozium++; }
    public void consumirEnergiaSimulador() { if (energiaSimuladorGlozium > 0) energiaSimuladorGlozium--; }

    public boolean isUsouVanishBatalhaAtual() { return usouVanishBatalhaAtual; }
    public void setUsouVanishBatalhaAtual(boolean usouVanishBatalhaAtual) { this.usouVanishBatalhaAtual = usouVanishBatalhaAtual; }

    public boolean isPulouParaFinal() { return pulouParaFinal; }
    public void setPulouParaFinal(boolean pulouParaFinal) { this.pulouParaFinal = pulouParaFinal; }

    public boolean isTemEspadaZG() { return temEspadaZG; }
    public void setTemEspadaZG(boolean temEspadaZG) { this.temEspadaZG = temEspadaZG; }

    public int getPenalidadeFaturamentus() { return penalidadeFaturamentus; }
    public void setPenalidadeFaturamentus(int penalidadeFaturamentus) { this.penalidadeFaturamentus = penalidadeFaturamentus; }

    public int getUltimoSorteioAzah() { return ultimoSorteioAzah; }
    public void setUltimoSorteioAzah(int ultimoSorteioAzah) { this.ultimoSorteioAzah = ultimoSorteioAzah; }

    public boolean isPenalidadeAzah() { return penalidadeAzah; }
    public void setPenalidadeAzah(boolean penalidadeAzah) { this.penalidadeAzah = penalidadeAzah; }

    public int getErrosBomba() { return errosBomba; }
    public void adicionarErroBomba() { errosBomba++; }
    public void resetarErrosBomba() { errosBomba = 0; }

    public String obterBarraStatus() {
        String statusCachorro = salsichinhaVivo ? (salsichinhaAtivoParaAtaque ? "Ativo" : "Inativo") : "Morto";
        return String.format("HP: %d/%d | Equipado: %s | Salsichinha: %s", pontosVida, pontosVidaMaxima, itemEquipado.getNome(), statusCachorro);
    }
}