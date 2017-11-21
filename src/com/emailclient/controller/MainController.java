package com.emailclient.controller;

import com.emailclient.controller.services.CreateAndRegisterEmailAccountService;
import com.emailclient.controller.services.FolderUpdaterService;
import com.emailclient.controller.services.MessageRendererService;
import com.emailclient.controller.services.SaveAttachmentService;
import com.emailclient.model.EmailMessageBean;
import com.emailclient.model.SampleData;
import com.emailclient.model.Singleton;
import com.emailclient.model.folder.EmailFolderBean;
import com.emailclient.model.table.BoldableRowFactory;
import com.emailclient.model.table.FormatableInteger;
import com.emailclient.view.SetStageIcon;
import com.emailclient.view.ViewFactory;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable {

    /*
      The mail controller class.
     */

    public MainController(ModelAccess modelAccess) {

        super(modelAccess);
    }


    @FXML
    private JFXButton refresh;

    @FXML
    private WebView messagerenderer;

    // Message renderer class instance :
    private MessageRendererService messageRendererService;

    // For TableView.
    @FXML
    private TableView<EmailMessageBean> emailTableView;

    @FXML
    private TableColumn<EmailMessageBean, String> subjectCol;

    @FXML
    private TableColumn<EmailMessageBean, String> senderCol;

    @FXML
    private TableColumn<EmailMessageBean, FormatableInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessageBean, Date> dateCol;


    // For JFX-TreeView
    @FXML
    private TreeView<String> emailFoldersTreeView;
    private SampleData sampleData = new SampleData();
    private MenuItem showDetails = new MenuItem("Show Details");
    private Singleton singleton; // Not used anymore, Functionality shifted to AbstractController.java and ModelAccess.java classes.


    @FXML
    private Label downAttachLabel;

    @FXML
    private ProgressBar downAttachProgress;

    @FXML
    private JFXButton downAttachBtn;

    @FXML
    private JFXButton compose;

    private SaveAttachmentService saveAttachmentService;


    @FXML
    void refreshBtnAction(ActionEvent event) {

        System.out.println("{?} Refresh button not implemented. :(");
//        Service<Void> emailService = new Service<Void>() {
//            @Override
//            protected Task<Void> createTask() {
//                return new Task<Void>() {
//                    @Override
//                    protected Void call() throws Exception {
//
//                        ObservableList<EmailMessageBean> data = getModelAccess().getSelectedFolder().getData();
//
//                        final EmailAccountBean emailAccountBean = new EmailAccountBean("$$Email-Here$$",
//                                "$$fpass-here$$");
//
//                        emailAccountBean.addEmailsToData(data);
//
//                        return null;
//                    }
//                };
//            }
//        };
//        emailService.start();
    }

    @FXML
    void composeMessageBtn(ActionEvent event) {

        Scene scene = ViewFactory.defaultFactory.getComposeMessageScene();
        Stage stage = new Stage();
        stage.setOpacity(0.955f); // Default Opacity value.
        stage.setTitle("Compose an e-mail");

        SetStageIcon.getInstance.setDefaultIcon(stage);

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void downAttachBtnAction(ActionEvent event) { // For download button.

        EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
        if (message != null && message.hasAttachments()) {

            saveAttachmentService.setMessageToDownload(message);
            saveAttachmentService.restart();
        }

    }


    @FXML
    void changeReadAction(ActionEvent event) {

        EmailMessageBean message = getModelAccess().getSelectedMessage();

        System.out.println("{*} Marking as read/unread.");

        if (message != null) {

            boolean value = message.isRead();
            message.setRead(!value);

            EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();

            if (selectedFolder != null) {

                if (value) {

                    selectedFolder.incrementUnreadMessageCount(1);
                } else {

                    selectedFolder.decrementUnreadMessageCount(1);
                }
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        downAttachProgress.setVisible(false);
        downAttachLabel.setVisible(false);

        saveAttachmentService = new SaveAttachmentService(downAttachProgress, downAttachLabel); // For attachment downloading.
        messageRendererService = new MessageRendererService(messagerenderer.getEngine());

        downAttachProgress.progressProperty().bind(saveAttachmentService.progressProperty()); // For download progress bar.

        // Updater thread :
        FolderUpdaterService folderUpdaterService = new FolderUpdaterService(getModelAccess().getFoldersList());
        folderUpdaterService.start();

        emailTableView.setRowFactory( // For making the selected text bold.

                (e) -> new BoldableRowFactory<>()
        );

        ViewFactory viewFactory = ViewFactory.defaultFactory;

        // singleton = Singleton.getInstance();

        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        // BUG : size doesn't get it's default comparator overridden.
        sizeCol.setComparator(new FormatableInteger(0));


        // Custom comparator, anonymous class using lambdas.
//        sizeCol.setComparator( // Functionality shifted to FormatableInteger.java class.
//                (String o1, String o2) -> {
//                    Integer i1, i2;
//
//                    i1 = EmailMessageBean.formattedValues.get(o1);
//                    i2 = EmailMessageBean.formattedValues.get(o2);
//
//                    return i1.compareTo(i2);
//                });


        // For TreeView :
        EmailFolderBean<String> root = new EmailFolderBean<>("");
        emailFoldersTreeView.setRoot(root);
        emailFoldersTreeView.setShowRoot(false);


        // Sample or static data :
//        EmailFolderBean<String> kps = new EmailFolderBean<>("Example@yahoo.com");
//        root.getChildren().add(kps);
//
//        EmailFolderBean<String> Inbox = new EmailFolderBean<>("Inbox", "CompleteInbox");
//        EmailFolderBean<String> Sent = new EmailFolderBean<>("Sent", "CompleteSent");
//        Sent.getChildren().add(new EmailFolderBean<>("SubFolder1", "SubFolder1Complete"));
//        Sent.getChildren().add(new EmailFolderBean<>("SubFolder2", "SubFolder2Complete2"));
//        EmailFolderBean<String> Spam = new EmailFolderBean<>("Spam", "CompleteSpam");
//        kps.getChildren().addAll(Inbox, Sent, Spam);
//        Inbox.getData().addAll(SampleData.Inbox);
//        Sent.getData().addAll(SampleData.Sent);
//        Spam.getData().addAll(SampleData.Spam);


        CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService1 = new CreateAndRegisterEmailAccountService("$$EnterEmailAddressHere$$",
                "$$pass$$",
                root,
                getModelAccess());
        createAndRegisterEmailAccountService1.start();


        CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService2 = new CreateAndRegisterEmailAccountService("$$EnterEmailAddressHere$$",
                "$$pass$",
                root,
                getModelAccess());
        createAndRegisterEmailAccountService2.start();

        // For options when right click is pressed :
        emailTableView.setContextMenu(new ContextMenu(showDetails));


        // For Folders TreeView.
        emailFoldersTreeView.setOnMouseClicked(
                (event) -> {

                    EmailFolderBean<String> item = (EmailFolderBean<String>) emailFoldersTreeView.getSelectionModel().getSelectedItem();

                    if (item != null && !item.isTopElement()) { // Null check.

                        // Get values.
                        emailTableView.setItems(item.getData());
                        getModelAccess().setSelectedFolder(item);

                        // Clear the selected message.
                        getModelAccess().setSelectedMessage(null);
                    }
                });

        // For when TableView is clicked.
        emailTableView.setOnMouseClicked(
                (event) -> {

                    EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();

                    if (message != null) { // Null check.

                        // Render message to WebView.
                        getModelAccess().setSelectedMessage(message);

                        messageRendererService.setMessageToRender(message);
                        messageRendererService.restart(); // Using restart() instead of start().
                        //  Platform.runLater(messageRendererService); // On application thread.

                        //    messagerenderer.getEngine().loadContent(message.getContent());
                        // Passing the message instance to singleton.
                        // System.out.println("[>] Singleton Initialized.");
                        //singleton.setMessage(message); // Functionality shifted to getmodelaccess() method.
                    }

                });
        showDetails.setOnAction(
                (event) -> {

                    // Getting detailsScene from ViewFactory.java class in view package.
                    Scene detailsScene = viewFactory.getEmailDetailsScene();

                    // Style sheet for show details.
                    Stage stage = new Stage();
                    stage.setOpacity(0.95f); // Scene transparency.
                    stage.setTitle("E-mail Details");

                    SetStageIcon.getInstance.setDefaultIcon(stage);

                    stage.setScene(detailsScene);
                    stage.show();
                });
    }
}
