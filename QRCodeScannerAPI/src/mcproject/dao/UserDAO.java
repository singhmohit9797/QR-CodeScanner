package mcproject.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mcproject.model.User;

@Repository("userDAO")
@Transactional
public class UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public User getUser(User user)
	{
		return (User) sessionFactory.getCurrentSession().createQuery("FROM User WHERE email=:email AND password=:password")
				.setParameter("email", user.getEmail())
				.setParameter("password", user.getPassword())
				.uniqueResult();
	}
	
	public User userRegister(User user)
	{
		sessionFactory.getCurrentSession().save(user);
		return user;
	}
}
