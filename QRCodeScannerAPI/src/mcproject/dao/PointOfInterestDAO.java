package mcproject.dao;

import java.util.List;

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
				.setParameter("title", title)
				.uniqueResult();
	}
	
	public List<PointOfInterest> getAllPOI()
	{
		return sessionFactory.getCurrentSession().createQuery("FROM PointOfInterest").list();
				
	}
	
	public PointOfInterest addNew(PointOfInterest poi)
	{
		sessionFactory.getCurrentSession().save(poi);
		return (PointOfInterest) sessionFactory.getCurrentSession().get(PointOfInterest.class, poi.getId());
	}
	
	public PointOfInterest del(PointOfInterest poi)
	{
		if(sessionFactory.getCurrentSession().createQuery("DELETE FROM PointOfInterest WHERE id=:id")
		.setParameter("id", poi.getId())
		.executeUpdate() > 0)
		{
			return null;
		}
		return poi;
	}
	
	public PointOfInterest edit(PointOfInterest newPoi)
	{
		PointOfInterest oldPoi = (PointOfInterest) sessionFactory.getCurrentSession().get(PointOfInterest.class, newPoi.getId());
		if(oldPoi != null)
		{
			oldPoi.setTitle(newPoi.getTitle());
			oldPoi.setDescription(newPoi.getDescription());
			sessionFactory.getCurrentSession().saveOrUpdate(oldPoi);
			return oldPoi;
		}
		return null;		
	}
	
}
