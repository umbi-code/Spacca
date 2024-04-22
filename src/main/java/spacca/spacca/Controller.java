package spacca.spacca;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void gioca(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loginPlayer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login_utente-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void loginAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login_admin-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
            System.out.println("Accesso effettuato come utente admin "+ username);
            usernameUtenteLoggato=username;
            // Carica la pagina crea_lobby_admin-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("crea_lobby_admin-view.fxml"));
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
            System.out.println("Accesso effettuato come utente "+ username);

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
    }

    @FXML
    private TextField codicePartita;

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
                campoDiGiocoController.visualizzaNumeroVite();

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
                    if (!sfidanti.contains(nuovoSfidante) && sfidanti.size() == 0){
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
                    if (!sfidanti.contains(nuovoSfidante) && sfidanti.size() < 2){
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
                    if (sfidanti.contains(nuovoSfidante) && sfidanti.size() == 1){
                        return "Attendi che un altro sfidante acceda alla partita";
                    }
                    //Utente è in lista ma la lista è piena --> utente non viene aggiunto, partita avviata
                    if (sfidanti.contains(nuovoSfidante) && sfidanti.size() == 2){
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


    public void visualizzaNumeroVite() {
        // Ottieni il numero delle vite dei giocatori dal file JSON
        int viteGiocatore1 = getNumeroViteGiocatore("viteGiocatore1");
        int viteGiocatore2 = getNumeroViteGiocatore("viteGiocatore2");
        System.out.println(viteGiocatore1);
        System.out.println(viteGiocatore2);

        // Visualizza il numero delle vite nella view
        visualizzaNumeroVite(viteGiocatore1, viteGiocatore2);
    }

    private int getNumeroViteGiocatore(String nomeChiave) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/spacca/spacca/partite.json")) {
            JSONArray partite = (JSONArray) parser.parse(reader);
            for (Object partitaObj : partite) {
                JSONObject partita = (JSONObject) partitaObj;
                if (partita.containsKey(nomeChiave)) {
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

}