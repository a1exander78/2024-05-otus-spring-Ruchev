package ru.otus.hw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.services.IceService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
	private final IceService service;

	@Override
	public void run(String... args) {
		service.startGenerateIceCubes();
	}
}
