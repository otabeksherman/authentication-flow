package com.authentication.Repository;

import com.authentication.Model.User;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class UserRepository {

    private static final String USERS_REPO_PATH = "src/main/java/com/authentication/Repository/users/";
    private static Logger logger = LogManager.getLogger(Repository.class.getName());

    private List<User> users = new ArrayList<>();

    public User save(User user) {
        Gson gson = new Gson();
        String filePath = USERS_REPO_PATH + user.getEmail();
        try(FileWriter fileWriter = new FileWriter(filePath)) {
            users.add(user);
            gson.toJson(user, fileWriter);
            fileWriter.flush();
            logger.info("write user to Repository");
            users.add(user);
            return user;
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public Optional<User> getUser(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    public void delete(String email) {
        Optional<User> user = getUser(email);
        if (user.isPresent()) {
            delete(user.get());
        }
    }

    public void delete(User user){
        users.remove(user);
    }

    public void initUsers() {
        Optional<List<String>> filePaths = getFilePaths();
        if (!filePaths.isPresent()) {
            return;
        }
        Gson gson = new Gson();
        for (String path : filePaths.get()) {
            try(BufferedReader reader = new BufferedReader(new FileReader(path));) {
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                // delete the last new line separator
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                User user = gson.fromJson(stringBuilder.toString(), User.class);
                this.users.add(user);
            } catch (IOException e) {
                Thread.dumpStack();
                throw new RuntimeException(e);
            }
        }
    }

    private Optional<List<String>> getFilePaths() {
        List<String> paths;
        try (Stream<Path> stream = Files.list(Paths.get(USERS_REPO_PATH))) {
            paths = stream
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            Thread.dumpStack();
            throw new RuntimeException(e);
        }
        return Optional.of(paths);
    }
}
