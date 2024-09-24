import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dijkstra extends Thread {

    private static final int FILOSOFOS = 5;
    private static final int ITERACOES = 100;
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
                    int tentativas = 0;

                    while (!conseguiuGarfos && tentativas < 5) {
                        tentativas++;
                        if (id % 2 == 0) {
                            if (garfos[id].tryLock()) {
                                if (garfos[(id + 1) % FILOSOFOS].tryLock()) {
                                    conseguiuGarfos = true;
                                } else {
                                    garfos[id].unlock();
                                }
                            }
                        } else {
                            if (garfos[(id + 1) % FILOSOFOS].tryLock()) {
                                if (garfos[id].tryLock()) {
                                    conseguiuGarfos = true;
                                } else {
                                    garfos[(id + 1) % FILOSOFOS].unlock();
                                }
                            }
                        }

                        if (!conseguiuGarfos) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    if (conseguiuGarfos) {
                        contagemComidas[id]++;
                        System.out.println(id + " comeu");

                        garfos[id].unlock();
                        garfos[(id + 1) % FILOSOFOS].unlock();
                        System.out.println(id + " liberou os garfos");
                    }
                }
            });
            filosofosThreads[i].start();
        }

        for (int i = 0; i < FILOSOFOS; i++) {
            try {
                filosofosThreads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;

        for (int i = 0; i < FILOSOFOS; i++) {
            System.out.println("Filósofo " + i + " comeu " + contagemComidas[i] + " vezes.");
        }

        System.out.println("Tempo total de execução: " + tempoTotal + " ms");
    }
}
