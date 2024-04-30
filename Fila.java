import java.util.Arrays;
import java.util.Comparator;
public class Fila
{
    private int servidores;
    private int capacidadeFila;
    private int tamanhoFila;
    private int[] mediaChegada = new int[2];
    private int[] mediaSaida = new int[2];
    private int perdas;
    private double estado[];
    private double[][] transicoesProbabilidades;
    private Escalonador escalonador;
        
    public Fila(int servidores, int capacidade, int chegadas[], int atendimentos[], double[][] transicoes){
        this.servidores = servidores;
        this.capacidadeFila = capacidade;
        this.mediaChegada[0] = chegadas[0];
        this.mediaChegada[1] = chegadas[1];
        this.mediaSaida[0] = atendimentos[0];
        this.mediaSaida[1] = atendimentos[1];
        this.tamanhoFila = 0;
        this.perdas = 0;
        this.estado = new double[capacidade + 1];
        this.transicoesProbabilidades = transicoes;
    }
    
    public int getCapacidadeFila(){
        return this.capacidadeFila;
    }
    
    public int getTamanhoFila(){
        return this.tamanhoFila;
    }
    
    public void inTamanhoFila(){
        this.tamanhoFila++;
    }
    
    public void outTamanhoFila(){
        this.tamanhoFila--;
    }
    
    public int getServidores(){
        return this.servidores;
    }
    
    public int[] getMediaChegada(){
        return this.mediaChegada;
    }
    
    public int[] getMediaSaida(){
        return this.mediaSaida;
    }
    
    public int getPerdas(){
        return this.perdas;
    }
    
    public void inPerdas(){
        this.perdas++;
    }
    
    public void atualizaEstado(double tempo) {
        estado[tamanhoFila] += tempo;
        //System.out.println("\nLista de estados da fila:");
        //for (int i = 0; i < estado.length; i++) {
        //    System.out.println("Estado " + i + ": " + estado[i]);
        //}
     }

    public double[] getEstadosAcumulados() {
        return estado;
    }
    
    public void setEscalonador(Escalonador escalonador) {
        this.escalonador = escalonador;
    }

    public double[] calcularProbabilidadesEstados() {
        double[] probabilidades = new double[estado.length];
        double total = 0;
        for (double value : estado) {
            total += value;
        }
        for (int i = 0; i < estado.length; i++) {
            probabilidades[i] = estado[i] / total;
        }
        return probabilidades;
    }
    
    public int decideProximaFila() {
        Arrays.sort(transicoesProbabilidades, Comparator.comparingDouble(a -> a[1]));
        double aleatorio = escalonador.getProximo();
        double sum = 0.0;
        for (int i = 0; i < transicoesProbabilidades.length; i++) {
            sum += transicoesProbabilidades[i][1];
            if (aleatorio < sum) {
                return (int) transicoesProbabilidades[i][0]; 
            }
        }
        return -777;
    }
}
