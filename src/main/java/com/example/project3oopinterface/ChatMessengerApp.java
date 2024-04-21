package com.example.project3oopinterface;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

interface MessageClickListener {
    void onMessageClicked(BaseMessage message);
}

abstract class BaseMessage {
    protected User sender;
    protected String content;

    public BaseMessage(User sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public abstract String render();

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}

class TextMessage extends BaseMessage {
    public TextMessage(User sender, String content) {
        super(sender, content);
    }

    @Override
    public String render() {
        return sender.getNickname() + ": " + content;
    }
}

class User {
    private String nickname;
    private String phoneNumber;
    private String title;

    public User(String nickname, String phoneNumber, String title) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.title = title;
    }

    // Getters and setters
    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "Nickname: " + nickname + ", Phone Number: " + phoneNumber + ", Title: " + title;
    }
}

class Chat {
    private String chatName;
    private ArrayList<User> users;
    private ArrayList<BaseMessage> messages;

    public Chat(String chatName) {
        this.chatName = chatName;
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addMessage(BaseMessage message) {
        messages.add(message);
    }

    public ArrayList<BaseMessage> getMessages() {
        return messages;
    }
    public void displayMessages(ListView<String> messageListView) {
        messageListView.getItems().clear();
        for (BaseMessage message : messages) {
            messageListView.getItems().add(message.render());
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}

class FileMessage extends BaseMessage {
    private String fileName;

    public FileMessage(User sender, String content, String fileName) {
        super(sender, content);
        this.fileName = fileName;
    }

    @Override
    public String render() {
        return sender.getNickname() + " sent a file: " + fileName;
    }
}

class ImageMessage extends BaseMessage {
    private String imageUrl;

    public ImageMessage(User sender, String content, String imageUrl) {
        super(sender, content);
        this.imageUrl = imageUrl;
    }

    @Override
    public String render() {
        return sender.getNickname() + " sent an image: " + imageUrl;
    }
}

public class ChatMessengerApp extends Application implements MessageClickListener {

    private Chat selectedChat;
    private ListView<Chat> chatListView;
    private ListView<String> userListView;
    private ListView<String> messageListView;
    private ComboBox<User> senderComboBox;
    private ComboBox<String> messageTypeComboBox;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(event -> {
            String nickname = nicknameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String title = titleField.getText();
            if (!nickname.isEmpty() && !phoneNumber.isEmpty() && !title.isEmpty()) {
                User user = new User(nickname, phoneNumber, title);
                selectedChat.addUser(user);
                updateUsersListView();
                nicknameField.clear();
                phoneNumberField.clear();
                titleField.clear();
                saveUsersToFile();
            }
        });
        senderComboBox = new ComboBox<>();
        senderComboBox.setPromptText("Select Sender");
        senderComboBox.setPrefWidth(150);

        messageTypeComboBox = new ComboBox<>();
        messageTypeComboBox.setPromptText("Select Message Type");
        messageTypeComboBox.setPrefWidth(150);
        messageTypeComboBox.setItems(FXCollections.observableArrayList("TextMessage", "ImageMessage", "FileMessage"));

        userListView = new ListView<>();
        userListView.setPrefHeight(100);

        Label chatLabel = new Label("Chats:");
        chatListView = new ListView<>();
        chatListView.setPrefHeight(100);
        chatListView.setOnMouseClicked(event -> {
            selectedChat = chatListView.getSelectionModel().getSelectedItem();
            if (selectedChat != null) {
                updateUsersListView();
                selectedChat.displayMessages(messageListView);
            }
        });

        Label userLabel = new Label("Users:");
        HBox userBox = new HBox(10);
        userBox.getChildren().addAll(userLabel, userListView);

        messageListView = new ListView<>();
        messageListView.setPrefHeight(200);
        messageListView.setOnMouseClicked(event -> {
            int index = messageListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < selectedChat.getMessages().size()) {
                BaseMessage message = selectedChat.getMessages().get(index);
                onMessageClicked(message);
            }
        });

        Button clearMessagesButton = new Button("Clear Messages");
        clearMessagesButton.setOnAction(event -> messageListView.getItems().clear());

        Button sendMessageButton = new Button("Send Message");
        TextField messageField = new TextField();
        messageField.setPromptText("Enter message...");
        sendMessageButton.setOnAction(event -> {
            String messageContent = messageField.getText();
            if (!messageContent.isEmpty() && messageTypeComboBox.getValue() != null) {
                User sender = senderComboBox.getValue(); // Use selected sender from ComboBox
                BaseMessage message = null;
                switch (messageTypeComboBox.getValue()) {
                    case "TextMessage":
                        message = new TextMessage(sender, messageContent);
                        break;
                    case "ImageMessage":
                        message = new ImageMessage(sender, messageContent, "image-url");
                        break;
                    case "FileMessage":
                        message = new FileMessage(sender, messageContent, "file-name");
                        break;
                }
                if (message != null) {
                    selectedChat.addMessage(message);
                    selectedChat.displayMessages(messageListView);
                    messageField.clear();
                }
            }
        });

        HBox messageBox = new HBox(10);
        messageBox.getChildren().addAll(messageField, sendMessageButton);

        Button clearUsersButton = new Button("Clear Users");
        clearUsersButton.setOnAction(event -> {
            if (selectedChat != null) {
                selectedChat.getUsers().clear();
                updateUsersListView();
                saveUsersToFile();
            }
        });

        root.getChildren().addAll(nicknameField, phoneNumberField, titleField, addUserButton,
                chatLabel, chatListView, userBox, userListView, clearMessagesButton, messageListView, messageBox, senderComboBox, messageTypeComboBox, clearUsersButton);

        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.setTitle("Chat Messenger");
        primaryStage.show();

        Chat chat1 = new Chat("Chat 1");
        Chat chat2 = new Chat("Chat 2");
        User user1 = new User("Alice", "123456789", "Manager");
        User user2 = new User("Bob", "987654321", "Developer");
        chat1.addUser(user1);
        chat1.addUser(user2);
        chat2.addUser(user1);

        chatListView.setItems(FXCollections.observableArrayList(chat1, chat2));
        updateUsersListView();

        loadChatsFromFile();
        loadUsersFromFile();
    }

    private void updateUsersListView() {
        if (selectedChat != null) {
            ArrayList<String> userInfoList = new ArrayList<>();
            for (User user : selectedChat.getUsers()) {
                userInfoList.add(user.toString());
            }
            userListView.setItems(FXCollections.observableArrayList(userInfoList));
            senderComboBox.setItems(FXCollections.observableArrayList(selectedChat.getUsers()));
            senderComboBox.getSelectionModel().selectFirst(); // Select the first user by default
        }
    }

    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(selectedChat.getUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        ArrayList<User> users = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.txt"))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (users != null && !users.isEmpty()) {
            selectedChat.getUsers().addAll(users);
            updateUsersListView();
        }
    }

    private void loadChatsFromFile() {
        ArrayList<Chat> chats = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("chats.dat"))) {
            chats = (ArrayList<Chat>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (chats != null && !chats.isEmpty()) {
            chatListView.getItems().addAll(chats);
        }
    }

    @Override
    public void onMessageClicked(BaseMessage message) {
        TextInputDialog dialog = new TextInputDialog(message.getContent());
        dialog.setTitle("Edit Message");
        dialog.setHeaderText("Editing Message");
        dialog.setContentText("Enter new message:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newContent -> {
            message.updateContent(newContent);
            selectedChat.displayMessages(messageListView); // Update message list view
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


