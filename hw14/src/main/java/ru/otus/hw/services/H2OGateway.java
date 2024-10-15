package ru.otus.hw.services;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;

import java.util.Collection;

@MessagingGateway
public interface H2OGateway {

	@Gateway(requestChannel = "iceChannel", replyChannel = "steamChannel")
	Collection<Steam> process(Collection<Ice> iceCube);
}
