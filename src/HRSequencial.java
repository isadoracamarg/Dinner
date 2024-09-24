import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HRSequencial extends Thread {

    private static final int FILOSOFOS = 5;
    private static final int ITERACOES = 100;
    private static final Lock[] garfos = new ReentrantLock[FILOSOFOS];
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
                    System.out.println("Filósofo " + id + " senta");

                    int primeiroGarf = Math.min(id, (id + 1) % FILOSOFOS);
                    int segundoGarf = Math.max(id, (id + 1) % FILOSOFOS);

                    garfos[primeiroGarf].lock();
                    garfos[segundoGarf].lock();

                    try {
                        contagemComidas[id]++;
                        System.out.println("Filósofo " + id + " comeu.");
                    } finally {

                        garfos[segundoGarf].unlock();
                        garfos[primeiroGarf].unlock();
                        System.out.println("Filósofo " + id + " liberou os garfos.");
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
