package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    @Override
    public void executeTest() {
        ioService.printLine("Please answer the questions below");

        // Получить вопросы из дао и вывести их с вариантами ответов
    }
}
