package org.example;

import java.io.File;

public class CustomException extends RuntimeException{

    public CustomException(File f){
        super("Файл " + f.getName() + " пуст");
    }

    public CustomException(String message, Throwable clause){
        super(message, clause);
    }
}

