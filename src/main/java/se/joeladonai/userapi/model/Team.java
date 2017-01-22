package se.joeladonai.userapi.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Team extends AbstractEntity {

	private String name;

	public Team(String name) {
		this.name = name;

	}

	protected Team() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
