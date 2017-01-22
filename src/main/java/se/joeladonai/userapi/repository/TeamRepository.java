package se.joeladonai.userapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import se.joeladonai.userapi.model.Team;


public interface TeamRepository extends CrudRepository<Team, Long> {

	List<Team> findAll();

}
