package mcproject.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mcproject.model.PointOfInterest;

@Repository("pointofinterestDAO")
@Transactional
public class PointOfInterestDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public PointOfInterest getDescription(String title)
	{
		return (PointOfInterest) sessionFactory.getCurrentSession().createQuery("FROM PointOfInterest WHERE title=:title")
				.setParameter("title", title).uniqueResult();
	}
	
}
