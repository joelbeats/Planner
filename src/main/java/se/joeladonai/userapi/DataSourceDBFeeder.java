package se.joeladonai.userapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import se.joeladonai.userapi.model.Issue;
import se.joeladonai.userapi.model.Team;
import se.joeladonai.userapi.model.User;
import se.joeladonai.userapi.model.WorkItem;
import se.joeladonai.userapi.repository.WorkItemRepository;
import se.joeladonai.userapi.service.IssueService;
import se.joeladonai.userapi.service.TeamService;
import se.joeladonai.userapi.service.UserService;
import se.joeladonai.userapi.service.WorkItemService;

@Component
public class DataSourceDBFeeder {

	@Autowired
	UserService userService;

	@Autowired
	TeamService teamService;

	@Autowired
	WorkItemService workItemService;

	@Autowired
	IssueService issueService;

	@Autowired
	WorkItemRepository workItemRepository;

	@Bean
	public String feedDB() throws Exception {

		Team team1 = teamService.createTeam(new Team("team1"));

		User u = userService.createUser(new User(810511L, "joelbeats001","secret", "Joel", "Hjelmstedt", team1));

		WorkItem w = workItemService.createWorkItem(new WorkItem("WI1", "Done"));
		w.setStatus(WorkItem.WorkItemStatus.DONE);
		workItemRepository.save(w);

		WorkItem w2 = workItemService.createWorkItem(new WorkItem("WI2", "Do it"));
		w2.setStatus(WorkItem.WorkItemStatus.DONE);
		workItemRepository.save(w2);

		userService.addWorkItemToUser(u.getId(), w.getId());

		issueService.createIssueAndAssignToWorkItem(new Issue("Done", "Awsome"), w.getId());

		return "Hej";
	}
}