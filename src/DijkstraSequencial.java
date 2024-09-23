public class DijkstraSequencial { 

        private static final int FILOSOFOS = 5;
        private static final int ITERACOES = 100; // Número de vezes que cada filósofo vai comer
        private static final int[] contagemComidas = new int[FILOSOFOS];
    
        public static void main(String[] args) {
    
            long tempoInicio = System.currentTimeMillis();
    
            // Filósofos comendo de forma sequencial
            for (int i = 0; i < FILOSOFOS; i++) {
                for (int j = 0; j < ITERACOES; j++) {
                    System.out.println(i + " senta");
                    System.out.println(i + " pegou o primeiro garfo");
                    System.out.println(i + " pegou o segundo garfo");
                    
                    contagemComidas[i]++; // Aumenta a contagem de comidas
                    System.out.println(i + " comeu");
                    
                    System.out.println(i + " liberou os garfos");
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
