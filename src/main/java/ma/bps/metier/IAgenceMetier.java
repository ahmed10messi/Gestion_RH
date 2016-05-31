package ma.bps.metier;

import java.util.List;

import ma.bps.entities.Agences;

public interface IAgenceMetier {
	public Agences ajouterAgence(Agences a);
	public Agences getAgenceById(Long idAgence);
	public List<Agences> getAllAgences();
	public List<Agences> getAllAgencesByBanque(Long idBanque);
	public Agences modifierAgence(Agences a);
	public void supprimerAgence(Long idAgence);
}
