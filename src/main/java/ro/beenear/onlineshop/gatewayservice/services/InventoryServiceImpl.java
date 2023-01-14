package ro.beenear.onlineshop.gatewayservice.services;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ro.beenear.onlineshop.inventoryservice.beans.LotDto;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final WebClient webClient;

    public InventoryServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public List<LotDto> findByArticleId(Long articleId) {
        return webClient.get()
                .uri("http://localhost:8081/inventories/{articleId}", articleId)
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NO_CONTENT), response -> Mono.error(new NoSuchElementException()))
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Throwable::new))
                .bodyToMono(List.class)
                .block();
    }

    @Override
    public List<LotDto> findAll() {
        return webClient.get()
                .uri("http://localhost:8081/inventories")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Throwable::new))
                .bodyToMono(List.class)
                .block();
    }

    @Override
    public ResponseEntity<Long> create(LotDto lotDto) {
        return webClient.post()
                .uri("http://localhost:8081/inventories")
                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(lotDto)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(InvalidDataAccessApiUsageException.class)
                        .flatMap(errorResponse -> Mono.error(new InvalidDataAccessApiUsageException(errorResponse.getMessage()))))
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(Throwable.class)
                        .flatMap(errorResponse -> Mono.error(new Throwable(errorResponse.getMessage()))))
                .toEntity(Long.class)
                .block();
    }

    @Override
    public ResponseEntity<Long> update(Long id, LotDto lotDto) {
        return webClient.put()
                .uri("http://localhost:8081/inventories/{id}", id)
                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(lotDto)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(InvalidDataAccessApiUsageException.class)
                        .flatMap(errorResponse -> Mono.error(new InvalidDataAccessApiUsageException(errorResponse.getMessage()))))

                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), response -> Mono.error(new NoSuchElementException()))

                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(Throwable.class)
                        .flatMap(errorResponse -> Mono.error(new Throwable(errorResponse.getMessage()))))
                .toEntity(Long.class)
                .block();
    }
}
