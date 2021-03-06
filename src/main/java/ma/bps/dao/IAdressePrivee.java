package ma.bps.dao;

import java.util.List;

import ma.bps.entities.AdressesPrivee;
import ma.bps.entities.AdressesPro;

public interface IAdressePrivee {
	public AdressesPrivee ajouterAdressePrivee(AdressesPrivee a);
	public AdressesPrivee getAdressePriveeById(Long idAdressePrivee);
	public AdressesPrivee getAdressesPriveeBySalarie(Long idSalarie);
	public List<AdressesPrivee> getAllAdressesPrivee();
	public AdressesPrivee modifierAdressePrivee(AdressesPrivee a);
	public void supprimerAdressePrivee(Long idAdressePrivee);
}
