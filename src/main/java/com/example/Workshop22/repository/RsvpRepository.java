package com.example.Workshop22.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.Workshop22.model.Rsvp;

@Repository
public class RsvpRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SQL_SELECT_ALL_FROM_RSVP = "SELECT * FROM rsvp";
    private final String SQL_SELECT_ALL_FROM_RSVP_LIKE_NAME = "SELECT * FROM rsvp WHERE name LIKE CONCAT('%', ?, '%') ";
    private final String SQL_INSERT_INTO_RSVP = "INSERT INTO rsvp (name, email, phone, confirmation_date, comments)  VALUES (?, ?, ?, ?, ?)";
    private final String SQL_UPDATE_RSVP_BY_ID = "UPDATE rsvp SET name = ?, email = ?, phone = ?, confirmation_date = ?, comments = ? WHERE id = ?";
    private final String SQL_SELECT_ALL_FROM_RSVP_BY_EMAIL = "SELECT * FROM rsvp WHERE email = ?";
    private final String SQL_GET_ROW_COUNT = "SELECT COUNT(*) FROM rsvp";
    private final String SQL_UPDATE_RSVP_BY_EMAIL = "UPDATE rsvp SET name = ?, email = ?, phone = ?, confirmation_date = ?, comments = ? WHERE email = ?";

    public List<Rsvp> getAllRsvp() {
        
        List<Rsvp> rsvps = new LinkedList<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ALL_FROM_RSVP);

        while(rs.next()){
            rsvps.add(Rsvp.fromSQL(rs));
        }
        return rsvps;
    }

    public List<Rsvp> getRsvpByName(String name) {

        // todo; Does this work?
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ALL_FROM_RSVP_LIKE_NAME, name);
        
        List<Rsvp> rsvps = new LinkedList<>();
        while (rs.next()){
            rsvps.add(Rsvp.fromSQL(rs));
        }
        return rsvps;
    }

    public Integer insertRsvp(Rsvp rsvp) {
        Integer added = jdbcTemplate.update(
                            SQL_INSERT_INTO_RSVP,
                            rsvp.getName(), rsvp.getEmail(), rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComments() );
        
        System.out.println("added --> " + added);

        return added;
    }

    public Integer updateRsvp(Rsvp rsvp) {
        Integer updated = jdbcTemplate.update(SQL_UPDATE_RSVP_BY_ID,
                                rsvp.getName(), rsvp.getEmail(), rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComments(), rsvp.getId());

        System.out.println("updated -->" + updated);

        return updated;
    }

    public List<Rsvp> getRsvpByEmail(String email) {

        List<Rsvp> rsvps = new LinkedList<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ALL_FROM_RSVP_BY_EMAIL, email);

        while (rs.next()) {
            rsvps.add(Rsvp.fromSQL(rs));
        }

        return rsvps;
    }

    public Integer getRsvpCount() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_ROW_COUNT);
        rs.next();
        Integer count = rs.getInt(1);
        
        return count;
    }

    public Integer updateRsvpByEmail(String email, Rsvp rsvp) {

        Integer updated = jdbcTemplate.update(SQL_UPDATE_RSVP_BY_EMAIL,
                                                rsvp.getName(), 
                                                rsvp.getEmail(), 
                                                rsvp.getPhone(), 
                                                rsvp.getConfirmationDate(), 
                                                rsvp.getComments(), 
                                                email);

        return updated;
    }

}
