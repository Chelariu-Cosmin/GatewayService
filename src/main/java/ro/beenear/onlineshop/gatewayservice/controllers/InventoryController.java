package ro.beenear.onlineshop.gatewayservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ro.beenear.onlineshop.gatewayservice.services.InventoryService;
import ro.beenear.onlineshop.inventoryservice.beans.LotDto;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping ("/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    /**
     * Saves a given article.
     *
     * @param lotDto - must not be null.
     * @return the saved entity and 201 Created response status
     * when the request succeeded,
     * INTERNAL_SERVER_ERROR (500) when the server has encountered
     * an internal error and was unable to complete the request.
     * BAD_REQUEST (400) status if the server will
     * not process with an invalid object received from web client
     */
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody LotDto lotDto) {

        try {
            return inventoryService.create(lotDto);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid data access for inventory lots: %s", lotDto));
        } catch (Throwable throwable) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server has encountered " +
                    "an internal error and was unable to complete the request.", throwable);
        }
    }

    /**
     * @param articleId cannot be null
     * @return lotDto and 200 OK response status if the id from an entity exists,
     * get all lots by articleId
     * otherwise will return 204 No Content,
     * Return 400 Bad Request if the server will
     * not process with an invalid path variable
     * Return 500 Internal Server Error, this indicates the server has encountered
     * an internal error and was unable to complete the request.
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<List<LotDto>> findByArticleId(@PathVariable("articleId") Long articleId) {

        try {
            List<LotDto> list = inventoryService.findByArticleId(articleId);

            return list.isEmpty() ?
                    ResponseEntity.noContent().build() :
                    ResponseEntity.ok(list);
        } catch (Throwable throwable) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server has encountered an internal error" +
                    " and was unable to complete the request.", throwable);
        }
    }

    /**
     * Update entity with the given ID
     *
     * @param id         cannot be null
     * @param lotDto - provided data
     * @return articleId and 200 status ok if the entity with a given id already exists
     * null, 404 Not Found otherwise.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long id,
                                              @RequestBody LotDto lotDto) {

        try {
            return inventoryService.update(id, lotDto);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (Throwable exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    /**
     * @return all entities response status 200 OK,
     * If the list is empty but everything works fine, will return also 200 OK
     * 500 InternalServer Error if the server has encountered
     * an internal error and was unable to complete the request.
     */
    @GetMapping
    public ResponseEntity<List<LotDto>> findAll() {

        try {
            List<LotDto> list = inventoryService.findAll();
            if (list.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No lots found");
            }
            return new ResponseEntity<>(inventoryService.findAll(), HttpStatus.OK);
        } catch (Throwable exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
