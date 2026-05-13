package ACZG_RPG;

import ACZG_RPG.ui.InterfaceUsuario;

public class Main {
    public static void main(String[] args) {
        InterfaceUsuario ui = new InterfaceUsuario();
        boolean jogarNovamente = true;

        while (jogarNovamente) {
            Jogo jogo = new Jogo();
            jogo.iniciar();

            jogarNovamente = ui.lerSimNao("\nDeseja jogar novamente?");
        }

        ui.imprimirNarrativa("Obrigado por jogar!");
    }
}