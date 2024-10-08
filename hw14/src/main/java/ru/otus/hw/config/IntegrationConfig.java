package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.domain.Steam;
import ru.otus.hw.domain.Water;
import ru.otus.hw.services.H2OService;

@Configuration
public class IntegrationConfig {

	@Bean
	public MessageChannelSpec<?, ?> iceChannel() {
		return MessageChannels.queue(10);
	}

	@Bean
	public MessageChannelSpec<?, ?> steamChannel() {
		return MessageChannels.direct();
	}

	@Bean
	public IntegrationFlow h2OFlow(H2OService h2OService) {
		return IntegrationFlow.from(iceChannel())
				.split()
				.handle(h2OService, "melt")
				.<Water, Water>transform(w -> new Water("24°C - room temperature"))
				.aggregate()
				.handle(h2OService, "boil")
				.split()
				.transform(s -> new Steam((s + " - it's hot")))
				.aggregate()
				.channel(steamChannel())
				.get();
	}
}
