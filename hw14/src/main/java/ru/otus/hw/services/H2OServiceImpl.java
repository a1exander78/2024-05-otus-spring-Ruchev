package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;
import ru.otus.hw.domain.Water;


@Service
@Slf4j
public class H2OServiceImpl implements H2OService {

	@Override
	public Water melt(Ice iceCube) {
		String temp = iceCube.temperature();
		log.info("Melting iceCube {}", temp);
		delay();
		temp = "0°C";
		log.info("Melting iceCube {} done", temp);
		return new Water(temp);
	}

	@Override
	public Steam boil(Water water) {
		String temp = water.temperature();
		log.info("Boiling water {}", temp);
		delay();
		temp = "100°C";
		log.info("Boiling water {} done", temp);
		return new Steam(temp);
	}

	private static void delay() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
