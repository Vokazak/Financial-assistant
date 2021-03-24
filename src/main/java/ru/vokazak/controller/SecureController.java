package ru.vokazak.controller;

public interface SecureController<REQ, RES> {
    RES handle(REQ request, Long userId);

    Class<REQ> getRequestClass();

}
