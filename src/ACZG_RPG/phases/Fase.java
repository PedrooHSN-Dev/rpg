package ACZG_RPG.phases;

import ACZG_RPG.model.EstadoJogo;
import ACZG_RPG.ui.InterfaceUsuario;

public interface Fase {
    String getNome();
    boolean executar(EstadoJogo estado, InterfaceUsuario ui);
}