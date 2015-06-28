package io.github.halfo;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        Server server = new Server(8000);
    }
}
