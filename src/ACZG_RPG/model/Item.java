package ACZG_RPG.model;

public enum Item {
    ESPADA_SIMPLES("Espada Simples", "Arma padrao sem aprimoramentos", 0),
    SALSICHINHA("Salsichinha", "Mascote leal, pode ajudar no ataque", 0),
    GUIA_ATENDIMENTO("Guia de Atendimento", "Sorteia 2 numeros", 0),
    FATURAMENTUS("Faturamentus", "Sorteia 4 numeros, mas pune em caso de erro", 0),
    BOMBA_MAGICA("Bomba Magica", "Atordoa o inimigo se acertar", 0),
    AZAH_TRANSMISSAO("Azah Transmissao", "Permite voar e sorteia 11 numeros", 0),
    COLAR_ESTATUA("Colar da Estatua", "Custa 3 de vida, sorteia 11 numeros", 0),
    ESPADA_ZG("Espada ZG", "Arma lendaria, sorteia 40 numeros", 0),
    TREVO_AUTORIZACAO("Trevo da Autorizacao", "Garante acerto no alvo (4 usos)", 4),
    SIMULADOR_GLOZIUM("Simulador de Glozium", "Evita ataque do monstro", 3),
    SEND_PEARL("Send Pearl", "Teleporta para o chefe final", 3),
    VANISH("Vanish", "Elimina a energia de ataque do monstro", 3),
    ELIXIR_VIDA("Elixir de Vida", "Restaura e aumenta vida", 0);

    private final String nome;
    private final String descricao;
    private final int custoVida;

    Item(String nome, String descricao, int custoVida) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoVida = custoVida;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCustoVida() {
        return custoVida;
    }
}