package ro.beenear.onlineshop.gatewayservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserServiceImpl implements UserService {

    private final WebClient webClient;

    public UserServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }
}
