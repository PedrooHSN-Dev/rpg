package ACZG_RPG.phases;

import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.model.Item;
import ACZG_RPG.ui.InterfaceUsuario;

public class Loja {
    public void abrir(EstadoJogo estado, InterfaceUsuario ui) {
        boolean naLoja = true;
        while (naLoja) {
            ui.imprimir("\n--- Loja de Itens Magicos ---");
            ui.imprimir("Os seus pontos de vida atuais: " + estado.getPontosVida());
            ui.imprimir("1. Trevo da autorizacao (Custo: 4 HP)");
            ui.imprimir("2. Simulador de Glozium (Custo: 3 HP)");
            ui.imprimir("3. Send Pearl (Custo: 3 HP)");
            ui.imprimir("4. Vanish (Custo: 3 HP)");
            ui.imprimir("0. Sair da loja");

            int escolha = ui.lerInteiro("Escolha um item para comprar:", 0, 4);

            switch (escolha) {
                case 1:
                    processarCompra(estado, ui, Item.TREVO_AUTORIZACAO);
                    break;
                case 2:
                    processarCompra(estado, ui, Item.SIMULADOR_GLOZIUM);
                    break;
                case 3:
                    processarCompra(estado, ui, Item.SEND_PEARL);
                    break;
                case 4:
                    processarCompra(estado, ui, Item.VANISH);
                    break;
                case 0:
                    naLoja = false;
                    break;
            }
        }
    }

    private void processarCompra(EstadoJogo estado, InterfaceUsuario ui, Item item) {
        if (estado.possuiItem(item)) {
            ui.imprimirNarrativa("Voce ja possui este item no inventario.");
            return;
        }
        if (estado.getPontosVida() > item.getCustoVida()) {
            estado.receberDano(item.getCustoVida());
            estado.adicionarItem(item);

            if (item == Item.TREVO_AUTORIZACAO) {
                estado.setUsosRestantesTrevo(4);
            }
            if (item == Item.SIMULADOR_GLOZIUM) {
                estado.adicionarEnergiaSimulador();
            }

            ui.imprimirNarrativa("Compra realizada com sucesso. O seu novo HP e: " + estado.getPontosVida());
        } else {
            ui.imprimirNarrativa("A sua forca vital e insuficiente para adquirir este item.");
        }
    }
}