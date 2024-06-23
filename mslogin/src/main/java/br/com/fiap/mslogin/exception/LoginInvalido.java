package br.com.fiap.mslogin.exception;

public class LoginInvalido extends RuntimeException{

    public LoginInvalido() {
        super("E-mail e/ou Senha Inválidos.");
    }
}