package ru.otus.hw.services;

import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;
import ru.otus.hw.domain.Water;

public interface H2OService {

	Water melt(Ice iceCube);

	Steam boil(Water pool);
}
