package com.example.project;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendAddressToDriverController implements Initializable {
    public static final String ACCOUNT_SID = "ACe3dc4af5115a8cfd1ec6d57fd691161f";
    public static final String AUTH_TOKEN = "d43ed4dda48b18e117a24a5dfcc7c342";

    ServerSocket server;
    Socket connection;
    DataOutputStream dos;
    DataInputStream dis;
    int counter = 1;

    String info = "";

    private Stack<Scene> navigationStack = new Stack();
    @FXML
    private VBox vBoxStudents;
    @FXML
    private Button backBtn;
    @FXML
    private Button sendBtn;

    private List<Integer> orderIds;

    List<String> stInfos = new ArrayList<>();

    public SendAddressToDriverController() {
    }

    public void sendAddressScreen() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SendAddressToDriver.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),900,500);
        stage.setTitle("Send Address to Driver");
        stage.setScene(scene);
        stage.show();

    }

    private List<Integer> lstOfOrderIds(){
        orderIds = CheckLaundryOrdersController.getKeepIds();
        List<Integer> lst = new ArrayList<>();
        for (int i: orderIds){
            lst.add(i);
        }
        return lst;
    }

    @FXML
    protected void onSendBtnClicked(){

        //sendEmail2();
        String combinedStInfo = "";
        for(String s:stInfos){
            combinedStInfo += s+"\n\n";
        }
        String finalCombinedStInfo = combinedStInfo;
        send(finalCombinedStInfo);

    }
    public void send(String infoToSend){

        try {
            server = new ServerSocket(5050);
            System.out.println("SERVER: Server started "
                    + InetAddress.getLocalHost().getHostAddress());
            connection = server.accept();

            dos = new DataOutputStream(connection.getOutputStream());
            if(!infoToSend.equals(""))
                dos.writeUTF(infoToSend);
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail() {

        final String username = "demmambesi@gmail.com";
        final String password = "EnrObe56";
        String recipient = "naomi.mtshambela@gmail.com"; // Replace with the recipient's email address

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Subject of your email");
            message.setText("Body of your email");

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void showCustomDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Custom Dialog");

        Label label = new Label("This is a custom dialog.");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());

        VBox dialogVBox = new VBox(20);
        dialogVBox.getChildren().addAll(label, closeButton);

        Scene dialogScene = new Scene(dialogVBox, 250, 150);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public void sendEmail2(){
        final String username = "demmambesi@gmail.com";
        final String password = "EnrObe56";
        String recipient = "s220106495@mandela.ac.za"; // Replace with the recipient's email address

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.google.com"); // Replace with your SMTP server
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Subject of your email");
            message.setText("Body of your email");

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stInfos = getStudentInfo();
        List<String> listOfStrings = getStudentInfo();
        for(int i=0;i<listOfStrings.size();i++){
            if(listOfStrings.get(i).equals(null)) System.out.println("no info");
            Label label = new Label();
            Label id = new Label();
            id.setText("Order Number:    "+lstOfOrderIds().get(i));
            id.setStyle("-fx-font-size: 25px;");
            id.setStyle("-fx-border-color: deepskyblue");
            label.setStyle("-fx-font-size: 20px;");
            label.autosize();
            label.setStyle("-fx-border-color: deepskyblue; -fx-border-width: 1px; -fx-padding: 10px;");
            BorderPane borderPane = new BorderPane();
            borderPane.setTop(id);
            borderPane.setCenter(label);
            label.setText(listOfStrings.get(i));
            vBoxStudents.getChildren().add(borderPane);
            vBoxStudents.setStyle("-fx-background-color: rgba(255,255,255,0.72)");
        }

    }
    public List<String> getStudentInfo(){
        Database database = new Database();
        database.connectToDB();
        Statement statement = database.getStatement();

        List<Integer> lst = lstOfOrderIds();
        List<String> studentInfo = new ArrayList<>();
        for(int i=0;i<lst.size();i++){
            int orderId = lst.get(i);
            String sql = "SELECT *\n" +
                    "FROM Student s\n" +
                    "JOIN LaundryOrder lo ON s.StudentNo = lo.StudentNo\n" +
                    "WHERE lo.OrderNumber = "+orderId+";";
            try {
                ResultSet resultSet = statement.executeQuery(sql);
                if(resultSet.next()){
                    int StudentNo = resultSet.getInt("StudentNo");
                    String StudentName = resultSet.getString("StudentName");
                    String sResidentialAddress = resultSet.getString("sResidentialAddress");
                    String sContactNo = resultSet.getString("sContactNo");
                    String Password = resultSet.getString("Password");
                    String StudentSurname = resultSet.getString("Surname");

                    Student student = new Student(StudentName, StudentNo,sResidentialAddress,"0"+sContactNo,Password,StudentSurname);
                    studentInfo.add(student.toString());
                }
                else{
                    System.out.println("noResultSet");
                    return null;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return studentInfo;
    }
    private void navigateToNewScreen(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene newScene = new Scene(loader.load(), 900, 500);
            navigationStack.push(((Node) event.getSource()).getScene()); // Push the current scene onto the stack
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackBtnClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CustomerManagement.fxml");
    }
}
