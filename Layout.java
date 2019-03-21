/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedannealing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Arthur Angelici
 */
public class Layout {
    //Criei matriz para representar arranjo fisico
    Estados[][] matrix = new Estados[5][4];
    //Criei um vetor para auxiliar na alocacao
    ArrayList<Estados> alocacao = new ArrayList<>();
   
    
    // Metodo para pegar a matriz
    public Estados[][] getMatrix() {
        return matrix;
    }
    //Metodo para modificar matriz
    public void setMatrix(Estados[][] matrix) {
        this.matrix = matrix;
    }
    
    //Construtor da classe
    public Layout(Estados[][] matrix) {
        for (int i =0; i< 5; i++){
            for(int j=0;j<4;j++){
                this.matrix[i][j] = matrix[i][j];
            }
    }
        
    }
    
    //Outro construtor da classe que criar o layout como ja esta definido e os room emptys sao onde serao alocados
    public Layout() {

        //nao precisa colocar externo, apenas referencia com a linha 0
        for (int i = 0; i < 4; i++) {
            matrix[0][i] = Estados.EXTERNO;
        }
        for (int i = 0; i < 4; i++) {
            matrix[1][i] = Estados.ROOM;
        }

        matrix[2][1] = Estados.ROOM_EMPTY;
        matrix[2][2] = Estados.ROOM_EMPTY2;

        matrix[2][0] = Estados.ACESSO_EXTERNO;
        matrix[2][3] = Estados.ACESSO_INTERNO;

        for (int i = 0; i < 4; i++) {
            matrix[3][i] = Estados.ROOM;
        }

        for (int i = 0; i < 4; i++) {
            matrix[4][i] = Estados.EXTERNO2;
        }

    }

    //Metodo para exibir matriz
    public void printLayout() {

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(matrix[i][j] + " ");

            }
            System.out.println("");
        }
    }

       //Metodo para calcular a distancia entre os departamentos
    public double getDistance(ArrayList<Integer> e, ArrayList<Integer> f) {
        double distance =0;
        if(e.size()>2 || f.size()>2){
            
            ArrayList<Double> aux = new ArrayList<>();
            aux.add (Math.sqrt(Math.pow(e.get(0) - f.get(0), 2) + Math.pow(e.get(1) - f.get(1), 2)));
           aux.add(Math.sqrt(Math.pow(e.get(0) - f.get(2), 2) + Math.pow(e.get(1) - f.get(3), 2)));
            aux.add(Math.sqrt(Math.pow(e.get(0) - f.get(4), 2) + Math.pow(e.get(1) - f.get(5), 2)));
            aux.add(Math.sqrt(Math.pow(e.get(0) - f.get(6), 2) + Math.pow(e.get(1) - f.get(7), 2)));
            Collections.sort(aux);
            distance = aux.get(0);
            
        }
        else{

        distance = Math.sqrt(Math.pow(e.get(0) - f.get(0), 2) + Math.pow(e.get(1) - f.get(1), 2));
        //distancia entre dois pontos
        }
        return distance;

    }

 
    //Metodo para verificar as posicoes de um determinado departamento alocado
    public ArrayList<Integer> searchPosition(Estados e) {  //considerando um ponto so
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] == e) {
                    positions.add(i);
                    positions.add(j);
                }

            }

        }
        return positions;

    }

    //Metodo para alocar aleatoriamente os departamentos
    public void inicialAleatorio() {
        //aqui adiciona naquele vetor que de auxilio
        alocacao.add(Estados.ELETRICISTAS);
        alocacao.add(Estados.ELETRICISTAS2);
        alocacao.add(Estados.QUALIDADE);
        alocacao.add(Estados.QUALIDADE2);
        alocacao.add(Estados.FABRICACAO_ESTRUTURAL);
        alocacao.add(Estados.FABRICACAO_ESTRUTURAL2);
        alocacao.add(Estados.FABRICACAO_ESTRUTURAL3);
        alocacao.add(Estados.PLANEJAMENTO);

        //biblioteca que sorteia aleatoriamente os departamentos
        Collections.shuffle(alocacao);

        for (int i = 0; i < 4; i++) {
            matrix[1][i] = alocacao.get(i);

        }

        for (int i = 0; i < 4; i++) {
            matrix[3][i] = alocacao.get(i + 4);
        }

    }

      // A funcao objetivo, ou seja utilizando as restriçoes e criei pesos para elas
    // Por exemplo areas de qualidades estarem perto tem peso 10
    // qualidade estar perto de fabricacao tem peso 5 ..
    // com isso o objetivo e minimizar esse valor para obter as menores distancias
    public double calcularEnergy() {
        double energyTotal = 0;
        double distanceEletricistas;
        double distanceQualidade;
        double distanceFabricacao;
        double distanceFabricacaoAcessoExterno;
        double distanceFabricacaoExterno;
        double distanceFabricacaoQualidade;
        double distanceEletricistaAcessoInterno;
        double distancePlanejamentoAcessoInterno;
        double distancePlanejamentoExterno;

        distanceEletricistas = getDistance(searchPosition(Estados.ELETRICISTAS), searchPosition(Estados.ELETRICISTAS2));
        
        distanceQualidade = getDistance(searchPosition(Estados.QUALIDADE), searchPosition(Estados.QUALIDADE2));
        
        distanceFabricacao = getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL), searchPosition(Estados.FABRICACAO_ESTRUTURAL2))
        +getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL), searchPosition(Estados.FABRICACAO_ESTRUTURAL3))
        +getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL2), searchPosition(Estados.FABRICACAO_ESTRUTURAL3));
                
        distanceFabricacaoAcessoExterno = (getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL), searchPosition(Estados.ACESSO_EXTERNO)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL3), searchPosition(Estados.ACESSO_EXTERNO)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL2), searchPosition(Estados.ACESSO_EXTERNO)));
        
        distanceFabricacaoQualidade = (getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL), searchPosition(Estados.QUALIDADE)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL3), searchPosition(Estados.QUALIDADE)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL2), searchPosition(Estados.QUALIDADE)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL), searchPosition(Estados.QUALIDADE2)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL3), searchPosition(Estados.QUALIDADE2)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL2), searchPosition(Estados.QUALIDADE2)));
       
        distanceEletricistaAcessoInterno = getDistance(searchPosition(Estados.ELETRICISTAS), searchPosition(Estados.ACESSO_INTERNO)) +
                getDistance(searchPosition(Estados.ELETRICISTAS2), searchPosition(Estados.ACESSO_INTERNO));
        
        distancePlanejamentoAcessoInterno = getDistance(searchPosition(Estados.PLANEJAMENTO), searchPosition(Estados.ACESSO_INTERNO));
                
        distanceFabricacaoExterno =(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL), searchPosition(Estados.EXTERNO)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL3), searchPosition(Estados.EXTERNO)))
        +(getDistance(searchPosition(Estados.FABRICACAO_ESTRUTURAL2), searchPosition(Estados.EXTERNO)));
                
        distancePlanejamentoExterno =getDistance(searchPosition(Estados.PLANEJAMENTO), searchPosition(Estados.ACESSO_EXTERNO));
        
        
        energyTotal = 10*distanceEletricistas + 10*distanceFabricacao + 10*distanceQualidade + 5*distanceEletricistaAcessoInterno 
                + 5*distanceFabricacaoAcessoExterno + 5*distanceFabricacaoExterno + 5*distanceFabricacaoQualidade + 5*distancePlanejamentoAcessoInterno
                + 5*distancePlanejamentoExterno;
        
        
        
        return energyTotal;

    }

}
