package fr.blois.siad.jee.tp2.beans;

import fr.blois.siad.jee.tp3.entities.UtilisateurEntity;
import fr.blois.siad.jee.tp3.entities.dto.Utilisateur;
import fr.blois.siad.jee.tp3.services.UtilisateurService;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
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
    
    public String changerMdp() {
        List<Utilisateur> users = getService().listerTous();
        users.removeIf(new Predicate<Utilisateur>(){
            @Override
            public boolean test(Utilisateur t) {
                return !t.getNom().equals(getNom());
            }
        });
        if (users.size() == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Utilisateur inconnu!! >:["));
            return null;
        } else {
            users.get(0).setMotDePasse(getMotDePasse());
            return "index";
        }
    }

    public String checkAddUser() {
        
        List<Utilisateur> users = getService().listerTous();

        // Validation
        FacesContext context = FacesContext.getCurrentInstance();
        if (email == null || email.isEmpty()) {
            context.addMessage(null, new FacesMessage("Email obligatoire"));
        } else if (!email.matches("([A-z]|\\.|[0-9])*@[A-z]*\\.(com|fr|net)")) {
            context.addMessage(null, new FacesMessage("Ton email est faux petit galopin."));
        } else {
            boolean found = false;
            for (Utilisateur u : users)
                if (u.getEmail().equals(email)){
                    found=true;
                    break;
                }
            if (found) context.addMessage(null, new FacesMessage("IMPOSTEUR quelqu'un a déjà ton email."));
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            context.addMessage(null, new FacesMessage("MDP obligatoire"));
        } else if (motDePasse.length() < 8) {
            context.addMessage(null, new FacesMessage("Ton mot de passe est trop petit."));
        }
        if (nom == null || nom.isEmpty()) {
            context.addMessage(null, new FacesMessage("Nom obligatoire"));
        } else {
            boolean found = false;
            for (Utilisateur u : users)
                if (u.getNom().equals(nom)){
                    found=true;
                    break;
                }
            if (found) context.addMessage(null, new FacesMessage("IMPOSTEUR quelqu'un a déjà ton nom."));
        }
        if (!context.getMessageList().isEmpty()) {
            return null;
        }

        // Cas nominal
        getService().ajouter(new UtilisateurEntity(email, motDePasse, nom, new Date(), true));
        return "index";
    }
    
    public String bloquer(Integer id) {
        getService().bloquer(id);
        return "index";
    }
    
    public String debloquer(Integer id) {
        getService().debloquer(id);
        return "index";
    }
    
    public String supprimer(Integer id) {
        getService().supprimer(id);
        return "index";
    }
}
