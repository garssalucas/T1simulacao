import java.util.*;
public class App{
    private static Escalonador escalonador = new Escalonador();
    private static double tempoGlobal;
    private static ArrayList<Fila> listaDeFilas = new ArrayList<>();
    /* Sistema do Hospital
    private static double[][] transicoesProbabilidadesF0 = {{0,0}   ,   {1,0.7} ,   {2,0.2} ,   {-1,0.1}}; 
    private static double[][] transicoesProbabilidadesF1 = {{0,0.5} ,   {1,0}   ,   {2,0.3} ,   {-1,0.2}};
    private static double[][] transicoesProbabilidadesF2 = {{0,0.2} ,   {1,0}   ,   {2,0}   ,   {-1,0.8}};
    */
    //Sistema do T1
    private static double[][] transicoesProbabilidadesF0 = {{0,0}   ,   {1,0.8} ,   {2,0.2} ,   {-1,0}}; // {filaDestino , probabilidade} Filas 0,1,2
    private static double[][] transicoesProbabilidadesF1 = {{0,0.3} ,   {1,0.5} ,   {2,0}   ,   {-1,0.2}}; // Inserir todas probabilidades de direcionamento
    private static double[][] transicoesProbabilidadesF2 = {{0,0}   ,   {1,0}   ,   {2,0.7} ,   {-1,0.3}}; // Fila -1 = Saída da rede
    
   
    public static void main(String args[]){
        int x=0;
        /*Sistema do Hospital Primeira Chegada em 5
        listaDeFilas.add(new Fila(3, 10, new int[]{5, 10}, new int[]{5, 15}, transicoesProbabilidadesF0)); 
        listaDeFilas.add(new Fila(4, 15, new int[]{0, 0}, new int[]{10, 30}, transicoesProbabilidadesF1)); 
        listaDeFilas.add(new Fila(2, 20, new int[]{0, 0}, new int[]{8, 15}, transicoesProbabilidadesF2));
        */
        // Sistema do T1
        listaDeFilas.add(new Fila(1, 100, new int[]{2, 4}, new int[]{1, 2}, transicoesProbabilidadesF0)); // G/G/1 considero 100 como "capacidade infinita"
        listaDeFilas.add(new Fila(2, 5, new int[]{0, 0}, new int[]{4, 8}, transicoesProbabilidadesF1)); // G/G/2/5 {min,max}chegadas {min,max}saidas
        listaDeFilas.add(new Fila(2, 10, new int[]{0, 0}, new int[]{5, 15}, transicoesProbabilidadesF2)); // G/G/2/10
        
        for (Fila fila : listaDeFilas) {
            fila.setEscalonador(escalonador);
        }
        
        chegada(2);//primeira chegada
        while (x < 100000) {
            Escalonador.EscalonadorEvento proximoEvento = proximoEvento(escalonador);
            int nrPseusdosGerados = escalonador.getNumeroPseusdosGerados();
            //System.out.println(x + " " + nrEventos);
            if (proximoEvento == null) break;
            if (nrPseusdosGerados >= 100000) break; 
            proximoEvento.setStatusEvento(false);
            double tempoAtual = proximoEvento.getTempoEvento();
            int indiceFila = proximoEvento.getIndiceFila();
            int indicePassagem = proximoEvento.getIndicePassagem();
            switch (proximoEvento.tipoEvento) {
                case 'S':
                    saida(tempoAtual, indiceFila);
                    break;
                case 'C':
                    chegada(tempoAtual);
                    break;
                case 'P':
                    passagem(tempoAtual, indiceFila, indicePassagem);
                    break;    
                default:
                    System.out.println("Tipo de evento desconhecido");
            }
            x++;
        }
        imprimirProbabilidades();
    }

    public static void chegada(double tempoAtual)
    {
       for(Fila fila : listaDeFilas) fila.atualizaEstado(tempoAtual-tempoGlobal);
       tempoGlobal = tempoAtual;
       Fila fila1 = listaDeFilas.get(0);
       if(fila1.getTamanhoFila()<fila1.getCapacidadeFila()){
           fila1.inTamanhoFila();
           if(fila1.getTamanhoFila()<=fila1.getServidores()){
               int indicePassagem = fila1.decideProximaFila();
               char tipo;
               if(indicePassagem==-1) tipo = 'S';
               else tipo = 'P';
               escalonador.addEvento(tipo, fila1.getMediaSaida()[0], fila1.getMediaSaida()[1], tempoGlobal, 0, indicePassagem);
           }
       }
       else fila1.inPerdas();
       escalonador.addEvento('C', fila1.getMediaChegada()[0], fila1.getMediaChegada()[1], tempoGlobal, 0 , 0);
    }
    
    public static void saida(double tempoAtual, int indiceFila)
    {
       for(Fila fila : listaDeFilas) fila.atualizaEstado(tempoAtual-tempoGlobal);
       tempoGlobal = tempoAtual;
       Fila fila2 = listaDeFilas.get(indiceFila);
       fila2.outTamanhoFila();
       if(fila2.getTamanhoFila()>=fila2.getServidores()){
            int novoIndicePassagem = fila2.decideProximaFila();
            char tipo;
            if(novoIndicePassagem==-1) tipo = 'S';
            else tipo = 'P';
            escalonador.addEvento(tipo, fila2.getMediaSaida()[0], fila2.getMediaSaida()[1], tempoGlobal, indiceFila, novoIndicePassagem);
       }
    }
    
    public static void passagem(double tempoAtual, int indiceFila, int indicePassagem)
    {
        for(Fila fila : listaDeFilas) fila.atualizaEstado(tempoAtual-tempoGlobal);
        tempoGlobal = tempoAtual;
        Fila fila1 = listaDeFilas.get(indiceFila);
        Fila fila2 = listaDeFilas.get(indicePassagem);
        fila1.outTamanhoFila();      
        if(fila1.getTamanhoFila()>=fila1.getServidores()) {
            int novoIndicePassagem = fila1.decideProximaFila();
            char tipo;
            if(novoIndicePassagem==-1) tipo = 'S';
            else tipo = 'P';
            escalonador.addEvento(tipo, fila1.getMediaSaida()[0], fila1.getMediaSaida()[1], tempoGlobal, indiceFila, novoIndicePassagem);
        }
        if(fila2.getTamanhoFila()<fila2.getCapacidadeFila()){
            fila2.inTamanhoFila();
            if(fila2.getTamanhoFila()<=fila2.getServidores()){
                int novoIndicePassagem = fila2.decideProximaFila();
                char tipo;
                if(novoIndicePassagem==-1) tipo = 'S';
                else tipo = 'P';
                escalonador.addEvento(tipo, fila2.getMediaSaida()[0], fila2.getMediaSaida()[1], tempoGlobal, indicePassagem, novoIndicePassagem);
            }
        }
        else fila2.inPerdas();
    }    
    
    public static Escalonador.EscalonadorEvento proximoEvento(Escalonador escalonador) {
        Escalonador.EscalonadorEvento menorEvento = null;
        double menorTempo = Double.MAX_VALUE;
        for (Escalonador.EscalonadorEvento evento : escalonador.eventos) {
            if (evento.isStatusEvento() && evento.getTempoEvento() < menorTempo) {
                menorEvento = evento;
                menorTempo = evento.getTempoEvento();
            }
        }
        return menorEvento;
    }
    
    public static void imprimirProbabilidades() {
        System.out.println("Tempo Global: " + tempoGlobal);
        int f = 1;
        for (Fila fila : listaDeFilas) {
            System.out.println("\nProbabilidades e tempos de cada estado da Fila " + f + ": ");
            double[] probabilidades = fila.calcularProbabilidadesEstados();
            double[] temposEstados = fila.getEstadosAcumulados();
            for (int i = 0; i < probabilidades.length; i++) {
                if (probabilidades[i] > 0 && temposEstados[i] > 0) { 
                    System.out.printf("Estado %d: Probabilidade=%.2f%%, Tempo=%.2f%n", i, probabilidades[i] * 100, temposEstados[i]);
                }
            }
            System.out.println("Perdas da Fila " + f + ": " + fila.getPerdas());
            f++;
        }
    }
    
}


