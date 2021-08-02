package by.ilyushenko.tech.controller;

import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.service.ImportObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImportObjectController {

    private final ImportObjectService importObjectService;

    @Autowired
    public ImportObjectController(ImportObjectService service) {
        this.importObjectService = service;
    }

    @GetMapping("/importobjects")
    public ResponseEntity<?> findImportObject(
            @RequestParam(value = "offset", defaultValue = "0") final int offset,
            @RequestParam(value = "limit", defaultValue = "100") final int limit,
            @RequestParam(value = "filter", required = false) final String filter) {
        try {
            final List<ImportObject> importObjects = importObjectService.findImportObject(offset, limit, filter);
            return ResponseEntity.ok(importObjects);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching data", HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
