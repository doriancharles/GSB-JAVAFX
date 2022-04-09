package fr.gsb.rv.dr.gsbrvdr;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.lang.System.out;


public class HelloApplication extends Application {
    private PanneauAccueil vueAccueil = new PanneauAccueil();
    private PanneauPraticiens vuePraticiens = new PanneauPraticiens();
    private PanneauRapports vueRapports = new PanneauRapports();
    private StackPane stackPane;
    public static class Visiteur {

        private String matricule;
        private String nom;
        private String prenom;

        public Visiteur(String matricule, String nom, String prenom) {
            super();
            this.matricule = matricule;
            this.nom = nom;
            this.prenom = prenom;
        }

        public Visiteur(){
            super();
        }

        public String getMatricule() {
            return matricule;
        }

        public void setMatricule(String matricule) {
            this.matricule = matricule;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        @Override
        public String toString(){
            return nom.toUpperCase() + " " + prenom + " (" + matricule + ")";
        }

    }
    class Connexion {

        private final static String host = "localhost";
        private final static String user = "gsbrv";
        private final static String mdp = "azerty";
        private final static String dbName = "gsbrv";
        private final static String port = "3306";

        public static Connection getConnexion(){
            Connection conn = null;
            Properties props = new Properties();
            String url = "jdbc:mariadb://"+host+":"+port+"/"+dbName;
            props.setProperty("user", user);
            props.setProperty("password", mdp);
            try {
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return conn;
        }

    }
    public static class Session {

        private static Session session = null;

        private static Visiteur leVisiteur;

        private Session(Visiteur leVisiteur){
            this.leVisiteur = leVisiteur;
        }

        public static void ouvrir(Visiteur leVisiteur){
            if(session == null){
                session = new Session(leVisiteur);
            }
        }

        public static void fermer(){
            session = null;
        }

        public static Session getSession(){
            return session;
        }

        public Visiteur getLeVisiteur(){
            return leVisiteur;
        }

        public static boolean estOuverte(){
            if(session == null){
                return true;
            }
            else{
                return false;
            }
        }

    }

    public class Praticien {

        private String numero;
        private String nom;
        private String prenom;
        private String ville;
        private String adresse;
        private String codePostale;
        private double coefNotoriete;
        private LocalDate dateDerniereVisite;
        private int dernierCoedConfiance;

        public Praticien(String numero, String nom, String ville, double coefNotoriete, LocalDate dateDerniereVisite, int dernierCoedConfiance) {
            super();
            this.numero = numero;
            this.nom = nom;
            this.ville = ville;
            this.coefNotoriete = coefNotoriete;
            this.dateDerniereVisite = dateDerniereVisite;
            this.dernierCoedConfiance = dernierCoedConfiance;
        }

        public Praticien(String numero, String nom, String prenom, String ville, String adresse, String codePostale, double coefNotoriete, LocalDate dateDerniereVisite, int dernierCoedConfiance) {
            super();
            this.numero = numero;
            this.nom = nom;
            this.prenom = prenom;
            this.ville = ville;
            this.adresse = adresse;
            this.codePostale = codePostale;
            this.coefNotoriete = coefNotoriete;
            this.dateDerniereVisite = dateDerniereVisite;
            this.dernierCoedConfiance = dernierCoedConfiance;
        }

        public Praticien(){
            super();
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getVille() {
            return ville;
        }

        public void setVille(String ville) {
            this.ville = ville;
        }

        public double getCoefNotoriete() {
            return coefNotoriete;
        }

        public void setCoefNotoriete(double coefNotoriete) {
            this.coefNotoriete = coefNotoriete;
        }

        public LocalDate getDateDerniereVisite() {
            return dateDerniereVisite;
        }

        public void setDateDerniereVisite(LocalDate dateDerniereVisite) {
            this.dateDerniereVisite = dateDerniereVisite;
        }

        public int getDernierCoedConfiance() {
            return dernierCoedConfiance;
        }

        public void setDernierCoedConfiance(int dernierCoedConfiance) {
            this.dernierCoedConfiance = dernierCoedConfiance;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getAdresse() {
            return adresse;
        }

        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }

        public String getCodePostale() {
            return codePostale;
        }

        public void setCodePostale(String codePostale) {
            this.codePostale = codePostale;
        }
    }

    public class RapportVisite {

        private int numero;
        private LocalDate dateVisite;
        private LocalDate dateRedaction;
        private String bilan;
        private String motif;
        private int coefConfiance;
        private boolean lu;
        private Visiteur leVisiteur;
        private Praticien lePraticien;

        public RapportVisite(int numero, LocalDate dateVisite, LocalDate dateRedaction, String bilan, String motif, int coefConfiance, boolean lu, Visiteur leVisiteur, Praticien lePraticien) {
            super();
            this.numero = numero;
            this.dateVisite = dateVisite;
            this.dateRedaction = dateRedaction;
            this.bilan = bilan;
            this.motif = motif;
            this.coefConfiance = coefConfiance;
            this.lu = lu;
            this.leVisiteur = leVisiteur;
            this.lePraticien = lePraticien;
        }

        public RapportVisite(){
            super();
        }

        public int getNumero() {
            return numero;
        }

        public void setNumero(int numero) {
            this.numero = numero;
        }

        public LocalDate getDateVisite() {
            return dateVisite;
        }

        public void setDateVisite(LocalDate dateVisite) {
            this.dateVisite = dateVisite;
        }

        public LocalDate getDateRedaction() {
            return dateRedaction;
        }

        public void setDateRedaction(LocalDate dateRedaction) {
            this.dateRedaction = dateRedaction;
        }

        public String getBilan() {
            return bilan;
        }

        public void setBilan(String bilan) {
            this.bilan = bilan;
        }

        public String getMotif() {
            return motif;
        }

        public void setMotif(String motif) {
            this.motif = motif;
        }

        public int getCoefConfiance() {
            return coefConfiance;
        }

        public void setCoefConfiance(int coefConfiance) {
            this.coefConfiance = coefConfiance;
        }

        public boolean isLu() {
            return lu;
        }

        public void setLu(boolean lu) {
            this.lu = lu;
        }

        public Visiteur getLeVisiteur() {
            return leVisiteur;
        }

        public void setLeVisiteur(Visiteur leVisiteur) {
            this.leVisiteur = leVisiteur;
        }

        public Praticien getLePraticien() {
            return lePraticien;
        }

        public void setLePraticien(Praticien lePraticien) {
            this.lePraticien = lePraticien;
        }
    }
  
    
    
    
    class PanneauAccueil {
    }

    class PanneauRapports {
    }

    class PanneauPraticiens {
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        VBox rootV = new VBox();
        Scene scene = new Scene(root, 300, 250);
        Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
        alertQuitter.setTitle("Quitter");
        alertQuitter.setHeaderText("Demande de confirmation");
        alertQuitter.setContentText("Voulez-vous vraiment quitter l'application ?");
        ButtonType btnOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alertQuitter.getButtonTypes().setAll(btnOui, btnNon);
        Dialog authentification = new Dialog();
        authentification.setTitle("Authentification");
        authentification.setHeaderText("Saisir vos données de connexions");
        TextField textFieldLogin = new TextField();
        Label labelLogin = new Label("Matricule : ");
        PasswordField passwordFieldMdp = new PasswordField();
        Label labelMotDePasse = new Label("Mot de passe : ");
        ButtonType boutonAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType boutonConnexion = new ButtonType("Se connecter", ButtonBar.ButtonData.OK_DONE);
        authentification.getDialogPane().getButtonTypes().addAll(boutonConnexion, boutonAnnuler);
        GridPane gridPaneDialog = new GridPane();
        gridPaneDialog.setHgap(6);
        gridPaneDialog.setVgap(6);
        gridPaneDialog.setPadding(new Insets(6));
        gridPaneDialog.add(labelLogin, 0, 0);
        gridPaneDialog.add(labelMotDePasse, 0, 1);
        gridPaneDialog.add(textFieldLogin, 1, 0);
        gridPaneDialog.add(passwordFieldMdp, 1, 1);
        authentification.getDialogPane().setContent(gridPaneDialog);
        final Node block = authentification.getDialogPane().lookupButton(boutonConnexion);
        block.setDisable(true);
        textFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            block.setDisable(newValue.trim().isEmpty());
        });
        Alert alerteErreur = new Alert(Alert.AlertType.ERROR);
        alerteErreur.setTitle("Erreur");
        alerteErreur.setHeaderText("Erreur de connexion");
        alerteErreur.setContentText("Votre identifion ou mot de passe est invalide.");
        ButtonType btnClose = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
        alerteErreur.getButtonTypes().setAll(btnClose);
        authentification.setResultConverter(dialogButton -> {
            if (dialogButton == boutonConnexion) {
                return new Pair<>(textFieldLogin.getText(), passwordFieldMdp.getText());
            }
            return null;
        });
        MenuBar barreMenus = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        barreMenus.getMenus().add(menuFichier);
        MenuItem itemSeConnecter = new MenuItem("Se connecter");
        menuFichier.getItems().add(itemSeConnecter);
        MenuItem itemSeDeconnecter = new MenuItem("Se déconnecter");
        menuFichier.getItems().add(itemSeDeconnecter);
        itemSeDeconnecter.setDisable(true);
        MenuItem itemQuitter = new MenuItem("Quitter");
        menuFichier.getItems().add(itemQuitter);
        itemQuitter.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        SeparatorMenuItem separateurMenu = new SeparatorMenuItem();
        Menu menuRapports = new Menu("Rapports");
        barreMenus.getMenus().add(menuRapports);
        MenuItem itemConsulter = new MenuItem("Consulter");
        menuRapports.getItems().add(itemConsulter);
        menuRapports.setDisable(true);
        Menu menuPraticiens = new Menu("Praticiens");
        barreMenus.getMenus().add(menuPraticiens);
        MenuItem itemHesitant = new MenuItem("Hésitants");
        menuPraticiens.getItems().add(itemHesitant);
        menuPraticiens.setDisable(true);
        itemSeConnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Optional<Pair<String, String>> reponse = authentification.showAndWait();
                        reponse.ifPresent(usernamePassword -> {
                                itemSeConnecter.setDisable(true);
                                itemSeDeconnecter.setDisable(false);
                                menuRapports.setDisable(false);
                                menuPraticiens.setDisable(false);
                                primaryStage.setTitle(Session.getSession().getLeVisiteur().getNom().toUpperCase() + " " + Session.getSession().getLeVisiteur().getPrenom());
                        });
                    }
                }
        );
        itemSeDeconnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Session.fermer();
                        itemSeConnecter.setDisable(false);
                        itemSeDeconnecter.setDisable(true);
                        menuRapports.setDisable(true);
                        menuPraticiens.setDisable(true);
                        primaryStage.setTitle("GSB-RV-DR");
                    }
                }
        );
        itemQuitter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Optional<ButtonType> reponse = alertQuitter.showAndWait();
                        if (reponse.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                            Platform.exit();
                        } else {
                            alertQuitter.close();
                        }
                    }
                }
        );
        itemConsulter.setOnAction(
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent event) {

                        rootV.setSpacing(5);
                        rootV.setPadding(new Insets(10, 20, 10, 10));
                        GridPane form = new GridPane();
                        Button btnValid = new Button("Valider");
                        ComboBox<Visiteur> comboBoxVisiteur = new ComboBox<Visiteur>();
                        ComboBox<Integer> comboBoxMois = new ComboBox();
                        ComboBox<Integer> comboBoxAnnee = new ComboBox<Integer>();
                        TableView<RapportVisite> tableViewRapportVisite = new TableView<RapportVisite>();
                        comboBoxVisiteur.setPromptText("Visiteur");
                        comboBoxMois.setPromptText("Mois");
                        comboBoxAnnee.setPromptText("Année");
                        form.setHgap(10);
                        form.setVgap(10);
                        form.add(comboBoxVisiteur, 0, 0);
                        form.add(comboBoxMois, 1, 0);
                        form.add(comboBoxAnnee, 2, 0);
                        TableColumn<RapportVisite, Integer> colonneNumero = new TableColumn<RapportVisite, Integer>("Numéro");
                        TableColumn<RapportVisite, String> colonnePraticien = new TableColumn<RapportVisite, String>("Praticien");
                        TableColumn<RapportVisite, String> colonneVille = new TableColumn<RapportVisite, String>("Ville");
                        TableColumn<RapportVisite, LocalDate> colonneVisite = new TableColumn<RapportVisite, LocalDate>("Visite");
                        TableColumn<RapportVisite, LocalDate> colonneRedaction = new TableColumn<RapportVisite, LocalDate>("Rédaction");
                        colonneNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
                        colonnePraticien.setCellValueFactory(new PropertyValueFactory<>("praticien"));
                        colonneVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
                        colonneVisite.setCellValueFactory(new PropertyValueFactory<>("dateVisite"));
                        colonneRedaction.setCellValueFactory(new PropertyValueFactory<>("dateRedaction"));
                        tableViewRapportVisite.getColumns().addAll(colonneNumero, colonnePraticien, colonneVille, colonneVisite, colonneRedaction);
                        tableViewRapportVisite.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                        rootV.getChildren().add(form);
                        rootV.getChildren().add(btnValid);
                        rootV.getChildren().add(tableViewRapportVisite);
                    }
                }
        );

        itemHesitant.setOnAction(
                new EventHandler<ActionEvent>() {
                    public final static int CRITERE_COEF_CONFIANCE = 1;
                    public final static int CRITERE_COEF_NOTORIETE = 2;
                    public final static int CRITERE_DATE_VISITE = 3;
                    private int critereTri = CRITERE_COEF_CONFIANCE;

                    public void handle(ActionEvent event) {
                            rootV.setSpacing(5);
                            rootV.setPadding(new Insets(10, 10, 10, 10));
                            GridPane gridPaneRadios = new GridPane();
                            gridPaneRadios.setHgap(10);
                            gridPaneRadios.setVgap(10);
                            Label lbConsigne = new Label("Sélectionner un critère de tri : ");
                            lbConsigne.setStyle("-fx-font-weight: bold");
                            rootV.getChildren().add(lbConsigne);
                            ToggleGroup tgTri = new ToggleGroup();
                            RadioButton radioButtonCoefConfiance = new RadioButton("Confiance");
                            RadioButton radioButtonCoefNotoriete = new RadioButton("Notoriété");
                            RadioButton radioButtonDateVisite = new RadioButton("Date Visite");
                            TableView<Praticien> tableViewPraticien = new TableView<Praticien>();
                            radioButtonCoefConfiance.setToggleGroup(tgTri);
                            radioButtonCoefConfiance.setSelected(true);
                            gridPaneRadios.add(radioButtonCoefConfiance, 0, 0);
                            gridPaneRadios.add(radioButtonCoefNotoriete, 1, 0);
                            gridPaneRadios.add(radioButtonDateVisite, 2, 0);
                            rootV.getChildren().add(gridPaneRadios);
                            TableColumn<Praticien, Integer> colonneNumero = new TableColumn<Praticien, Integer>("Numéro");
                            TableColumn<Praticien, String> colonneNom = new TableColumn<Praticien, String>("Nom");
                            TableColumn<Praticien, String> colonneVille = new TableColumn<Praticien, String>("Ville");
                            colonneNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
                            colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
                            colonneVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
                            tableViewPraticien.getColumns().addAll(colonneNumero, colonneNom, colonneVille);
                            tableViewPraticien.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                            rootV.getChildren().add(tableViewPraticien);
                    }

                }
        );
        root.setTop(barreMenus);
        root.setCenter(rootV);
        primaryStage.setTitle("GSB-RV-DR");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
