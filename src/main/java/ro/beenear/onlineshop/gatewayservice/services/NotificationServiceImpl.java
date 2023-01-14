package ro.beenear.onlineshop.gatewayservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final WebClient webClient;

    public NotificationServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }
}
