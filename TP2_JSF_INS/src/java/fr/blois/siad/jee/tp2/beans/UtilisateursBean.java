package fr.blois.siad.jee.tp2.beans;

import fr.blois.siad.jee.tp2.dto.Utilisateur;
import fr.blois.siad.jee.tp2.services.UtilisateurService;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ManagedBean
@RequestScoped
public class UtilisateursBean {

    private String email;
    private String motDePasse;
    private String nom;

    private static final long serialVersionUID = 1L;

    public UtilisateursBean() {
    }

    public List<Utilisateur> getUtilisateurs() {
        return getService().listerTous();
    }

    private UtilisateurService getService() {
        try {
            UtilisateurService beanRemote = (UtilisateurService) new InitialContext().lookup("UtilisateurService");
            return beanRemote;
        } catch (NamingException ne) {
            System.err.println(ne);
        }
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {

    }

    public String checkAddUser() {

        // Validation
        FacesContext context = FacesContext.getCurrentInstance();
        if (email == null || email.isEmpty()) {
            context.addMessage(null, new FacesMessage("Email obligatoire"));
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            context.addMessage(null, new FacesMessage("MDP obligatoire"));
        }
        if (nom == null || nom.isEmpty()) {
            context.addMessage(null, new FacesMessage("Nom obligatoire"));
        }
        if (!context.getMessageList().isEmpty()) {
            return null;
        }

        // Cas nominal
        getService().ajouter(new Utilisateur(null, email, motDePasse, nom, new Date()));
        return "index";
    }
    
    public String supprimer(Integer id) {
        getService().supprimer(id);
        return "index";
    }
}
