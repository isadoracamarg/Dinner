import java.util.concurrent.Semaphore;

public class Semaforos extends Thread {

    private static final int FILOSOFOS = 5;
    private static final int ITERACOES = 100;
    private static final Semaphore[] garfos = new Semaphore[FILOSOFOS];
    private static final Semaphore semaforo = new Semaphore(2); // Limitar a 2 filósofos comendo
    private static final int[] contagemComidas = new int[FILOSOFOS];
    private static final Thread[] filosofosThreads = new Thread[FILOSOFOS];

    public static void main(String[] args) {
        for (int i = 0; i < FILOSOFOS; i++) {
            garfos[i] = new Semaphore(1);
        }

        long tempoInicio = System.currentTimeMillis();

        for (int i = 0; i < FILOSOFOS; i++) {
            final int id = i;
            filosofosThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERACOES; j++) {
                    try {
                        semaforo.acquire(); // Aguarda até que possa comer
                        System.out.println(id + " senta");
                        if (id % 2 == 0) {
                            garfos[id].acquire();
                            System.out.println(id + " pegou o primeiro garfo");
                            garfos[(id + 1) % FILOSOFOS].acquire();
                            System.out.println(id + " pegou o segundo garfo");
                        } else {
                            garfos[(id + 1) % FILOSOFOS].acquire();
                            System.out.println(id + " pegou o segundo garfo");
                            garfos[id].acquire();
                            System.out.println(id + " pegou o primeiro garfo");
                        }

                        contagemComidas[id]++;
                        garfos[id].release();
                        garfos[(id + 1) % FILOSOFOS].release();
                        System.out.println(id + " liberou os garfos");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        semaforo.release(); // Libera o semáforo
                    }
                }
            });
            filosofosThreads[i].start(); // Inicia a thread do filósofo
        }

        // Aguardar o término das threads
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

        System.out.println("Tempo total de execução: " + (tempoTotal / 1000.0) + " segundos");
    }
}
