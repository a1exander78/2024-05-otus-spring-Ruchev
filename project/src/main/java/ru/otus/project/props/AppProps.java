package ru.otus.project.props;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "app-props")
public class AppProps {
    private final int limitForBagsInHome;

    private final int limitForBagsInCart;

    @ConstructorBinding
    public AppProps(int limitForBagsInHome, int limitForBagsInCart) {
        this.limitForBagsInHome = limitForBagsInHome;
        this.limitForBagsInCart = limitForBagsInCart;
    }
}
