package spacca.spacca;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginManager {
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/spacca/spacca/admin_credentials.json";

    public static boolean verificaCredenziali(String username, String password) {
        JSONParser parser = new JSONParser();

        try {
            // Leggi il file JSON delle credenziali degli amministratori
            Object obj = parser.parse(new FileReader(CREDENTIALS_FILE_PATH));
            JSONArray jsonArray = (JSONArray) obj;

            // Itera su ciascun oggetto JSON all'interno dell'array
            for (Object json : jsonArray) {
                JSONObject jsonObject = (JSONObject) json;

                // Recupera le credenziali dell'amministratore dall'oggetto JSON corrente
                String usernameAdmin = (String) jsonObject.get("username");
                String passwordAdmin = (String) jsonObject.get("password");

                // Confronta le credenziali con quelle inserite dall'utente
                if (username.equals(usernameAdmin) && password.equals(passwordAdmin)) {
                    return true; // Credenziali corrette
                }
            }

            // Nessuna corrispondenza trovata
            return false;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verificaUtente(String username) {
        JSONParser parser = new JSONParser();

        try {
            // Leggi il file JSON delle credenziali degli amministratori
            Object obj = parser.parse(new FileReader(CREDENTIALS_FILE_PATH));
            JSONArray jsonArray = (JSONArray) obj;

            // Itera su ciascun oggetto JSON all'interno dell'array
            for (Object json : jsonArray) {
                JSONObject jsonObject = (JSONObject) json;

                // Recupera le credenziali dell'amministratore dall'oggetto JSON corrente
                String usernameUtente = (String) jsonObject.get("username");
                String passwordUtente = (String) jsonObject.get("password");

                // Confronta le credenziali con quelle inserite dall'utente
                if (username.equals(usernameUtente)&&(passwordUtente==null)) {
                    return true; // Credenziali corrette
                }
            }

            // Nessuna corrispondenza trovata
            return false;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registraUtente(String username, String password) {
        // Carica il file JSON delle credenziali degli utenti
        JSONArray jsonArray = leggiCredenziali();

        // Controlla se l'username è già presente
        for (Object json : jsonArray) {
            JSONObject jsonObject = (JSONObject) json;
            String existingUsername = (String) jsonObject.get("username");
            if (existingUsername.equals(username)) {
                System.out.println("Username già esistente. Impossibile registrare l'utente.");
                return false; // Username già presente, registrazione fallita
            }
        }

        // Crea un nuovo oggetto JSON per l'utente
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);

        // Aggiungi il nuovo utente all'array
        jsonArray.add(newUser);

        // Scrivi l'array aggiornato nel file JSON
        try (FileWriter fileWriter = new FileWriter(CREDENTIALS_FILE_PATH)) {
            fileWriter.write(jsonArray.toJSONString());
            return true; // Registrazione avvenuta con successo
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Errore durante la registrazione
        }
    }

    private static JSONArray leggiCredenziali() {
        JSONParser parser = new JSONParser();

        try {
            // Leggi il file JSON delle credenziali degli amministratori
            Object obj = parser.parse(new FileReader(CREDENTIALS_FILE_PATH));
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}