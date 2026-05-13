package ACZG_RPG.ui;

import java.util.Scanner;

public class InterfaceUsuario {
    private final Scanner scanner;
    private final int atrasoTextoMilissegundos = 1;

    public InterfaceUsuario() {
        this.scanner = new Scanner(System.in);
    }

    public void imprimir(String texto) {
        System.out.println(texto);
    }

    public void imprimirComEfeito(String texto) {
        for (char caractere : texto.toCharArray()) {
            System.out.print(caractere);
            System.out.flush();
            try {
                Thread.sleep(atrasoTextoMilissegundos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    public void imprimirNarrativa(String texto) {
        System.out.println();
        imprimirComEfeito(texto);
    }

    public void imprimirFala(String personagem, String fala) {
        imprimirComEfeito("[" + personagem + "] - " + fala);
    }

    public String lerEntrada(String prompt) {
        System.out.print("\n" + prompt + " ");
        return scanner.nextLine().trim();
    }

    public boolean lerSimNao(String prompt) {
        while (true) {
            System.out.print("\n" + prompt + " (S/N): ");
            String entrada = scanner.nextLine().trim().toUpperCase();
            if (entrada.equals("S")) {
                return true;
            }
            if (entrada.equals("N")) {
                return false;
            }
        }
    }

    public int lerInteiro(String prompt, int min, int max) {
        while (true) {
            System.out.print("\n" + prompt + " ");
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public void pausar() {
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}