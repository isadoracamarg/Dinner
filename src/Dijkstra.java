import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dijkstra extends Thread {

    private static final int FILOSOFOS = 5;
    private static final int ITERACOES = 100; // Número de vezes que cada filósofo vai comer
    private static final Lock[] garfos = new Lock[FILOSOFOS];
    private static final int[] contagemComidas = new int[FILOSOFOS];
    private static final Thread[] filosofosThreads = new Thread[FILOSOFOS];

    public static void main(String[] args) {
        for (int i = 0; i < FILOSOFOS; i++) {
            garfos[i] = new ReentrantLock();
        }

        long tempoInicio = System.currentTimeMillis();

        for (int i = 0; i < FILOSOFOS; i++) {
            final int id = i;
            filosofosThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERACOES; j++) {
                    System.out.println(id + " senta");

                    boolean conseguiuGarfos = false;
                    int tentativas = 0;  // Limitar o número de tentativas

                    while (!conseguiuGarfos && tentativas < 5) {  // Max 5 tentativas
                        tentativas++;
                        if (id % 2 == 0) {
                            if (garfos[id].tryLock()) {
                                if (garfos[(id + 1) % FILOSOFOS].tryLock()) {
                                    conseguiuGarfos = true;  // Conseguiu pegar os dois garfos
                                } else {
                                    garfos[id].unlock();  // Não conseguiu o segundo garfo, libera o primeiro
                                }
                            }
                        } else {
                            if (garfos[(id + 1) % FILOSOFOS].tryLock()) {
                                if (garfos[id].tryLock()) {
                                    conseguiuGarfos = true;  // Conseguiu pegar os dois garfos
                                } else {
                                    garfos[(id + 1) % FILOSOFOS].unlock();  // Não conseguiu o primeiro garfo, libera o segundo
                                }
                            }
                        }

                        if (!conseguiuGarfos) {
                            try {
                                Thread.sleep(10);  // Espera um pouco antes de tentar novamente
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    if (conseguiuGarfos) {
                        contagemComidas[id]++;
                        System.out.println(id + " comeu");

                        // Libera os garfos após comer
                        garfos[id].unlock();
                        garfos[(id + 1) % FILOSOFOS].unlock();
                        System.out.println(id + " liberou os garfos");
                    }
                }
            });
            filosofosThreads[i].start(); // Inicia a thread do filósofo
        }

        // Aguardar o término dos filósofos
        for (int i = 0; i < FILOSOFOS; i++) {
            try {
                filosofosThreads[i].join(); // Espera a thread do filósofo terminar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;

        // Mostrar resultados
        for (int i = 0; i < FILOSOFOS; i++) {
            System.out.println("Filósofo " + i + " comeu " + contagemComidas[i] + " vezes.");
        }

        System.out.println("Tempo total de execução: " + tempoTotal + " ms");
    }
}
