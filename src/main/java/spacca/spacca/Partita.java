package spacca.spacca;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partita {
    private String codicePartita;
    private List<String> sfidanti;
    private int viteGiocatore1;
    private int viteGiocatore2;
    private List<Boolean> carteAlCentro;
    private boolean turnoG1;



    public Partita() {
        // Genera un codice partita casuale
        this.codicePartita = generaCodicePartita();
        this.sfidanti = new ArrayList<>();
        this.viteGiocatore1 = 3;
        this.viteGiocatore2 = 3;
        this.carteAlCentro = CarteAlCentro();
        this.turnoG1=true;
    }

    public ArrayList<Boolean> CarteAlCentro() {
        // Creazione di un oggetto Random per generare valori casuali
        Random random = new Random();

        // Generazione di un numero casuale compreso tra 2 e 6 per la dimensione dell'ArrayList
        int dimensione = random.nextInt(5) + 2; // Genera un numero tra 2 e 6 inclusi

        // Creazione dell'ArrayList di booleani con dimensione casuale
        ArrayList<Boolean> booleanList = new ArrayList<>();

        // Aggiunta di valori booleani casuali all'ArrayList
        for (int i = 0; i < dimensione; i++) {
            boolean valore = random.nextBoolean();
            booleanList.add(valore);
        }

        // Stampa dell'ArrayList di booleani
        System.out.println("ArrayList di booleani:");
        for (int i = 0; i < booleanList.size(); i++) {
            System.out.println("Elemento " + i + ": " + booleanList.get(i));

        }
        return booleanList;
    }

    private String generaCodicePartita() {
        // Genera un codice partita casuale di lunghezza 5
        String numeri = "0123456789";
        StringBuilder sb = new StringBuilder(5);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(numeri.length());
            sb.append(numeri.charAt(index));
        }
        return sb.toString();
    }

    public void aggiungiSfidante(String sfidante) {
        // Aggiungi uno sfidante alla partita
        this.sfidanti.add(sfidante);
    }

    public void salvaPartita() {
        // Salva i dati della partita nel file JSON
        JSONObject partitaJson = new JSONObject();
        partitaJson.put("codice", this.codicePartita);
        partitaJson.put("sfidanti", this.sfidanti);
        partitaJson.put("viteGiocatore1", this.viteGiocatore1);
        partitaJson.put("viteGiocatore2", this.viteGiocatore2);
        partitaJson.put("carteAlCentro", this.carteAlCentro);
        partitaJson.put("turnoG1", this.turnoG1);

        // Leggi il contenuto attuale del file JSON
        JSONArray partiteJson = leggiPartite();

        // Aggiungi la nuova partita all'array JSON
        partiteJson.add(partitaJson);

        // Scrivi l'array JSON aggiornato nel file
        scriviPartite(partiteJson);
    }

    private JSONArray leggiPartite() {
        JSONArray partiteJson = new JSONArray();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsing del contenuto in un array JSON
            partiteJson = (JSONArray) new JSONParser().parse(reader);
        } catch (IOException | ParseException e) {
            // Se il file non esiste o è vuoto, restituisci un array vuoto
            e.printStackTrace();
        }
        return partiteJson;
    }

    private void scriviPartite(JSONArray partiteJson) {

        // Scrivi l'array JSON nel file
        try (FileWriter file = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
            file.write(partiteJson.toJSONString());
            System.out.println("Partita salvata con successo.");
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }


}