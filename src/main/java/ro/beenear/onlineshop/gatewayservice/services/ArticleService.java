package ro.beenear.onlineshop.gatewayservice.services;

import reactor.core.publisher.Mono;
import ro.beenear.onlineshop.articleservice.beans.ArticleDto;
import ro.beenear.onlineshop.inventoryservice.beans.LotDto;
import java.util.List;

public interface ArticleService {
    List<LotDto> findLotsByArticleId(Long articleId );

    Long saveNewArticle(ArticleDto articleDto);

    Long updateNewArticle(ArticleDto articleDto);

    Mono<Void> deleteById(Long articleId);


}
