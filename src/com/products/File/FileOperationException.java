package com.products.File;

/**Excepție pentru fișiere */
public class FileOperationException extends Exception {
    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
