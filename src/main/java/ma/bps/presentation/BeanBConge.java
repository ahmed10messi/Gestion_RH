package ma.bps.presentation;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import ma.bps.entities.B_Anciennetes;
import ma.bps.entities.B_Conges;
import ma.bps.metier.B_CongeMetierImpl;
import ma.bps.metier.IB_CongeMetier;

@ManagedBean
@SessionScoped
public class BeanBConge {

	IB_CongeMetier bCongeMetier = new B_CongeMetierImpl();
	B_Conges b_conge = new B_Conges();
	private String msgSuccess = null;
	private int modification = 0;
	public String getMsgSuccess() {
		return msgSuccess;
	}
	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
	}
	public int getModification() {
		return modification;
	}
	public void setModification(int modification) {
		this.modification = modification;
	}
	
	public B_Conges getB_conge() {
		return b_conge;
	}
	public void setB_conge(B_Conges b_conge) {
		this.b_conge = b_conge;
	}

	public void ajouterBConge(ActionEvent e){
		System.out.println("ajout bareme du cong�!!!");
		this.setMsgSuccess("Ce bar�me a �t� ajout� avec succ�s !!!");
		System.out.println("b_cong� : " + this.b_conge.getTranche1());
		this.b_conge = bCongeMetier.ajouterB_Conge(this.b_conge);
		this.b_conge = new B_Conges();
	}
	
	public List<B_Conges> getAllBConge(){
		return bCongeMetier.getAllB_Conges();
	}
	
	public String editerBConge(B_Conges b_Conges){
		this.setMsgSuccess(null);
		this.setModification(1);
		this.b_conge = b_Conges;
		return "gestionBConge?faces-redirect=true";
	}
	
	public String modifierBAnciennete(){
		this.setModification(0);
		bCongeMetier.modifierB_Conge(this.b_conge);
		this.setMsgSuccess("Ce bar�me d'anciennet� a �t� modifi� avec succ�s !!!");
		this.b_conge = new B_Conges();
		return "gestionBConge?faces-redirect=true";
	}
	
	public void supprimerBConge(Long idBConge){
		bCongeMetier.supprimerB_Conge(idBConge);
		this.setModification(0);
		this.setMsgSuccess("Ce bar�me a �t� supprim� avec succ�s !!!");
	}
	
}
