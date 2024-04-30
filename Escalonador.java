import java.util.ArrayList;
import java.util.List;

public class Escalonador {
    private double proximo;
    private final double m = Math.pow(2, 32); 
    public List<EscalonadorEvento> eventos;
    public int numeroPseusdosGerados;

    public Escalonador() {
        this.proximo = 1025555898;
        this.eventos = new ArrayList<>();
        this.numeroPseusdosGerados = 0;
    }

    public void addEvento(char tipoEvento, int tempoMinAtendimento, int tempoMaxAtendimento, double tempoGlobal, int indiceF, int indiceP) {
        this.eventos.add(new EscalonadorEvento(tipoEvento, tempoGlobal + agendaEvento(tempoMinAtendimento,tempoMaxAtendimento), indiceF, indiceP));
        //imprimirTodosEventos();
    }

    private double agendaEvento(int min, int max) {
        double evento = (max - min) * (proximo / m) + min;
        nextPseudo();
        return evento;
    }

    private double pseudos() {
        int a = 1664525;
        int c = 1013904223;
        proximo = (a * proximo + c) % m;
        numeroPseusdosGerados++;
        return proximo;
    }
    
    public double getProximo(){
        double aux = proximo / m;
        nextPseudo();
        return aux;
    }
    
    private void nextPseudo() {
        proximo = pseudos();
    }
    
    public int getNumeroPseusdosGerados() {
            return this.numeroPseusdosGerados;
    }
    
    public class EscalonadorEvento {
        public char tipoEvento;
        public double tempoEvento;
        public boolean statusEvento;
        public int indiceFila;
        public int indicePassagem;
        
        public EscalonadorEvento(char tipoEvento, double tempoEvento,int indiceF, int indiceP) {
            this.tipoEvento = tipoEvento;
            this.tempoEvento = tempoEvento;
            this.statusEvento = true;
            this.indiceFila = indiceF;
            this.indicePassagem = indiceP;
        }
        
        public int getIndiceFila() {
            return this.indiceFila;
        }
                        
        public int getIndicePassagem() {
            return this.indicePassagem;
        }

        public void setStatusEvento(boolean status) {
            this.statusEvento = status;
        }

        public double getTempoEvento() {
            return tempoEvento;
        }

        public boolean isStatusEvento() {
            return statusEvento;
        }
        @Override
        public String toString() {
            return "EscalonadorEvento{" +
                    "tipoEvento=" + tipoEvento +
                    ", tempoEvento=" + tempoEvento +
                    ", statusEvento=" + statusEvento +
                    "}";
        }
    }
    public void imprimirTodosEventos() {
        System.out.println("\nTodos os eventos:");
        for (EscalonadorEvento evento : eventos) {
            System.out.println(evento.toString());
        }
        System.out.println();
    }
}
