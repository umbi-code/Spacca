package spacca.spacca;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe che rappresenta un mazzo di carte da gioco.
 */
public class MazzoCarte {
    private ArrayList<CartedelGioco> carte;
    private Random random;

    /**
     * Costruttore per il mazzo di carte.
     */
    public MazzoCarte() {
        random = new Random();
        carte = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            for (int k = 1; k <= 13; k++) {
                carte.add(new CartedelGioco(k, "spara")); // Aggiunge una nuova carta al mazzo
            }
        }
    }

    /**
     * Metodo che restituisce la lista di carte nel mazzo.
     * @return restituisce la lista di carte
     */
    public ArrayList<CartedelGioco> getCarte() {
        return carte;
    }

    /**
     * Restituisce una lista di carte dal mazzo.
     * Rimuove le carte dal mazzo.
     *
     * @param n il numero di carte da restituire
     * @return restituisce un ArrayList di carte
     */
    public ArrayList<CartedelGioco> distribuisciMano(int n){
        ArrayList<CartedelGioco> mano = new ArrayList<>();
        while (n >= 1 && !carte.isEmpty()) {
            CartedelGioco carta = carte.remove(random.nextInt(carte.size())); // Rimuove e ottiene una carta casuale
            mano.add(carta); // Aggiunge la carta alla mano
            n--;
        }
        return mano;
    }

    public static void main(String[] args) {
        MazzoCarte mazzo = new MazzoCarte(); // Crea un nuovo mazzo di carte
        ArrayList<CartedelGioco> mano = mazzo.distribuisciMano(5); // Distribuisce 5 carte
        System.out.println("Mano del giocatore:");
        for (CartedelGioco carta : mano) {
            System.out.println(carta.getAsString()); // Stampa ogni carta distribuita
        }
    }
}