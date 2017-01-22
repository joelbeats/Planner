# UserApi
Joel and Adonais project in the course Integration in JU15 Plushögskolan/Teknikhögskolan

# How to start in Console
1) Clone project  `git clone git@github.com:joelbeats/Planner.git`

2) Create a MYSQL -database called userapi at your host or localhost use: http://localhost:8080/

3) Type in your username and password for database-access in DataSourceConfig.java

3) In console change directory to project-directory

4) IN Console type `mvn spring-boot:run` 
OBS: The first time you run it it will download all the necessary files.

5) To create User, Team, Issue and WorkItem in the Database, download and install a REST Api Client 
such as PostMan from this URL: https://www.getpostman.com/

6) In the header always choose Content-Type : application/json.

7) To Create a User use a POST request to: http://{Your Host IP-adress}:{Port-number}/users ;

In the body type:

{
  "personalCode": "2000-01-01",
  "username": "firstname-lastname",
  "firstname": "Firstname",
  "lastname": "Lastname"
}
The Username must be atleast 10 characters long.
Now you have created an active User with the Id: 2. 
This you should get in the response body with the status-code: 201 Created.
As you can see in the body the user does not have a team yet which is now "null". 

8) To create a new team do a POST request to same host but this time put /teams instead of /users.
In the body type:

{
  "id": 2,
  "isActive": true,
  "name": "team2"
}
Now you have created an active Team with the Id: 2 with name: "team2". 

9) To assign a User to a team do a PUT request to the host with users/{userId}/team/{teamId} as an endpoint.
for example PUT -->  http://localhost:8080/users/2/team/2 which will put user with userId "2" in team "2".

