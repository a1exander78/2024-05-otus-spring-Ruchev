package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IceServiceImpl implements IceService {
	private static final String[] TEMPS = {"-15°C", "-13°C", "-17°C", "-21°C", "-18°C", "-10°C", "-5°C"};

	private final H2OGateway h2OGateway;

	public IceServiceImpl(H2OGateway h2OGateway) {
		this.h2OGateway = h2OGateway;
	}

	@Override
	public void startGenerateIceCubes() {
		ForkJoinPool pool = ForkJoinPool.commonPool();
		for (int i = 0; i < 5; i++) {
			int num = i + 1;
			pool.execute(() -> {
				Collection<Ice> iceCubes = generateIceCubes();
				log.info("{}, New iceCubes: {}", num,
						iceCubes.stream().map(Ice::temperature)
								.collect(Collectors.joining(",")));
				Collection<Steam> steam = h2OGateway.process(iceCubes);
				log.info("{}, Steam is ready: {}", "IceCube #" + num + " transformed", steam.stream()
						.map(Steam::temperature)
						.collect(Collectors.joining(",")));
			});
			delay();
		}
	}

	private static Ice generateIceCube() {
		return new Ice(TEMPS[RandomUtils.nextInt(0, TEMPS.length)]);
	}

	private static Collection<Ice> generateIceCubes() {
		List<Ice> iceCubes = new ArrayList<>();
		for (int i = 0; i < RandomUtils.nextInt(1, 5); ++i) {
			iceCubes.add(generateIceCube());
		}
		return iceCubes;
	}

	private void delay() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
