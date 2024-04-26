package spacca.spacca;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Classe per una singola mano di carte.
 * Contiene una lista di carte e metodi per controllare i valori.
 */
public class Mano {
    private final ArrayList<CartedelGioco> mano;

    /**
     * Costruttore per la classe Mano.
     *
     * @param mano lista di carte.
     */
    public Mano(List<CartedelGioco> mano) {
        this.mano = (ArrayList<CartedelGioco>) mano;
    }

    public List<CartedelGioco> getMano() {
        return mano;
    }

    /**
     * Metodo per trovare una carta nella mano.
     *
     * @param faccia la faccia della carta cercata.
     * @return la carta trovata, o null se la carta non è stata trovata.
     */
    public CartedelGioco trovaCarta(int faccia) {
        return this.mano.stream().filter(carta -> carta.getFaccia() == faccia)
                .findAny()
                .orElse(null);
    }

}