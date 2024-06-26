package spacca.spacca;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void gioca(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void esci(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void toMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loginPlayer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login_utente-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loginAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login_admin-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ChoiceBox<String> sfidante1;

    @FXML
    private ChoiceBox<String> sfidante2;

    @FXML
    public void initialize() {
        if (sfidante1 == null || sfidante2 == null) {
            System.err.println("Le ChoiceBox non sono state iniettate correttamente!");
        } else {
            // Popola le ChoiceBox con gli usernames presenti nel file admin_credentials.json
            popolaChoiceBox();
        }
        if (listaUtenti == null) {
            System.err.println("Le ChoiceBox non sono state iniettate correttamente!");
        } else {
            // Popola le ChoiceBox con gli usernames presenti nel file admin_credentials.json
            popolaChoiceBoxCambiaNome();
        }
        if (sfidanteTorneo1 == null||sfidanteTorneo2 == null||sfidanteTorneo3 == null||sfidanteTorneo4 == null||sfidanteTorneo5 == null||sfidanteTorneo6 == null||sfidanteTorneo7 == null||sfidanteTorneo8 == null) {
            System.err.println("Le ChoiceBox non sono state iniettate correttamente!");
        } else {
            // Popola le ChoiceBox con gli usernames presenti nel file admin_credentials.json
            popolaChoiceBoxTornei();
        }
    }

    @FXML
    private TextField nuovoNomeUtente;

    @FXML
    private void gestisciUtenti(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("gestisci_utenti-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void cambiaNomeUtente() {
        String nomeUtente = sfidante1.getValue();
        String nomeUtenteNuovo = nuovoNomeUtente.getText();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/resources/spacca/spacca/admin_credentials.json"));

            JSONArray userList = (JSONArray) obj;

            // Estrai gli username dalla lista degli utenti nel file JSON
            for (Object user : userList) {
                JSONObject userObject = (JSONObject) user;
                String username = (String) userObject.get("username");
                if (username.equals(nomeUtente)) {
                    userObject.put("username", nomeUtenteNuovo);
                    System.out.println(nomeUtente + " è cambiato in: " + nomeUtenteNuovo);
                }
            }
            try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/admin_credentials.json")) {
                fileWriter.write(userList.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void gestisciPartite(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gestisci_partite-view.fxml"));
        Parent root = loader.load();
        Controller gestisciPartiteController = loader.getController();
        gestisciPartiteController.setUsernameUtenteLoggato(usernameUtenteLoggato);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void gestisciTornei(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("gestisci_tornei-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void popolaChoiceBox() {
        // Carica gli username degli utenti presenti in admin_credentials.json
        List<String> usernames = caricaUsernameDaFile();

        // Verifica che gli username siano stati caricati correttamente
        if (usernames != null && !usernames.isEmpty()) {
            // Popola le ChoiceBox con gli username degli utenti
            sfidante1.getItems().addAll(usernames);
            sfidante2.getItems().addAll(usernames);
        } else {
            System.out.println("Nessun utente trovato");
        }
    }

    @FXML
    public void popolaChoiceBoxTornei() {
        // Carica gli username degli utenti presenti in admin_credentials.json
        List<String> usernames = caricaUsernameDaFile();

        // Verifica che gli username siano stati caricati correttamente
        if (usernames != null && !usernames.isEmpty()) {
            // Popola le ChoiceBox con gli username degli utenti
            sfidanteTorneo1.getItems().addAll(usernames);
            sfidanteTorneo2.getItems().addAll(usernames);
            sfidanteTorneo3.getItems().addAll(usernames);
            sfidanteTorneo4.getItems().addAll(usernames);
            sfidanteTorneo5.getItems().addAll(usernames);
            sfidanteTorneo6.getItems().addAll(usernames);
            sfidanteTorneo7.getItems().addAll(usernames);
            sfidanteTorneo8.getItems().addAll(usernames);
        } else {
            System.out.println("Nessun utente trovato");
        }
    }

    @FXML
    private ChoiceBox<String> listaUtenti;


    @FXML
    public void popolaChoiceBoxCambiaNome() {
        // Carica gli username degli utenti presenti in admin_credentials.json
        List<String> usernames = caricaUsernameDaFile();

        // Verifica che gli username siano stati caricati correttamente
        if (usernames != null && !usernames.isEmpty()) {
            // Popola le ChoiceBox con gli username degli utenti
            listaUtenti.getItems().addAll(usernames);
        } else {
            System.out.println("Nessun utente trovato");
        }
    }

    private List<String> caricaUsernameDaFile() {
        List<String> usernames = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/resources/spacca/spacca/admin_credentials.json"));

            JSONArray userList = (JSONArray) obj;

            // Estrai gli username dalla lista degli utenti nel file JSON
            for (Object user : userList) {
                JSONObject userObject = (JSONObject) user;
                String username = (String) userObject.get("username");
                usernames.add(username);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label loginMessageLabel;

    private String usernameUtenteLoggato;


    public void setUsernameUtenteLoggato(String username) {
        this.usernameUtenteLoggato = username;
    }

    @FXML
    private void accediAdmin(ActionEvent event) throws IOException {
        // Recupera le credenziali inserite dall'utente
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Verifica le credenziali utilizzando la classe LoginManager
        boolean accessoConsentito = LoginManager.verificaCredenziali(username, password);

        if (accessoConsentito) {
            System.out.println("Accesso effettuato come utente admin " + username);
            usernameUtenteLoggato = username;
            // Carica la pagina crea_lobby_admin-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("scelta_admin-view.fxml"));
            Parent root = loader.load();

            // Ottieni il controller della nuova vista
            Controller accessoUtenteController = loader.getController();
            // Imposta il nome utente nel nuovo controller
            accessoUtenteController.setUsernameUtenteLoggato(username);
            //bentornatoAdmin.setText(username);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            loginMessageLabel.setText("Nome utente o password invalidi, riprovare.");
            loginMessageLabel.setTextFill(Color.RED);
            System.out.println("Errore di autenticazione");
        }
    }

    @FXML
    private void accediUtente(ActionEvent event) throws IOException {
        // Recupera le credenziali inserite dall'utente
        String username = usernameField.getText();

        // Verifica le credenziali utilizzando la classe LoginManager
        boolean accessoConsentito = LoginManager.verificaUtente(username);

        if (accessoConsentito) {
            setUsernameUtenteLoggato(username);
            System.out.println("Accesso effettuato come utente " + username);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("accesso_lobby_utente-view.fxml"));
            Parent root = loader.load();

            // Ottieni il controller della nuova vista
            Controller accessoUtenteController = loader.getController();
            // Imposta il nome utente nel nuovo controller
            accessoUtenteController.setUsernameUtenteLoggato(username);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            loginMessageLabel.setText("Il nome utente inserito non è valido, riprova!");
            loginMessageLabel.setTextFill(Color.RED);
            System.out.println("Errore di autenticazione");
        }
    }

    @FXML
    private TextField usernameRegistrationField;

    @FXML
    private TextField passwordRegistrationField;

    @FXML
    private Label registrationMessageLabel1;

    @FXML
    private void registrati(ActionEvent event) throws IOException {
        // Recupera i dati inseriti dall'utente
        String username = usernameRegistrationField.getText();
        String password = passwordRegistrationField != null ? passwordRegistrationField.getText() : null;
        // Registra l'utente utilizzando la classe LoginManager
        boolean registrazioneAvvenuta = LoginManager.registraUtente(username, password);

        if (registrazioneAvvenuta) {
            registrationMessageLabel1.setText("Registrazione avvenuta con successo.");
            registrationMessageLabel1.setTextFill(Color.GREEN);
        } else {
            registrationMessageLabel1.setText("Si è verificato un errore durante la registrazione.");
            registrationMessageLabel1.setTextFill(Color.RED);
        }
    }

    @FXML
    private ChoiceBox<String> sfidanteTorneo1;
    @FXML
    private ChoiceBox<String> sfidanteTorneo2;
    @FXML
    private ChoiceBox<String> sfidanteTorneo3;
    @FXML
    private ChoiceBox<String> sfidanteTorneo4;
    @FXML
    private ChoiceBox<String> sfidanteTorneo5;
    @FXML
    private ChoiceBox<String> sfidanteTorneo6;
    @FXML
    private ChoiceBox<String> sfidanteTorneo7;
    @FXML
    private ChoiceBox<String> sfidanteTorneo8;

    @FXML
    private void creaPartita(ActionEvent event) throws IOException {
        // Creazione di un'istanza di Partita
        Partita partita = new Partita();

        // Acquisizione degli sfidanti selezionati dalle ChoiceBox
        String sfidanteScelto1 = sfidante1.getValue();
        String sfidanteScelto2 = sfidante2.getValue();

        // Aggiunta degli sfidanti all'istanza di Partita
        if (sfidanteScelto1 != null && sfidanteScelto2 != null) {
            partita.aggiungiSfidante(sfidanteScelto1);
            partita.aggiungiSfidante(sfidanteScelto2);
        }

        // Salvataggio della partita
        partita.salvaPartita();
        partita.salvaPartita();
    }

    @FXML
    private void creaTorneo(ActionEvent event) throws IOException {
        // Creazione di un'istanza di Partita
        Torneo t = new Torneo();
        ArrayList<Partita> partiteTorneo = t.getPartiteTorneo();
        for (int i = 0; i < 4; i++) {
            Partita p = partiteTorneo.get(i);
            switch (i) {
                case 0:
                    p.aggiungiSfidante(sfidanteTorneo1.getValue());
                    p.aggiungiSfidante(sfidanteTorneo2.getValue());
                    p.salvaPartita();
                    break;
                case 1:
                    p.aggiungiSfidante(sfidanteTorneo3.getValue());
                    p.aggiungiSfidante(sfidanteTorneo4.getValue());
                    p.salvaPartita();
                    break;
                case 2:
                    p.aggiungiSfidante(sfidanteTorneo5.getValue());
                    p.aggiungiSfidante(sfidanteTorneo6.getValue());
                    p.salvaPartita();
                    break;
                case 3:
                    p.aggiungiSfidante(sfidanteTorneo7.getValue());
                    p.aggiungiSfidante(sfidanteTorneo8.getValue());
                    p.salvaPartita();
                    break;
            }
        }

        for (int i = 4; i < partiteTorneo.size(); i++) {
            Partita p = partiteTorneo.get(i);
            p.salvaPartita();
        }

        t.salvaTorneo();
    }

    @FXML
    private void eliminaPartitaDaAdmin() {
        String codiceElimina = codicePartitaDaEliminare.getText();
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));
            JSONObject partitaCorrente = null;

            for (Object obj : partite) {
                JSONObject partita = (JSONObject) obj;
                String codice = (String) partita.get("codice");
                if (codice != null && codice.equals(codiceElimina)) {
                    partitaCorrente = partita;
                    break;
                }
            }
            partite.remove(partitaCorrente);

            // Scrivi le modifiche nel file JSON
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(partite.toJSONString());
                System.out.println("Partita rimossa con successo");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField codicePartita;

    @FXML
    private TextField codicePartitaDaEliminare;

    @FXML
    private Label accessoLabel;

    @FXML
    private Label g1;

    @FXML
    private Label g2;

    private String codicePartitaInserito;

    @FXML
    private FlowPane flowPaneViteG1;

    @FXML
    private FlowPane flowPaneViteG2;

    @FXML
    private FlowPane spawnCarte;

    @FXML
    private Button gCAutoG1;
    @FXML
    private Button gCNemG1;
    @FXML
    private Button gCAutoG2;
    @FXML
    private Button gCNemG2;

    @FXML
    private FlowPane mazzoG1;

    @FXML
    private FlowPane mazzoG2;

    @FXML
    private void entraInGioco(ActionEvent event) throws IOException {
        // Recupera il codice della partita inserito dall'utente
        String codicePartitaInserito = codicePartita.getText();
        // Controlla se il codice della partita esiste nel file JSON
        boolean partitaEsistente = controlloCodicePartita(codicePartitaInserito);

        if (partitaEsistente) {
            // Aggiungi lo username dell'utente alla lista degli sfidanti della partita
            String risultatoAggiunta = aggiungiSfidantePartita(codicePartitaInserito, usernameUtenteLoggato);
            if (risultatoAggiunta.equals("successo")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("campo_di_gioco-view.fxml"));
                Parent root = loader.load();

                // Inizializza i controlli nella vista campo_di_gioco-view.fxml
                Controller campoDiGiocoController = loader.getController();
                campoDiGiocoController.setCodicePartita(codicePartitaInserito);
                campoDiGiocoController.caricaNomiGiocatori();
                campoDiGiocoController.visualizzaNumeroVite(codicePartitaInserito);
                campoDiGiocoController.visualizzaCarteAlCentro();
                campoDiGiocoController.visualizzaBottoni(codicePartitaInserito);
                //campoDiGiocoController.visualizzaCarteAlCentroNascoste();
                campoDiGiocoController.visualizzaMani();

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                accessoLabel.setText(risultatoAggiunta);
                accessoLabel.setTextFill(Color.RED);
            }
        } else {
            accessoLabel.setText("Impossibile accedere alla partita!");
            accessoLabel.setTextFill(Color.RED);
            System.out.println("Errore: Partita non trovata.");
        }
    }

    @FXML
    private void aggiungiVitaG1() {
        System.out.println(codicePartitaInserito);
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));
            JSONObject partitaCorrente = null;

            for (Object obj : partite) {
                JSONObject partita = (JSONObject) obj;
                String codice = (String) partita.get("codice");
                if (codice != null && codice.equals(codicePartitaInserito)) {
                    partitaCorrente = partita;
                    break;
                }
            }

            if (partitaCorrente != null) {
                // Incrementa il valore delle vite del giocatore 1
                int viteGiocatore1 = ((Long) partitaCorrente.get("viteGiocatore1")).intValue();
                if (viteGiocatore1 < 8) {
                    viteGiocatore1++;
                    partitaCorrente.put("viteGiocatore1", viteGiocatore1);

                    // Scrivi le modifiche nel file JSON
                    try (FileWriter fileWriter = new FileWriter(filePath)) {
                        fileWriter.write(partite.toJSONString());
                        System.out.println("Vita aggiunta al giocatore 1 con successo");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Aggiungi un'immagine del cuore della view
                    aggiungiImmagineCuore1();
                }

            } else {
                System.out.println("Partita non trovata.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label vincitoreLabel;

    public void setVincitore(String vincitore) {
        vincitoreLabel.setText(vincitore);
    }

    private void rimuoviVitaG1(JSONArray partite, JSONObject partitaCorrente, ActionEvent event) {
        try {
            int viteGiocatore1 = ((Long) partitaCorrente.get("viteGiocatore1")).intValue();
            if (viteGiocatore1 > 0) {
                viteGiocatore1--;
                partitaCorrente.put("viteGiocatore1", viteGiocatore1);

                // Cerca l'indice della partita corrente nell'array di partite
                int index = partite.indexOf(partitaCorrente);
                if (index != -1) {
                    // Aggiorna la partita corrente nell'array di partite
                    partite.set(index, partitaCorrente);
                } else {
                    System.out.println("Errore: Partita non trovata nell'array.");
                }

                // Rimuovi un'immagine del cuore della view
                rimuoviImmagineCuore1();
            }
            if (viteGiocatore1 == 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("vittoria_partita-view.fxml"));
                Parent root = loader.load();
                Controller vittoriaController = loader.getController();
                JSONArray sfidantiPartita = getArraySfidantiFromPartita(codicePartitaInserito);
                // Il vincitore è il giocatore 2
                String vincitore = ((String) sfidantiPartita.get(1));
                vittoriaController.setVincitore(vincitore);
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void aggiungiVitaG2() {
        System.out.println(codicePartitaInserito);
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));
            JSONObject partitaCorrente = null;

            for (Object obj : partite) {
                JSONObject partita = (JSONObject) obj;
                String codice = (String) partita.get("codice");
                if (codice != null && codice.equals(codicePartitaInserito)) {
                    partitaCorrente = partita;
                    break;
                }
            }

            if (partitaCorrente != null) {
                // Incrementa il valore delle vite del giocatore 1
                int viteGiocatore2 = ((Long) partitaCorrente.get("viteGiocatore2")).intValue();
                if (viteGiocatore2 < 8) {
                    viteGiocatore2++;
                    partitaCorrente.put("viteGiocatore2", viteGiocatore2);

                    // Scrivi le modifiche nel file JSON
                    try (FileWriter fileWriter = new FileWriter(filePath)) {
                        fileWriter.write(partite.toJSONString());
                        System.out.println("Vita aggiunta al giocatore 2 con successo");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Rimuovi un'immagine del cuore della view
                    aggiungiImmagineCuore2();
                }

            } else {
                System.out.println("Partita non trovata.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void rimuoviVitaG2(JSONArray partite, JSONObject partitaCorrente, ActionEvent event) {
        try {
            int viteGiocatore2 = ((Long) partitaCorrente.get("viteGiocatore2")).intValue();
            if (viteGiocatore2 > 0) {
                viteGiocatore2--;
                partitaCorrente.put("viteGiocatore2", viteGiocatore2);

                // Cerca l'indice della partita corrente nell'array di partite
                int index = partite.indexOf(partitaCorrente);
                if (index != -1) {
                    // Aggiorna la partita corrente nell'array di partite
                    partite.set(index, partitaCorrente);
                } else {
                    System.out.println("Errore: Partita non trovata nell'array.");
                }

                // Rimuovi un'immagine del cuore della view
                rimuoviImmagineCuore2();
            }
            if (viteGiocatore2 == 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("vittoria_partita-view.fxml"));
                Parent root = loader.load();
                Controller vittoriaController = loader.getController();
                JSONArray sfidantiPartita = getArraySfidantiFromPartita(codicePartitaInserito);
                // Il vincitore è il giocatore 1
                String vincitore = ((String) sfidantiPartita.get(0));
                vittoriaController.setVincitore(vincitore);
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void rimuoviImmagineCuore1() {
        // Rimuovi l'ultima ImageView aggiunta da FlowPaneViteG1
        ObservableList<Node> childrenG1 = flowPaneViteG1.getChildren();
        if (!childrenG1.isEmpty()) {
            childrenG1.remove(childrenG1.size() - 1);
        }
    }

    private void rimuoviImmagineCuore2() {
        // Rimuovi l'ultima ImageView aggiunta da FlowPaneViteG1
        ObservableList<Node> childrenG2 = flowPaneViteG2.getChildren();
        if (!childrenG2.isEmpty()) {
            childrenG2.remove(childrenG2.size() - 1);
        }
    }

    private void aggiungiImmagineCuore1() {
        // Dimensioni desiderate per le immagini
        double width = 20; // Larghezza desiderata
        double height = 20; // Altezza desiderata

        ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/vita.png").toExternalForm());
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        flowPaneViteG1.getChildren().add(imageView);
    }

    private void aggiungiImmagineCuore2() {
        // Dimensioni desiderate per le immagini
        double width = 20; // Larghezza desiderata
        double height = 20; // Altezza desiderata

        ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/vita.png").toExternalForm());
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        flowPaneViteG2.getChildren().add(imageView);
    }

    private boolean controlloCodicePartita(String codicePartita) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON delle partite come un array JSON
            JSONArray partite = (JSONArray) parser.parse(reader);

            // Itera attraverso le partite nel file JSON
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                // Controlla se il codice della partita fornito corrisponde a quello nel JSON
                String codicePartitaJSON = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaJSON)) {
                    return true;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return false; // In caso di eccezione o se il codice non corrisponde, restituisce false
    }

    private String aggiungiSfidantePartita(String codicePartita, String nuovoSfidante) {
        // Leggi il file JSON delle partite
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON come un array
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Itera su ciascun oggetto JSONObject nell'array
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                // Ottieni il codice della partita corrente
                String codicePartitaJSON = (String) jsonObject.get("codice");

                // Se il codice della partita corrente corrisponde a quello inserito
                if (codicePartita.equals(codicePartitaJSON)) {
                    // Ottieni l'array "sfidanti" dal JSONObject corrente
                    JSONArray sfidanti = (JSONArray) jsonObject.get("sfidanti");

                    //Utente non è in lista e la lista è vuota --> utente viene aggiunto, ma rimane in attesa
                    if (!sfidanti.contains(nuovoSfidante) && sfidanti.size() == 0) {
                        sfidanti.add(nuovoSfidante);

                        // Salva le modifiche nel file JSON
                        try (FileWriter file = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                            file.write(jsonArray.toJSONString());
                            file.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Sfidante aggiunto alla partita con successo.");
                        return "Attendi che un altro sfidante acceda alla partita";
                    }
                    //Utente non è in lista ma nella lista c'è già un giocatore --> utente viene aggiunto, partita avviata
                    if (!sfidanti.contains(nuovoSfidante) && sfidanti.size() < 2) {
                        sfidanti.add(nuovoSfidante);

                        // Salva le modifiche nel file JSON
                        try (FileWriter file = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                            file.write(jsonArray.toJSONString());
                            file.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Sfidante aggiunto alla partita con successo.");
                        return "successo";
                    }
                    //Utente è in lista e la lista ha solo lui --> utente non viene aggiunto, rimane in attesa
                    if (sfidanti.contains(nuovoSfidante) && sfidanti.size() == 1) {
                        return "Attendi che un altro sfidante acceda alla partita";
                    }
                    //Utente è in lista ma la lista è piena --> utente non viene aggiunto, partita avviata
                    if (sfidanti.contains(nuovoSfidante) && sfidanti.size() == 2) {
                        return "successo";
                    }
                }
            }
            // Se nessuna partita con il codice corrispondente è stata trovata
            return "Impossibile accedere alla partita.";
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return "Errore durante l'aggiunta dello sfidante.";
        }
    }

    public void setCodicePartita(String codice) {
        this.codicePartitaInserito = codice;
    }

    public void caricaNomiGiocatori() {
        // Carica i nomi dei giocatori nella vista campo_di_gioco-view.fxml
        // Recupera l'array degli sfidanti dalla partita usando il codicePartita
        JSONArray sfidantiPartita = getArraySfidantiFromPartita(codicePartitaInserito);

        // Estrai i nomi dei giocatori dall'array e impostali nelle label g1 e g2
        if (sfidantiPartita != null) {
            if (sfidantiPartita.size() >= 1) {
                g1.setText((String) sfidantiPartita.get(0));
            }
            if (sfidantiPartita.size() >= 2) {
                g2.setText((String) sfidantiPartita.get(1));
            }
        }
    }

    private JSONArray getArraySfidantiFromPartita(String codicePartita) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON delle partite come un array JSON
            JSONArray partite = (JSONArray) parser.parse(reader);

            // Itera attraverso le partite nel file JSON
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                // Controlla se il codice della partita fornito corrisponde a quello nel JSON
                String codicePartitaJSON = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaJSON)) {
                    // Restituisci l'array degli sfidanti
                    return (JSONArray) partita.get("sfidanti");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; // In caso di eccezione o se il codice non corrisponde, restituisce null
    }


    public void visualizzaNumeroVite(String codicePartita) {
        // Ottieni il numero delle vite dei giocatori dal file JSON
        int viteGiocatore1 = getNumeroViteGiocatore("viteGiocatore1", codicePartita);
        int viteGiocatore2 = getNumeroViteGiocatore("viteGiocatore2", codicePartita);
        System.out.println(viteGiocatore1);
        System.out.println(viteGiocatore2);

        // Visualizza il numero delle vite nella view
        visualizzaNumeroVite(viteGiocatore1, viteGiocatore2);
    }


    private int getNumeroViteGiocatore(String nomeChiave, String codicePartita) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            JSONArray partite = (JSONArray) parser.parse(reader);
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                String codice = partita.get("codice").toString();
                if (codice.equals(codicePartita) && partita.containsKey(nomeChiave)) {
                    System.out.println(((Long) partita.get(nomeChiave)).intValue());
                    return ((Long) partita.get(nomeChiave)).intValue();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return 0; // Se non viene trovato il numero di vite nel JSON, restituisci 0
    }


    public void visualizzaNumeroVite(int viteGiocatore1, int viteGiocatore2) {
        // Cancella eventuali ImageView preesistenti nei FlowPane
        flowPaneViteG1.getChildren().clear();
        flowPaneViteG2.getChildren().clear();

        // Dimensioni desiderate per le immagini
        double width = 20; // Larghezza desiderata
        double height = 20; // Altezza desiderata

        // Aggiungi le ImageView corrispondenti al numero di vite dei giocatori nel FlowPane appropriato
        for (int i = 0; i < viteGiocatore1; i++) {
            ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/vita.png").toExternalForm());
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            flowPaneViteG1.getChildren().add(imageView);
        }

        for (int i = 0; i < viteGiocatore2; i++) {
            ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/vita.png").toExternalForm());
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            flowPaneViteG2.getChildren().add(imageView);
        }
    }

    public void visualizzaCarteAlCentro() {
        JSONParser parser = new JSONParser();

        try {
            JSONArray booleanList = (JSONArray) parser.parse(getArrayCarteAlCentroFromPartita(codicePartitaInserito).toString());

            Collections.shuffle(booleanList);

            spawnCarte.getChildren().clear();
            for (int i = 0; i < booleanList.size(); i++) {
                boolean isTrue = (boolean) booleanList.get(i);

                // Dimensioni desiderate per le immagini
                double width = 48; // Larghezza desiderata
                double height = 69; // Altezza desiderata

                String imageUrl = isTrue ? "/spacca/spacca/images/dorso.png" : "/spacca/spacca/images/dorso2.png";

                ImageView imageView = new ImageView(getClass().getResource(imageUrl).toExternalForm());
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                spawnCarte.getChildren().add(imageView);
            }

            // Aggiungi un ritardo di 10 secondi dopo l'esecuzione del for
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                // Codice da eseguire dopo il ritardo di 10 secondi

                spawnCarte.getChildren().clear();

                for (int i = 0; i < booleanList.size(); i++) {
                    // Dimensioni desiderate per le immagini
                    double width = 48; // Larghezza desiderata
                    double height = 69; // Altezza desiderata

                    ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/Retro.png").toExternalForm());
                    imageView.setFitWidth(width);
                    imageView.setFitHeight(height);
                    spawnCarte.getChildren().add(imageView);
                }

                //Collections.shuffle(booleanList);

                // Stampa dell'ArrayList di booleani
                System.out.println("ArrayList di booleani mischiato:");
                for (int i = 0; i < booleanList.size(); i++) {
                    System.out.println("Elemento " + i + ": " + booleanList.get(i));
                }

            }));

            timeline.play();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void visualizzaCarteAlCentroNascoste(JSONArray booleanList) {

        spawnCarte.getChildren().clear();

        for (int i = 0; i < booleanList.size(); i++) {
            // Dimensioni desiderate per le immagini
            double width = 48; // Larghezza desiderata
            double height = 69; // Altezza desiderata

            ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/Retro.png").toExternalForm());
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            spawnCarte.getChildren().add(imageView);
        }

        // Stampa dell'ArrayList di booleani
        System.out.println("ArrayList di booleani:");
        for (int i = 0; i < booleanList.size(); i++) {
            System.out.println("Elemento " + i + ": " + booleanList.get(i));
        }

    }

    public void visualizzaCarteAlCentroNonDaJson(JSONArray booleanList) {

        Collections.shuffle(booleanList);

        spawnCarte.getChildren().clear();
        for (int i = 0; i < booleanList.size(); i++) {
            boolean isTrue = (boolean) booleanList.get(i);

            // Dimensioni desiderate per le immagini
            double width = 48; // Larghezza desiderata
            double height = 69; // Altezza desiderata

            String imageUrl = isTrue ? "/spacca/spacca/images/dorso.png" : "/spacca/spacca/images/dorso2.png";

            ImageView imageView = new ImageView(getClass().getResource(imageUrl).toExternalForm());
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            spawnCarte.getChildren().add(imageView);
        }

        // Aggiungi un ritardo di 10 secondi dopo l'esecuzione del for
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            // Codice da eseguire dopo il ritardo di 10 secondi

            spawnCarte.getChildren().clear();

            for (int i = 0; i < booleanList.size(); i++) {
                // Dimensioni desiderate per le immagini
                double width = 48; // Larghezza desiderata
                double height = 69; // Altezza desiderata

                ImageView imageView = new ImageView(getClass().getResource("/spacca/spacca/images/Retro.png").toExternalForm());
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                spawnCarte.getChildren().add(imageView);
            }

            // Stampa dell'ArrayList di booleani
            System.out.println("ArrayList di booleani mischiato:");
            for (int i = 0; i < booleanList.size(); i++) {
                System.out.println("Elemento " + i + ": " + booleanList.get(i));
            }

        }));

        timeline.play();

    }

    @FXML
    private void visualizzaBottoni(String codicePartitaInserito) {
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il contenuto attuale del file JSON
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));

            // Cerca la partita corrispondente al codice inserito
            JSONObject partitaCorrispondente = null;
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                String codicePartita = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaInserito)) {
                    partitaCorrispondente = partita;
                    break;
                }
            }

            if (partitaCorrispondente != null) {
                boolean turnoG1 = (boolean) partitaCorrispondente.get("turnoG1");

                // Visualizza i bottoni in base al turno corrente
                if (turnoG1) {
                    gCAutoG1.setVisible(true);
                    gCNemG1.setVisible(true);
                    gCAutoG2.setVisible(false);
                    gCNemG2.setVisible(false);
                } else {
                    gCAutoG1.setVisible(false);
                    gCNemG1.setVisible(false);
                    gCAutoG2.setVisible(true);
                    gCNemG2.setVisible(true);
                }
            } else {
                // Gestisci il caso in cui non viene trovata nessuna partita corrispondente al codice inserito
                System.out.println("Partita non trovata.");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void usaCartaSulNemicoG1(ActionEvent event) throws IOException {
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il contenuto attuale del file JSON
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));

            // Cerca la partita corrispondente al codice inserito
            JSONObject partitaCorrispondente = null;
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                String codicePartita = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaInserito)) {
                    partitaCorrispondente = partita;
                    break;
                }
            }

            if (partitaCorrispondente != null) {
                // Estrai l'array "carteAlCentro" dalla partita corrispondente
                JSONArray booleanList = (JSONArray) partitaCorrispondente.get("carteAlCentro");

                // Verifica se il primo valore di booleanList è true
                if (!booleanList.isEmpty() && (boolean) booleanList.get(0)) {
                    // Se il primo valore è true, richiama il metodo rimuoviVitaG2
                    rimuoviVitaG2(partite, partitaCorrispondente, event);
                }

                // Rimuovi il primo elemento da booleanList
                if (!booleanList.isEmpty()) {
                    booleanList.remove(0);
                    visualizzaCarteAlCentroNascoste(booleanList);
                }

                if (booleanList.isEmpty()) {
                    booleanList.addAll(new Partita().CarteAlCentro());
                    visualizzaCarteAlCentroNonDaJson(booleanList);
                }


                boolean turnoG1 = false;
                partitaCorrispondente.put("turnoG1", turnoG1);


                // Scrivi le modifiche nel file JSON
                try (FileWriter file = new FileWriter(filePath)) {
                    file.write(partite.toJSONString());
                }
                visualizzaBottoni(codicePartitaInserito);
                visualizzaMani();

            } else {
                // Gestisci il caso in cui non viene trovata nessuna partita corrispondente al codice inserito
                System.out.println("Partita non trovata.");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void usaCartaSuDiTeG1(ActionEvent event) throws IOException {
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il contenuto attuale del file JSON
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));

            // Cerca la partita corrispondente al codice inserito
            JSONObject partitaCorrispondente = null;
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                String codicePartita = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaInserito)) {
                    partitaCorrispondente = partita;
                    break;
                }
            }

            if (partitaCorrispondente != null) {
                // Estrai l'array "carteAlCentro" dalla partita corrispondente
                JSONArray booleanList = (JSONArray) partitaCorrispondente.get("carteAlCentro");

                // Verifica se il primo valore di booleanList è true
                if (!booleanList.isEmpty() && (boolean) booleanList.get(0)) {
                    // Se il primo valore è true, richiama il metodo rimuoviVitaG2
                    rimuoviVitaG1(partite, partitaCorrispondente, event);
                    boolean turnoG1 = false;
                    partitaCorrispondente.put("turnoG1", turnoG1);
                }

                // Rimuovi il primo elemento da booleanList
                if (!booleanList.isEmpty()) {
                    booleanList.remove(0);
                    visualizzaCarteAlCentroNascoste(booleanList);
                }

                if (booleanList.isEmpty()) {
                    booleanList.addAll(new Partita().CarteAlCentro());
                    visualizzaCarteAlCentroNonDaJson(booleanList);
                }

                // Scrivi le modifiche nel file JSON
                try (FileWriter file = new FileWriter(filePath)) {
                    file.write(partite.toJSONString());
                }
                visualizzaBottoni(codicePartitaInserito);
                visualizzaMani();

            } else {
                // Gestisci il caso in cui non viene trovata nessuna partita corrispondente al codice inserito
                System.out.println("Partita non trovata.");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void usaCartaSulNemicoG2(ActionEvent event) throws IOException {
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il contenuto attuale del file JSON
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));

            // Cerca la partita corrispondente al codice inserito
            JSONObject partitaCorrispondente = null;
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                String codicePartita = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaInserito)) {
                    partitaCorrispondente = partita;
                    break;
                }
            }

            if (partitaCorrispondente != null) {
                // Estrai l'array "carteAlCentro" dalla partita corrispondente
                JSONArray booleanList = (JSONArray) partitaCorrispondente.get("carteAlCentro");

                // Verifica se il primo valore di booleanList è true
                if (!booleanList.isEmpty() && (boolean) booleanList.get(0)) {
                    // Se il primo valore è true, richiama il metodo rimuoviVitaG2
                    rimuoviVitaG1(partite, partitaCorrispondente, event);
                }

                // Rimuovi il primo elemento da booleanList
                if (!booleanList.isEmpty()) {
                    booleanList.remove(0);
                    visualizzaCarteAlCentroNascoste(booleanList);
                }

                if (booleanList.isEmpty()) {
                    booleanList.addAll(new Partita().CarteAlCentro());
                    visualizzaCarteAlCentroNonDaJson(booleanList);
                }


                boolean turnoG1 = true;
                partitaCorrispondente.put("turnoG1", turnoG1);


                // Scrivi le modifiche nel file JSON
                try (FileWriter file = new FileWriter(filePath)) {
                    file.write(partite.toJSONString());
                }
                visualizzaBottoni(codicePartitaInserito);
                visualizzaMani();

            } else {
                // Gestisci il caso in cui non viene trovata nessuna partita corrispondente al codice inserito
                System.out.println("Partita non trovata.");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void usaCartaSuDiTeG2(ActionEvent event) throws IOException {
        String filePath = "src/main/resources/spacca/spacca/partite.json";

        try {
            // Leggi il contenuto attuale del file JSON
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader(filePath));

            // Cerca la partita corrispondente al codice inserito
            JSONObject partitaCorrispondente = null;
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                String codicePartita = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaInserito)) {
                    partitaCorrispondente = partita;
                    break;
                }
            }

            if (partitaCorrispondente != null) {
                // Estrai l'array "carteAlCentro" dalla partita corrispondente
                JSONArray booleanList = (JSONArray) partitaCorrispondente.get("carteAlCentro");

                // Verifica se il primo valore di booleanList è true
                if (!booleanList.isEmpty() && (boolean) booleanList.get(0)) {
                    // Se il primo valore è true, richiama il metodo rimuoviVitaG2
                    rimuoviVitaG2(partite, partitaCorrispondente, event);
                    boolean turnoG1 = true;
                    partitaCorrispondente.put("turnoG1", turnoG1);
                }

                // Rimuovi il primo elemento da booleanList
                if (!booleanList.isEmpty()) {
                    booleanList.remove(0);
                    visualizzaCarteAlCentroNascoste(booleanList);
                }

                if (booleanList.isEmpty()) {
                    booleanList.addAll(new Partita().CarteAlCentro());
                    visualizzaCarteAlCentroNonDaJson(booleanList);
                }

                // Scrivi le modifiche nel file JSON
                try (FileWriter file = new FileWriter(filePath)) {
                    file.write(partite.toJSONString());
                }
                visualizzaBottoni(codicePartitaInserito);
                visualizzaMani();

            } else {
                // Gestisci il caso in cui non viene trovata nessuna partita corrispondente al codice inserito
                System.out.println("Partita non trovata.");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    private JSONArray getArrayCarteAlCentroFromPartita(String codicePartita) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON delle partite come un array JSON
            JSONArray partite = (JSONArray) parser.parse(reader);

            // Itera attraverso le partite nel file JSON
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                // Controlla se il codice della partita fornito corrisponde a quello nel JSON
                String codicePartitaJSON = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaJSON)) {
                    // Restituisci l'array di carteAlCentro
                    return (JSONArray) partita.get("carteAlCentro");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; // In caso di eccezione o se il codice non corrisponde, restituisce null
    }

    private JSONArray getArrayManoG1(String codicePartita) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON delle partite come un array JSON
            JSONArray partite = (JSONArray) parser.parse(reader);

            // Itera attraverso le partite nel file JSON
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                // Controlla se il codice della partita fornito corrisponde a quello nel JSON
                String codicePartitaJSON = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaJSON)) {
                    // Restituisci l'array di carteAlCentro
                    return (JSONArray) partita.get("manoG1");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; // In caso di eccezione o se il codice non corrisponde, restituisce null
    }

    private JSONArray getArrayManoG2(String codicePartita) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON delle partite come un array JSON
            JSONArray partite = (JSONArray) parser.parse(reader);

            // Itera attraverso le partite nel file JSON
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                // Controlla se il codice della partita fornito corrisponde a quello nel JSON
                String codicePartitaJSON = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaJSON)) {
                    // Restituisci l'array di carteAlCentro
                    return (JSONArray) partita.get("manoG2");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; // In caso di eccezione o se il codice non corrisponde, restituisce null
    }

    private boolean getTurno(String codicePartita) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            // Parsa il file JSON delle partite come un array JSON
            JSONArray partite = (JSONArray) parser.parse(reader);

            // Itera attraverso le partite nel file JSON
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                // Controlla se il codice della partita fornito corrisponde a quello nel JSON
                String codicePartitaJSON = (String) partita.get("codice");
                if (codicePartita.equals(codicePartitaJSON)) {
                    // Restituisci l'array di carteAlCentro
                    return (boolean) partita.get("turnoG1");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        System.out.println("E' finita!!");
        return false; //sperando non ci vada mai
    }

    public void visualizzaMani() {
        JSONParser parser = new JSONParser();

        try {
            JSONArray manoG1 = (JSONArray) parser.parse(getArrayManoG1(codicePartitaInserito).toString());
            JSONArray manoG2 = (JSONArray) parser.parse(getArrayManoG2(codicePartitaInserito).toString());

            mazzoG1.getChildren().clear();
            mazzoG2.getChildren().clear();
            String imageUrl = "";
            for (int i = 0; i < manoG1.size(); i++) {

                String nomeCarta = manoG1.get(i).toString();

                switch (nomeCarta) {
                    case "Cura":
                        imageUrl = "/spacca/spacca/images/Cura.png";
                        break;
                    case "ScambioMano":
                        imageUrl = "/spacca/spacca/images/ScambioMano.png";
                        break;
                    case "Scarta":
                        imageUrl = "/spacca/spacca/images/Scarta.png";
                        break;
                    case "Rigenera":
                        imageUrl = "/spacca/spacca/images/Rigenera.png";
                        break;
                    case "DoppioDanno":
                        imageUrl = "/spacca/spacca/images/DoppioDanno.png";
                        break;
                    case "Sbircia":
                        imageUrl = "/spacca/spacca/images/Sbircia.png";
                        break;
                }

                // Dimensioni desiderate per le immagini
                double width = 58; // Larghezza desiderata
                double height = 79; // Altezza desiderata


                ImageView imageView = new ImageView(getClass().getResource(imageUrl).toExternalForm());
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                if (getTurno(codicePartitaInserito)) {
                    imageView.setOnMouseClicked(event -> {
                        System.out.println("Immagine cliccata!");
                        // Avvia il metodo collegato all'evento
                        giocaCarta(nomeCarta, event);
                    });
                }
                mazzoG1.getChildren().add(imageView);
            }

            for (int i = 0; i < manoG2.size(); i++) {

                String nomeCarta = manoG2.get(i).toString();

                switch (nomeCarta) {
                    case "Cura":
                        imageUrl = "/spacca/spacca/images/Cura.png";
                        break;
                    case "ScambioMano":
                        imageUrl = "/spacca/spacca/images/ScambioMano.png";
                        break;
                    case "Scarta":
                        imageUrl = "/spacca/spacca/images/Scarta.png";
                        break;
                    case "Rigenera":
                        imageUrl = "/spacca/spacca/images/Rigenera.png";
                        break;
                    case "DoppioDanno":
                        imageUrl = "/spacca/spacca/images/DoppioDanno.png";
                        break;
                    case "Sbircia":
                        imageUrl = "/spacca/spacca/images/Sbircia.png";
                        break;
                }
                // Dimensioni desiderate per le immagini
                double width = 58; // Larghezza desiderata
                double height = 79; // Altezza desiderata


                ImageView imageView = new ImageView(getClass().getResource(imageUrl).toExternalForm());
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                if (!getTurno(codicePartitaInserito)) {
                    imageView.setOnMouseClicked(event -> {
                        System.out.println("Immagine cliccata!");
                        // Avvia il metodo collegato all'evento
                        giocaCarta(nomeCarta, event);
                    });
                }
                mazzoG2.getChildren().add(imageView);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void giocaCarta(String nomeCarta, javafx.scene.input.MouseEvent event) {
        boolean turno = getTurno(codicePartitaInserito);

        //GIOCATORE 2
        if (!turno) {
            JSONArray manoG2 = getArrayManoG2(codicePartitaInserito);
            // Il counter in questo for serve per rimuovere solo un elemento che ha quel nome dall'array mano
            int counter = 0;
            for (int i = 0; i < manoG2.size(); i++) {
                if (manoG2.get(i).equals(nomeCarta) && counter == 0) {
                    manoG2.remove(i);
                    counter++;
                }
            }

            try {
                // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
                JSONParser parser = new JSONParser();
                JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
                JSONObject partitaCorrente = null;

                for (Object obj : partite) {
                    JSONObject partita = (JSONObject) obj;
                    String codice = (String) partita.get("codice");
                    if (codice != null && codice.equals(codicePartitaInserito)) {
                        partitaCorrente = partita;
                        break;
                    }
                }

                if (partitaCorrente != null) {
                    partitaCorrente.put("manoG2", manoG2);

                    try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                        fileWriter.write(partite.toJSONString());
                        System.out.println("Mano G2 aggiornata");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    switchCarte(nomeCarta, event);
                    visualizzaMani();
                } else {
                    System.out.println("Partita non trovata.");
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        } else {
            //GIOCATORE 1
            JSONArray manoG1 = getArrayManoG1(codicePartitaInserito);
            // Il counter in questo for serve per rimuovere solo un elemento che ha quel nome dall'array mano
            int counter = 0;
            for (int i = 0; i < manoG1.size(); i++) {
                if (manoG1.get(i).equals(nomeCarta) && counter == 0) {
                    manoG1.remove(i);
                    counter++;
                }
            }
            try {
                // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
                JSONParser parser = new JSONParser();
                JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
                JSONObject partitaCorrente = null;

                for (Object obj : partite) {
                    JSONObject partita = (JSONObject) obj;
                    String codice = (String) partita.get("codice");
                    if (codice != null && codice.equals(codicePartitaInserito)) {
                        partitaCorrente = partita;
                        break;
                    }
                }

                if (partitaCorrente != null) {
                    partitaCorrente.put("manoG1", manoG1);

                    try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                        fileWriter.write(partite.toJSONString());
                        System.out.println("Mano G1 aggiornata");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    switchCarte(nomeCarta, event);
                    visualizzaMani();
                } else {
                    System.out.println("Partita non trovata.");
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

    }

    private void switchCarte(String nomeCarta, javafx.scene.input.MouseEvent event) {
        switch (nomeCarta) {
            case "Cura":
                cura();
                break;
            case "ScambioMano":
                scambiaMani();
                break;
            case "Scarta":
                scartaCartaAlCentro();
                break;
            case "Rigenera":
                rigeneraCarteAlCentro();
                break;
            case "DoppioDanno":
                doppioDanno(event);
                break;
            case "Sbircia":
                sbircia();
                break;
        }
    }

    public void cura() {
        boolean turno = getTurno(codicePartitaInserito);

        if (turno) {
            aggiungiVitaG1();
        } else {
            aggiungiVitaG2();
        }
    }


    public void pescaCarta() {
        boolean turno = getTurno(codicePartitaInserito);
        String cartaPescata = Partita.generaNomeCasuale();

        //G1 pesca
        //Controllare chi pesca la carta
        //se pesca G1, allora aggiorna la mano di G1 aggiungendo una carta presa a random
        if (turno) {
            turno = false;
            JSONArray manoG1 = getArrayManoG1(codicePartitaInserito);
            //aggiorna la mano col nuovo elemento
            manoG1.add(cartaPescata);
            //
            try {
                // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
                JSONParser parser = new JSONParser();
                JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
                JSONObject partitaCorrente = null;

                for (Object obj : partite) {
                    JSONObject partita = (JSONObject) obj;
                    String codice = (String) partita.get("codice");
                    if (codice != null && codice.equals(codicePartitaInserito)) {
                        partitaCorrente = partita;
                        break;
                    }
                }

                if (partitaCorrente != null) {
                    partitaCorrente.put("manoG1", manoG1);
                    partitaCorrente.put("turnoG1", turno);

                    try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                        fileWriter.write(partite.toJSONString());
                        System.out.println("Mano G1 aggiornata");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    visualizzaMani();
                } else {
                    System.out.println("Partita non trovata.");
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            //turno G2
            turno = true;
            JSONArray manoG2 = getArrayManoG2(codicePartitaInserito);
            //aggiorna la mano col nuovo elemento
            manoG2.add(cartaPescata);
            //
            try {
                // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
                JSONParser parser = new JSONParser();
                JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
                JSONObject partitaCorrente = null;

                for (Object obj : partite) {
                    JSONObject partita = (JSONObject) obj;
                    String codice = (String) partita.get("codice");
                    if (codice != null && codice.equals(codicePartitaInserito)) {
                        partitaCorrente = partita;
                        break;
                    }
                }

                if (partitaCorrente != null) {
                    partitaCorrente.put("manoG2", manoG2);
                    partitaCorrente.put("turnoG1", turno);

                    try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                        fileWriter.write(partite.toJSONString());
                        System.out.println("Mano G2 aggiornata");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    visualizzaMani();
                } else {
                    System.out.println("Partita non trovata.");
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        visualizzaBottoni(codicePartitaInserito);
    }

    public void scambiaMani() {
        //Prendi entrambi i mazzi e assrgnali alle variabili
        JSONArray manoG1 = getArrayManoG1(codicePartitaInserito);
        JSONArray manoG2 = getArrayManoG2(codicePartitaInserito);

        try {
            // Leggi il file JSON e ottieni l'oggetto JSON della partita in corso
            JSONParser parser = new JSONParser();
            JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
            JSONObject partitaCorrente = null;

            for (Object obj : partite) {
                JSONObject partita = (JSONObject) obj;
                String codice = (String) partita.get("codice");
                if (codice != null && codice.equals(codicePartitaInserito)) {
                    partitaCorrente = partita;
                    break;
                }
            }

            if (partitaCorrente != null) {
                partitaCorrente.put("manoG1", manoG2);
                partitaCorrente.put("manoG2", manoG1);

                try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                    fileWriter.write(partite.toJSONString());
                    System.out.println("Mano G1 aggiornata");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                visualizzaMani();
            } else {
                System.out.println("Partita non trovata.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //visualizzaBottoni(codicePartitaInserito);
    }

    public void scartaCartaAlCentro() {
        try {
            //Prendi l'array di carte al centro dal JSON e assegnalo ad una variabile
            JSONParser parser = new JSONParser();
            JSONArray booleanList = (JSONArray) parser.parse(getArrayCarteAlCentroFromPartita(codicePartitaInserito).toString());
            //Aggiorna il valore togliendo il primo elemento
            booleanList.remove(0);
            //Aggiorna dati nel JSON
            JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
            JSONObject partitaCorrente = null;

            for (Object obj : partite) {
                JSONObject partita = (JSONObject) obj;
                String codice = (String) partita.get("codice");
                if (codice != null && codice.equals(codicePartitaInserito)) {
                    partitaCorrente = partita;
                    break;
                }
            }

            if (partitaCorrente != null) {
                partitaCorrente.put("carteAlCentro", booleanList);

                try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                    fileWriter.write(partite.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //visualizzaMani();
                visualizzaCarteAlCentroNascoste(booleanList);
            } else {
                System.out.println("Partita non trovata.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void rigeneraCarteAlCentro() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray booleanList = (JSONArray) parser.parse(Partita.CarteAlCentro().toString());
            //Aggiorna dati nel JSON
            JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
            JSONObject partitaCorrente = null;

            for (Object obj : partite) {
                JSONObject partita = (JSONObject) obj;
                String codice = (String) partita.get("codice");
                if (codice != null && codice.equals(codicePartitaInserito)) {
                    partitaCorrente = partita;
                    break;
                }
            }

            if (partitaCorrente != null) {
                partitaCorrente.put("carteAlCentro", booleanList);

                try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                    fileWriter.write(partite.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //visualizzaMani();
                visualizzaCarteAlCentro();
            } else {
                System.out.println("Partita non trovata.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void doppioDanno(javafx.scene.input.MouseEvent event) {
        //Prendi il JSON di carte al centro
        JSONArray booleanList = getArrayCarteAlCentroFromPartita(codicePartitaInserito);

        //Prendi dal JSON il turno
        boolean turno = getTurno(codicePartitaInserito);

        // Controlla il turno del giocatore e separa le casistiche
        if(turno){
            //se l'elemento di indice 0 è true, allora rimuovi 2 vite a G2 e setta turno G1 su false, altrimenti nulla
            if((boolean)booleanList.get(0)==true){
                try {
                    JSONParser parser = new JSONParser();
                    //Aggiorna dati nel JSON
                    JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
                    JSONObject partitaCorrente = null;

                    for (Object obj : partite) {
                        JSONObject partita = (JSONObject) obj;
                        String codice = (String) partita.get("codice");
                        if (codice != null && codice.equals(codicePartitaInserito)) {
                            partitaCorrente = partita;
                            break;
                        }
                    }

                    if (partitaCorrente != null) {
                        booleanList.remove(0);
                        partitaCorrente.put("carteAlCentro", booleanList);
                        int viteGiocatore2 = ((Long) partitaCorrente.get("viteGiocatore2")).intValue();
                        if (viteGiocatore2 > 2) {
                            viteGiocatore2 = viteGiocatore2 - 2;
                            partitaCorrente.put("viteGiocatore2", viteGiocatore2);
                            rimuoviImmagineCuore2();
                            rimuoviImmagineCuore2();
                        }
                        turno = false;
                        partitaCorrente.put("turnoG1", turno);
                        try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                            fileWriter.write(partite.toJSONString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        visualizzaMani();
                        visualizzaCarteAlCentro();
                        visualizzaBottoni(codicePartitaInserito);

                    } else {
                        System.out.println("Partita non trovata.");
                    }

                } catch(IOException | ParseException e){
                    e.printStackTrace();
                }}
        } else {
            //se l'elemento di indice 0 è true, allora rimuovi 2 vite a G1 e setta turnoG1 su true, altrimenti nulla
            if ((boolean) booleanList.get(0) == true) {
                try {
                    JSONParser parser = new JSONParser();
                    //Aggiorna dati nel JSON
                    JSONArray partite = (JSONArray) parser.parse(new FileReader("src/main/resources/spacca/spacca/partite.json"));
                    JSONObject partitaCorrente = null;

                    for (Object obj : partite) {
                        JSONObject partita = (JSONObject) obj;
                        String codice = (String) partita.get("codice");
                        if (codice != null && codice.equals(codicePartitaInserito)) {
                            partitaCorrente = partita;
                            break;
                        }
                    }

                    if (partitaCorrente != null) {
                        booleanList.remove(0);
                        partitaCorrente.put("carteAlCentro", booleanList);
                        int viteGiocatore1 = ((Long) partitaCorrente.get("viteGiocatore1")).intValue();
                        if (viteGiocatore1 > 2) {
                            viteGiocatore1 = viteGiocatore1 - 2;
                            partitaCorrente.put("viteGiocatore1", viteGiocatore1);
                            rimuoviImmagineCuore1();
                            rimuoviImmagineCuore1();
                        }
                        turno = true;
                        partitaCorrente.put("turnoG1", turno);

                        try (FileWriter fileWriter = new FileWriter("src/main/resources/spacca/spacca/partite.json")) {
                            fileWriter.write(partite.toJSONString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        visualizzaMani();
                        visualizzaCarteAlCentro();
                        visualizzaBottoni(codicePartitaInserito);

                    } else {
                        System.out.println("Partita non trovata.");
                    }

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void sbircia(){
        JSONParser parser = new JSONParser();

        try {
            JSONArray booleanList = (JSONArray) parser.parse(getArrayCarteAlCentroFromPartita(codicePartitaInserito).toString());

            spawnCarte.getChildren().clear();
            boolean primaCarta = (boolean) booleanList.get(0);

            // Dimensioni desiderate per le immagini
            double width = 48; // Larghezza desiderata
            double height = 69; // Altezza desiderata

            String imageUrl = primaCarta ? "/spacca/spacca/images/dorso.png" : "/spacca/spacca/images/dorso2.png";

            ImageView imageView = new ImageView(getClass().getResource(imageUrl).toExternalForm());
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            spawnCarte.getChildren().add(imageView);
            if (booleanList.size() > 1) {
                for (int i = 1; i < booleanList.size(); i++) {
                    imageUrl = "/spacca/spacca/images/Retro.png";

                    imageView = new ImageView(getClass().getResource(imageUrl).toExternalForm());
                    imageView.setFitWidth(width);
                    imageView.setFitHeight(height);
                    spawnCarte.getChildren().add(imageView);
                }
            }
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                // Codice da eseguire dopo il ritardo di 5 secondi

                spawnCarte.getChildren().clear();

                for (int i = 0; i < booleanList.size(); i++) {
                    ImageView image = new ImageView(getClass().getResource("/spacca/spacca/images/Retro.png").toExternalForm());
                    image.setFitWidth(width);
                    image.setFitHeight(height);
                    spawnCarte.getChildren().add(image);
                }
            }));

            timeline.play();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

