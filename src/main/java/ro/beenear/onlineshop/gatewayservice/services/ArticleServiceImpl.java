package ro.beenear.onlineshop.gatewayservice.services;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ro.beenear.onlineshop.articleservice.beans.ArticleDto;
import ro.beenear.onlineshop.inventoryservice.beans.LotDto;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final WebClient webClient;

    public ArticleServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<LotDto> findLotsByArticleId(Long articleId ){
        return webClient.get()
                .uri("http://localhost:8081/api/inventory/{articleId}",articleId)
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    @Override
    public Long saveNewArticle(ArticleDto articleDto) {

        ResponseEntity<Long> response = webClient.post()
                .uri("http://localhost:8090/api/article")
                .header("Autorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(articleDto)
                .retrieve()
                .toEntity(Long.class)
                .block();
        Long articleId= response.getBody();
//        articleDto.setId(articleId);
//        articleDto = new ArticleDto();
//        articleDto.setDescription(articleDto.getDescription());
//        articleDto.setImage(articleDto.getImage());
//        articleDto.setName(articleDto.getName());
//        articleDto.setPrice(articleDto.getPrice());
//        articleDto.setType(articleDto.getType());
//        articleDto.setUnitOfMeasure(articleDto.getUnitOfMeasure());
//        articleDto.setUrl(articleDto.getUrl());
        return articleId;
    }

    @Override
    public Long updateNewArticle(ArticleDto articleDto) {
        ResponseEntity<Long> response = webClient.put()
                .uri("http://localhost:8090/api/article/patch")
                .header("Autorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(articleDto)
                .retrieve()
                .toEntity(Long.class)
                .block();

        Long articleId= response.getBody();


        articleDto = new ArticleDto();
//        articleDto.setId(articleDto.getId());
//        articleDto.setDescription(articleDto.getDescription());
//        articleDto.setImage(articleDto.getImage());
//        articleDto.setName(articleDto.getName());
//        articleDto.setPrice(articleDto.getPrice());
//        articleDto.setType(articleDto.getType());
//        articleDto.setUnitOfMeasure(articleDto.getUnitOfMeasure());
//        articleDto.setUrl(articleDto.getUrl());
        return articleId;
    }

    @Override
    public Mono<Void> deleteById(Long articleId) {
        return webClient.delete()
                .uri("http://localhost:8090/api/article/{articleId}", articleId)
                .retrieve()
                .bodyToMono(Void.class);


    }
}

