package io.github.halfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Validator {
    private static Validator instance = null;
    private Map<String, String> database;

    protected Validator() throws IOException {
        database = new HashMap<>();
        populateDB();
    }
    
    public Validator getInstance() throws IOException {
        if (instance == null) {
            instance = new Validator();        
        }
        return instance;
    }

    // line format = "username:password"
    private void populateDB() throws IOException {
        InputStreamReader fileStream = new InputStreamReader(this.getClass().getResourceAsStream("/database.txt"));
        BufferedReader fileBufferReader = new BufferedReader(fileStream);
        String line = fileBufferReader.readLine();
        while (line != null) {
            String username = line.substring(0, line.indexOf(":"));
            String password = line.substring(line.indexOf(":") + 1, line.length());

            database.put(username, password);
            line = fileBufferReader.readLine();
        }
    }

    public boolean match(String username, String password) {
        return database.containsKey(username) &&
               database.get(username).equals(password);
    }
}
