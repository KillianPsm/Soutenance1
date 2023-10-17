package projet;

import ihm.Formulaire;
import ihm.FormulaireInt;

import java.util.ArrayList;

/**
 * Classe de definition de l'IHM de modification d'une commande
 */
public class GUIModifierCommande implements FormulaireInt {
    private Commande commande;

    /**
     * Constructeur
     * @param commande
     */
    public GUIModifierCommande(Commande commande) {
        this.commande = commande;

        // Créer un formulaire pour l'IHM de modification
        Formulaire form = new Formulaire("Modifier Commande", this, 400, 300);

        ArrayList<String> refProduits = commande.getReferences();
        for (String ref : refProduits) {
            String[] tab = ref.split("=");
            String nomRef = tab[0];
            form.addText(nomRef, nomRef, true, tab[1]);
        }

        // Ajouter un bouton "Valider" pour enregistrer les modifications
        form.addButton("VALIDER_MODIFICATIONS", "Valider");

        // Afficher le formulaire
        form.afficher();
    }

    /**
     *
     * @param form Le formulaire dans lequel se trouve le bouton
     * @param nomSubmit  Le nom du bouton qui a ete utilise.
     */
    public void submit(Formulaire form, String nomSubmit) {
        if (nomSubmit.equals("VALIDER_MODIFICATIONS")) {
            ArrayList<String> refProduitNew = new ArrayList<>();
            ArrayList<String> refProduits = commande.getReferences();

            for (String ref : refProduits) {
                String[] tab = ref.split("=");
                String nomRef = tab[0];
                String quantite = form.getValeurChamp(nomRef);
                String refP = nomRef + "=" + quantite;
                refProduitNew.add(refP);
            }

            commande.setReferences(refProduitNew);

            // Fermer la fenêtre de modification
            form.fermer();
        }
    }
}
