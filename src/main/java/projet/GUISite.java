package projet;

import ihm.Formulaire;
import ihm.FormulaireInt;

/**
 * Classe de definition de l'IHM principale du compte
 */
public class GUISite implements FormulaireInt {
    private Site site;  // Le site

    /**
     * Constructeur
     * @param site
     */
    public GUISite(Site site) {
        this.site = site;

        // Creation du formulaire
        Formulaire form = new Formulaire("Site de vente", this, 1100, 730);

        //  Creation des elements de l'IHM
        form.addLabel("Afficher tous les produits du stock");
        form.addButton("AFF_STOCK", "Tout le stock");
        form.addLabel("");
        form.addLabel("Afficher tous les bons de commande");
        form.addButton("AFF_COMMANDES", "Toutes les commandes");
        form.addLabel("");
        form.addText("NUM_COMMANDE", "Numero de commande", true, "1");
        form.addButton("AFF_COMMANDE", "Afficher");
        form.addLabel("");

        form.addButton("LIVRER_COMMANDES", "Livrer");
        form.addLabel("");

        form.addButton("MODIFIER", "Modifier");
        form.addLabel("");

        form.addButton("CALCULER_VENTES", "Calculer ventes");
        form.addLabel("");

        form.addButton("SAUVEGARDER", "Sauvegarder");
        form.addLabel("");

        form.addButton("FERMER", "Fermer");

        form.setPosition(400, 0);
        form.addZoneText("RESULTATS", "Resultats",
                true,
                "",
                600, 700);

        // Affichage du formulaire
        form.afficher();
    }

    /**
     * Methode appellee quand on clique dans un bouton
     * @param form Le formulaire dans lequel se trouve le bouton
     * @param nomSubmit  Le nom du bouton qui a ete utilise.
     */
    public void submit(Formulaire form, String nomSubmit) {

        // Affichage de tous les produits du stock
        if (nomSubmit.equals("AFF_STOCK")) {
            String res = site.listerTousProduits();
            form.setValeurChamp("RESULTATS", res);
        }

        // Affichage de toutes les commandes
        if (nomSubmit.equals("AFF_COMMANDES")) {
            String res = site.listerToutesCommandes();
            form.setValeurChamp("RESULTATS", res);
        }

        // Affichage d'une commande
        if (nomSubmit.equals("AFF_COMMANDE")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            int num = Integer.parseInt(numStr);
            String res = site.listerCommande(num);
            form.setValeurChamp("RESULTATS", res);
        }

        if (nomSubmit.equals("LIVRER_COMMANDES")) {
            String res = site.livrerCommandes();
            form.setValeurChamp("RESULTATS", res);
        }

        // Modifier une commande
        if (nomSubmit.equals("MODIFIER")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            int num = Integer.parseInt(numStr);
            GUIModifierCommande Modif = new GUIModifierCommande(site.chercherCommande(num));
        }

        // Calculer les ventes
        if (nomSubmit.equals("CALCULER_VENTES")) {
            // Calculer les ventes
            if (nomSubmit.equals("CALCULER_VENTES")) {
                String numStr = form.getValeurChamp("NUM_COMMANDE");
                int num = Integer.parseInt(numStr);
                double totalVentes = site.calculerVentes(num); // Utilisez la méthode du site pour calculer les ventes
                form.setValeurChamp("RESULTATS", "Total des ventes pour la commande " + num + ": " + totalVentes);
            }
        }


        // Sauvegarde des fichiers
        if (nomSubmit.equals("SAUVEGARDER")) {
            site.sauvegarderFichiers();
            form.setValeurChamp("RESULTATS", "Les produits et commandes ont bien été sauvegardés.");
        }

        // Fermer
        if (nomSubmit.equals("FERMER")) {
            form.fermer();
        }

    }

}
