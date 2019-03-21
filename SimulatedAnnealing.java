/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedannealing;

/**
 *
 * @author Arthur Angelici
 */
public class SimulatedAnnealing {

    // Calcula a probabilidade de aceitar uma solucao
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // Se a nova solucao é melhor, entao aceita ela
        if (newEnergy < energy) {
            return 1.0;
        }
        // Se a nova solucao e pior, entao calcula a probabilidade de aceitar ela
        return Math.exp((energy - newEnergy) / temperature);
    }

   
    public static void main(String[] args) {

        // Seta o temp em 1000
        double temp = 10000;

        // "taxa de resfriamento"
        double coolingRate = 0.003;

        // Inicializa a solucao inicial
        Layout currentSolution = new Layout();
        currentSolution.inicialAleatorio();
        currentSolution.printLayout();
        currentSolution.calcularEnergy();

        System.out.println("Initial solution distance: " + currentSolution.calcularEnergy());

        // Guarda a melhor solucao
        Layout best = new Layout(currentSolution.getMatrix());

        // Loop ate que resfrie
        while (temp > 1) {
            // Cria uma solucao vizinha
            Layout newSolution = new Layout(currentSolution.getMatrix());

            // Pega duas posicao aleatoria ja alocada
            int pos11 = (int) (2 * Math.random());
            int pos1 = (int) (4 * Math.random());
            int pos22 = (int) (2 * Math.random());
            int pos2 = (int) (4 * Math.random());

            // Pega os departamentos dessas posicoes
            if (pos11 == 0) {
                pos11 = 3;
            }
            if (pos22 == 0) {
                pos22 = 3;
            }
            System.out.println(pos11);

            Estados swap1 = newSolution.getMatrix()[pos11][pos1];
            Estados swap2 = newSolution.getMatrix()[pos22][pos2];

            System.out.println(swap1);
            System.out.println(swap2);

            // Inverte eles
            newSolution.matrix[pos11][pos1] = swap2;
            newSolution.matrix[pos22][pos2] = swap1;

            // Pega a energia da solucao antiga e da nova (funcao objetivo)
            double currentEnergy = currentSolution.calcularEnergy();
            double neighbourEnergy = newSolution.calcularEnergy();

            System.out.println(currentEnergy);
            System.out.println(neighbourEnergy);

            // Decide se devemos aceitar a solucao vizinha
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Layout(newSolution.getMatrix());
            }

            // Mantem a solucao melhor se for o caso
            if (currentSolution.calcularEnergy() < best.calcularEnergy()) {
                best = new Layout(currentSolution.getMatrix());
            }

            // Sistema de resfriamento
            temp *= 1 - coolingRate;
        }

        System.out.println("Final solution distance: " + best.calcularEnergy());
        best.printLayout();
        //System.out.println("Tour: " + best.printLayout());

    }

}
