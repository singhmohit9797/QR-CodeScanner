package mcproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mcproject.dao.PointOfInterestDAO;
import mcproject.dao.UserDAO;
import mcproject.model.PointOfInterest;
import mcproject.model.User;

@Controller
@RequestMapping(value = "/api")
public class MainController {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PointOfInterestDAO pointOfInterestDAO;

	public PointOfInterestDAO getPointOfInterestDAO() {
		return pointOfInterestDAO;
	}

	public void setPointOfInterestDAO(PointOfInterestDAO pointOfInterestDAO) {
		this.pointOfInterestDAO = pointOfInterestDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public @ResponseBody User userSignUp(@RequestBody User user)
	{
		System.out.println("in sign up");
		return getUserDAO().userRegister(user);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody User validateUser(@RequestBody User user)
	{
		System.out.print("in validate user");
		return getUserDAO().getUser(user);
	}
	
	@RequestMapping(value = "/get/{title}", method = RequestMethod.GET)
	public @ResponseBody PointOfInterest getInfo(@PathVariable String title)
	{
		System.out.print("in getInfo");
		return getPointOfInterestDAO().getDescription(title);
	}
	
	/*public User validateCredential()
	{
		
	}*/
}
