package se.joeladonai.userapi.repository;

import org.springframework.data.repository.CrudRepository;

import se.joeladonai.userapi.model.Issue;

public interface IssueRepository extends CrudRepository<Issue, Long> {

}