package ro.beenear.onlineshop.gatewayservice.services;

import org.springframework.http.ResponseEntity;
import ro.beenear.onlineshop.inventoryservice.beans.LotDto;

import java.util.List;


public interface InventoryService {

    List<LotDto> findByArticleId(Long articleId);

    List<LotDto> findAll();

    ResponseEntity<Long> create(LotDto lotDto);
    ResponseEntity<Long> update(Long id, LotDto lotDto);

}
