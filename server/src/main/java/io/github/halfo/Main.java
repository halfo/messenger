package io.github.halfo;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(8000);
        } catch (IOException e) {
        }
    }
}
