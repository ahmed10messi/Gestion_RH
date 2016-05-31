package ma.bps.dao;

import java.util.Date;
import java.util.List;

import ma.bps.entities.PlanTravail;
import ma.bps.entities.PointageDatePasse;

public interface IPointageDatePasse {

	public void ajouterPointageDatePasse(PointageDatePasse p);
	public PointageDatePasse getPointageDatePasseById(Long idPointage);
	public List<PointageDatePasse> getAllPointageDatePasse();
	public PointageDatePasse modifierPointageDatePasse(PointageDatePasse p);
	public void supprimerPointageDatePasse(Long idPointage);
	public boolean getPointageDatePasseByDate(Date datePointage);
	
}
