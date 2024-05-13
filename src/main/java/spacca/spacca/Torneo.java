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

public class Torneo extends Partita {
    private String codiceTorneo;
    private ArrayList<String> codiciPartite = new ArrayList<>();

    private ArrayList<Partita> partite = new ArrayList<>();

    public Torneo() {
        // Genera un codice partita casuale
        this.codiceTorneo = generaCodiceTorneo();
        this.partite = generaPartiteTorneo();
        this.codiciPartite = aggiungiCodiciPartite();
    }

    private ArrayList<Partita> generaPartiteTorneo() {
        //genera 7 partite
        for (int i = 0; i < 7; i++) {
            Partita p = new Partita();
            partite.add(p);
        }
        return partite;
    }

    public ArrayList<String> aggiungiCodiciPartite() {
        for (int i = 0; i < partite.size(); i++){
            String s = partite.get(i).getCodicePartita();
            codiciPartite.add(s);
        }
        return codiciPartite;
    }

    private String generaCodiceTorneo() {
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

    public ArrayList<Partita> getPartiteTorneo() {
        return this.partite;
    }

    public void salvaTorneo() {
        // Salva i dati della partita nel file JSON
        JSONObject torneoJson = new JSONObject();
        torneoJson.put("codice", this.codiceTorneo);
        torneoJson.put("codiciPartite", this.codiciPartite);
        //partitaJson.put("Vincitore", "");

        // Leggi il contenuto attuale del file JSON
        JSONArray torneiJson = leggiTornei();

        // Aggiungi la nuova partita all'array JSON
        torneiJson.add(torneoJson);

        // Scrivi l'array JSON aggiornato nel file
        scriviTornei(torneiJson);
    }

    private JSONArray leggiTornei() {
        JSONArray torneiJson = new JSONArray();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/tornei.json")) {
            // Parsing del contenuto in un array JSON
            torneiJson = (JSONArray) new JSONParser().parse(reader);
        } catch (IOException | ParseException e) {
            // Se il file non esiste o è vuoto, restituisci un array vuoto
            e.printStackTrace();
        }
        return torneiJson;
    }

    private void scriviTornei(JSONArray torneiJson) {

        // Scrivi l'array JSON nel file
        try (FileWriter file = new FileWriter("src/main/resources/spacca/spacca/tornei.json")) {
            file.write(torneiJson.toJSONString());
            System.out.println("Partita salvata con successo.");
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }


}
