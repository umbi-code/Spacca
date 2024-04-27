package spacca.spacca;

import java.util.ArrayList;
import java.util.Random;

/**
 * Rappresenta una carta da gioco. Una carta da gioco ha un numero (faccia) compreso tra
 * 1 e 7, dove 1 è chiamato "".
 * La carta può anche essere diversa a seconda della sua funzionalità.
 */
public class CartedelGioco {

    private final int faccia; // un numero compreso tra 1 e 7
    private final String abilita; // rappresenta l'abilità della carta

    /**
     * Crea un'istanza di CartaDaGioco con una faccia e un'abilità.
     * @param faccia Il valore della carta.
     * @param abilita L'abilità della carta.
     */
    public CartedelGioco(int faccia, String abilita) {
        this.faccia = faccia;
        this.abilita = abilita;
    }

    /**
     * Restituisce la rappresentazione della carta come stringa.
     * Ad esempio, "4 di cuori" o "Asso di picche".
     *
     * @return la rappresentazione della carta come stringa
     */
    public String getAsString() {
        switch (faccia) {
            case 1:
                return "Asso di " + abilita;
            case 11:
                return "Jack di " + abilita;
            case 12:
                return "Regina di " + abilita;
            case 13:
                return "Re di " + abilita;
            default:
                return faccia + " di " + abilita;
        }
    }

    /**
     * Restituisce la faccia della carta (valore compreso tra 1 e 7).
     *
     * @return la faccia della carta
     */
    public int getFaccia() {
        return faccia;
    }

    /**
     * Metodo che ottiene l'URL in formato png per la carta.
     * @return restituisce l'URL in formato stringa per la carta.
     */
    public String getUrlString() {
        String result = "";
        if (this.faccia <= 10 && this.faccia > 1) {
            result += String.valueOf(this.faccia);
        } else {
            switch (this.faccia) {
                case 11:
                    result += "jack";
                    break;
                case 12:
                    result += "queen";
                    break;
                case 13:
                    result += "king";
                    break;
                case 1:
                    result += "ace";
                    break;
            }
        }
        result += "_of_" + abilita.toLowerCase() + ".png";
        return result;
    }
}

/**
 * Rappresenta una carta da gioco con l'abilità "spara".
 */
class CartaSpara extends CartedelGioco {

    /**
     * Costruttore per la carta "spara".
     * @param faccia Il valore della carta.
     */
    public CartaSpara(int faccia) {
        super(faccia, "spara");
    }

    /**
     * Restituisce la rappresentazione della carta come stringa.
     * Ad esempio, "4 di spara" o "Asso di spara".
     *
     * @return la rappresentazione della carta come stringa
     */
    @Override
    public String getAsString() {
        return super.getAsString();
    }
}