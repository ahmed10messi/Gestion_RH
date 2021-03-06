package ma.bps.presentation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import ma.bps.entities.Conges;
import ma.bps.entities.Contrats;
import ma.bps.entities.Salaries;
import ma.bps.impression.AttestationConge;
import ma.bps.impression.AttestationTravail;
import ma.bps.metier.CongeMetierImpl;
import ma.bps.metier.DemandeDesCertificatsParSalarieMetierImpl;
import ma.bps.metier.ICongeMetier;
import ma.bps.metier.IDemandeDesCertificatsParSalarieMetier;
import ma.bps.metier.ISalarieMetier;
import ma.bps.metier.SalarieMetierImpl;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

@ManagedBean
@SessionScoped
public class BeanAdmin {
	
	// ce bean pour la gestion des admin et leurs taches
	
	IDemandeDesCertificatsParSalarieMetier metierDemande = new DemandeDesCertificatsParSalarieMetierImpl();
	
	ISalarieMetier metierSalarie = new SalarieMetierImpl();
	
	
	
	// imprimer une attestation de travail demander par un salarie
	public void imprimerAttestationTravail(Long idSalarie) throws JRException, IOException{
		
		Salaries s = metierSalarie.getSalarieById(idSalarie);
		
		Date dateDebutContrat = null;
		Collection<Contrats> listeContrat = s.getContrats();
		for (Contrats contrats : listeContrat) 
		{
			if (dateDebutContrat == null) 
			{
				dateDebutContrat = contrats.getDateDebutContrat();
			}
			else
			{
				if (dateDebutContrat.after(contrats.getDateDebutContrat())) 
				{
					dateDebutContrat = contrats.getDateDebutContrat();
				}
			}
		}
		
		System.out.println("********************************************" + dateDebutContrat);
		
		
		
		
//		System.out.println("**************************************************************");
//		
//		System.out.println(s.getSociete().getNomSociete());
//		System.out.println(s.getQualification().getPoste());
//		System.out.println(s.getCIN());
//		System.out.println(s.getCnss());
//		System.out.println(s.getNom() + "" + s.getPrenom());
//
//		
//		System.out.println("**************************************************************");
		
		
		
		
		// creation de l'objet d'impression
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		AttestationTravail attestation = new AttestationTravail();
		
		
		// nom societe
		if (s.getSociete() != null) 
		{
			attestation.setNomSociete(s.getSociete().getNomSociete());
		}
		else
		{
			attestation.setNomSociete("............................");
		}
		
		// nom prenom
		if (s.getNom() != null) 
		{
			if (s.getPrenom() != null) 
			{
				attestation.setNomPrenomSalarie(s.getNom() + " " + s.getPrenom());
			}
			else
			{
				attestation.setNomPrenomSalarie(s.getNom() + " ............... ");
			}
		}
		else
		{
			if (s.getPrenom() != null) 
			{
				attestation.setNomPrenomSalarie(" ............... " + s.getPrenom());
			}
			else
			{
				attestation.setNomPrenomSalarie(" .............................. ");
			}
		}
		

		// CIN
		if (!s.getCIN().equals("")) 
		{
			attestation.setCin(s.getCIN());
		}
		else
		{
			attestation.setCin("................");
		}		

		
		// cnss
		if (!s.getCnss().equals("")) 
		{
			attestation.setCnss(s.getCnss());
		}
		else
		{
			attestation.setCnss("................");
		}		

		
		// date debut contrat
		if (dateDebutContrat != null) 
		{
			attestation.setDateDebutContrat(formatter.format(dateDebutContrat));
		}
		else
		{
			attestation.setDateDebutContrat(".........................");
		}
		

		// date debut contrat
		if (s.getQualification() != null) 
		{
			attestation.setPoste(s.getQualification().getPoste());
		}
		else
		{
			attestation.setPoste("........................");
		}
		

		
		
		
		// hada ghi zayd ma khdamtch bih f impression
		Map<String,Object> parametre = new HashMap<String, Object>();
		parametre.put("z_nomSociete", "nomSociete");

		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/attestationTravail.jasper"));
		
		List<AttestationTravail> vide = new ArrayList<AttestationTravail>();
		
	
		vide.add(attestation);	
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametre,new JRBeanCollectionDataSource(vide));
		
		SimpleDateFormat forme = new SimpleDateFormat("dd-MM-yyyy");
		
	    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	    response.addHeader("Content-disposition","attachement; filename=AttestationTravail_" + attestation.getNomPrenomSalarie().replace(" ", "_") + "_" + forme.format(new Date()) +  ".pdf");
	    ServletOutputStream stream = response.getOutputStream();
	   
	    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
	    
	    stream.flush();
	    stream.close();

	    FacesContext.getCurrentInstance().responseComplete();
		
	}
	
	
	// imprimer une attestation de conge demander par un salarie	
	
	public void imprimerAttestationConge(Long idSalarie, Long idConge) throws JRException, IOException{
		
		
		ISalarieMetier metierSalarie = new SalarieMetierImpl();
		ICongeMetier metierConge = new CongeMetierImpl();
		
		Salaries s = metierSalarie.getSalarieById(idSalarie);
		Conges c = metierConge.getCongeById(idConge);
		
		
		
		
		// creation de l'objet d'impression
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		AttestationConge attestation = new AttestationConge();
		
		
		// nom societe
		if (s.getSociete() != null) 
		{
			attestation.setNomSociete(s.getSociete().getNomSociete());
		}
		else
		{
			attestation.setNomSociete("............................");
		}
		
		// nom prenom
		if (s.getNom() != null) 
		{
			if (s.getPrenom() != null) 
			{
				attestation.setNomPrenomSalarie(s.getNom() + " " + s.getPrenom());
			}
			else
			{
				attestation.setNomPrenomSalarie(s.getNom() + " ............... ");
			}
		}
		else
		{
			if (s.getPrenom() != null) 
			{
				attestation.setNomPrenomSalarie(" ............... " + s.getPrenom());
			}
			else
			{
				attestation.setNomPrenomSalarie(" .............................. ");
			}
		}
		

		// CIN
		if (!s.getCIN().equals("")) 
		{
			attestation.setCin(s.getCIN());
		}
		else
		{
			attestation.setCin("................");
		}		
	

		// date debut contrat
		if (s.getQualification() != null) 
		{
			attestation.setPoste(s.getQualification().getPoste());
		}
		else
		{
			attestation.setPoste("........................");
		}
		
		
		// date debut conge
		if (c.getDateDebutC() != null) 
		{
			attestation.setDateDebutConge(formatter.format(c.getDateDebutC()));
		} 
		else 
		{
			attestation.setDateDebutConge("........................");
		}
		

		
		// date fin conge
		if (c.getDateFinC() != null) 
		{
			attestation.setDateFinConge(formatter.format(c.getDateFinC()));
		} 
		else 
		{
			attestation.setDateFinConge("........................");
		}
		
		
		
		
		// hada ghi zayd ma khdamtch bih f impression
		Map<String,Object> parametre = new HashMap<String, Object>();
		parametre.put("z_nomSociete", "nomSociete");

		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/attestationConge.jasper"));
		
		List<AttestationConge> vide = new ArrayList<AttestationConge>();
		
	
		vide.add(attestation);	
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),parametre,new JRBeanCollectionDataSource(vide));
		
		SimpleDateFormat forme = new SimpleDateFormat("dd-MM-yyyy");
		
	    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	    response.addHeader("Content-disposition","attachement; filename=AttestationConge_" + attestation.getNomPrenomSalarie().replace(" ", "_") + "_" + forme.format(new Date()) +".pdf");
	    	    
	    ServletOutputStream stream = response.getOutputStream();
	   
	    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
	    
	    stream.flush();
	    stream.close();

	    FacesContext.getCurrentInstance().responseComplete();
		
		
		
		
		
		
		
		
	}
	
	

}
