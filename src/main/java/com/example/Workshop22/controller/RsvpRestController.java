package com.example.Workshop22.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Workshop22.model.Rsvp;
import com.example.Workshop22.repository.RsvpRepository;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;


@RestController
@RequestMapping("/api")
public class RsvpRestController {

    @Autowired
    RsvpRepository rsvpRepository;
    
    @GetMapping(path="/rsvps", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRsvp() {

        List<Rsvp> rsvps = rsvpRepository.getAllRsvp();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Rsvp r : rsvps) {
            arrBuilder.add(r.toJsonObject());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("rsvps", arrBuilder)
                        .build().toString());
    }

    @GetMapping(path="/rsvp", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRsvpById(@RequestParam(value="q") String name) {
        
        List<Rsvp> rsvps = rsvpRepository.getRsvpByName(name);

        if(rsvps.isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Json.createObjectBuilder()
                    .add("message", "rsvp with name like %s not found".formatted(name))
                    .build().toString());
        }

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Rsvp r : rsvps) {
            arrBuilder.add(r.toJsonObject());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("rsvps", arrBuilder)
                        .build().toString());
    }

    // Because consumes = application/x-www-form-url
    // Spring does not recognize data in request body
    // Need to remove @RequestBody annotation from param
    // But somehow model will pick up data (Magikkkk)
    @PostMapping(path="/rsvp", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRsvp(Rsvp rsvp) {

        //TODO: validate rsvp is not null

        // rsvp = new Rsvp();
        // rsvp.setId(1);
        // rsvp.setName("Hello World");
        // rsvp.setEmail("good@bye.world");
        // rsvp.setPhone("12-345-67");
        // rsvp.setConfirmationDate(Date.valueOf("2000-05-01"));
        // rsvp.setComments(null);

        Integer updated = rsvpRepository.updateRsvp(rsvp);
        Integer inserted = 0;

        if (updated == 0) {
            inserted = rsvpRepository.insertRsvp(rsvp);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                            .add("update_count", updated)
                            .add("insert_count", inserted)
                            .build().toString());

    }

    // Because consumes = application/x-www-form-url
    // Spring does not recognize data in request body
    // Need to remove @RequestBody annotation from param
    // But somehow model will pick up data (Magikkkk)
    @PutMapping(path="/rsvp/{email}", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRsvpByEmail(Rsvp rsvp, @PathVariable String email) {
        
        Integer updated = rsvpRepository.updateRsvpByEmail(email, rsvp);

        if(updated <= 0) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                             .add("message", "email %s not found".formatted(email))
                             .build().toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("update_count", updated)
                        .build().toString());
    }

    @GetMapping(path="/rsvps/count")
    public ResponseEntity<String> getRsvpCount() {

        Integer count = rsvpRepository.getRsvpCount();

        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("count", count)
                            .build().toString());
    }

}
