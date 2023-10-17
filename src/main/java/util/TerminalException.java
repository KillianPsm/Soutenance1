package util;

/**
 * Classe de definition de l'exception TerminalException qui peut etre retournee dans l'usage des methodes de la classe Terminal.
 */
public class TerminalException extends RuntimeException {
    Exception ex;

    TerminalException(Exception e) {
        ex = e;
    }
}
