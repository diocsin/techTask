package by.ilyushenko.tech.controller;

import by.ilyushenko.tech.model.ImportObject;
import by.ilyushenko.tech.service.ImportObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImportObjectController {

    private ImportObjectService service;

    @Autowired
    public ImportObjectController(ImportObjectService service) {
        this.service = service;
    }

    @GetMapping("/importobjects")
    public ResponseEntity<List<ImportObject>> findImportObject(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "filter", required = false) String filter) {
        List<ImportObject> importObjects;
        importObjects = service.findImportObject(offset, limit, filter);
        return ResponseEntity.ok(importObjects);
    }
}
