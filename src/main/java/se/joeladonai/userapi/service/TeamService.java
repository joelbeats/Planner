package se.joeladonai.userapi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import se.joeladonai.userapi.exception.RepositoryException;
import se.joeladonai.userapi.exception.ServiceException;
import se.joeladonai.userapi.model.Team;
import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.repository.TeamRepository;
import se.joeladonai.userapi.repository.UserRepository;

@Service
public class TeamService {

	private final TeamRepository teamRepository;
	private final UserRepository userRepository;

	@Autowired
	public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public Team createTeam(Team team) throws ServiceException, RepositoryException {

		try {
			return teamRepository.save(team);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	@Transactional
	public Team updateTeam(Long id, String name) throws ServiceException, RepositoryException {

		if (!teamRepository.exists(id)) {
			throw new ServiceException("Team with id: " + id + " doesn't exist");
		}

		try {
			Team team = teamRepository.findOne(id);
			team.setName(name);

			return teamRepository.save(team);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public Team getTeamById(Long id) throws ServiceException, RepositoryException {

		if (!teamRepository.exists(id)) {
			throw new ServiceException("Team with id: " + id + " doesn't exist");
		}

		try {
			return teamRepository.findOne(id);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	public List<Team> getAllTeams() throws ServiceException, RepositoryException {

		try {
			return teamRepository.findAll();

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	@Transactional
	public Team addUserToTeam(Long userId) throws ServiceException, RepositoryException {

		if (!userRepository.exists(userId)) {
			throw new ServiceException("User with id: " + userId + " doesn't exist");
		}

		if (!userRepository.findOne(userId).getIsActive()) {
			throw new ServiceException("Could not add user to team since it's inactive");
		}

		try {
			User user = userRepository.findOne(userId);

			for (Team team : teamRepository.findAll()) {
				if (userRepository.findByTeamId(team.getId()).size() < 10 && team.getIsActive()) {
					user.setTeam(team);
					userRepository.save(user);
					return team;
				}
			}
			Team newTeam = teamRepository.save(new Team("Team " + teamRepository.findAll().size() + 1));
			user.setTeam(newTeam);
			return newTeam;

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public Team inActivateTeam(Long id) throws ServiceException, RepositoryException {

		if (!teamRepository.exists(id)) {
			throw new ServiceException("Team with id: " + id + " doesn't exist");
		}

		try {
			Team team = teamRepository.findOne(id);
			team.setIsActive(false);
			return teamRepository.save(team);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

	@Transactional
	public Team activateTeam(Long id) throws ServiceException, RepositoryException {

		if (!teamRepository.exists(id)) {
			throw new ServiceException("Team with id: " + id + " doesn't exist");
		}

		try {
			Team team = teamRepository.findOne(id);
			team.setIsActive(true);
			return teamRepository.save(team);

		} catch (DataAccessException e) {
			throw new RepositoryException(e.getMessage());
		} catch (UnexpectedRollbackException e) {
			throw new RepositoryException(e.getMessage());
		}

	}

}
