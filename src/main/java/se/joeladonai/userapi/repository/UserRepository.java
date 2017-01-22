package se.joeladonai.userapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import se.joeladonai.userapi.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findByUsernameStartingWithAndFirstnameStartingWithAndLastnameStartingWith(String username,
			String firstname, String lastname);

	List<User> findByTeamId(Long teamId);

	User findByIdNumber(Long idNumber);
	
	User findByUsername(String username);
	
	

}
