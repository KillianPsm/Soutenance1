package projet;

import util.Terminal;

import java.util.ArrayList;

/**
 * Classe de definition du site de vente
 */
public class Site {
    private ArrayList<Produit> stock;       // Les produits du stock
    private ArrayList<Commande> commandes;  // Les bons de commande

    /**
     * Constructeur
     */
    public Site() {
        stock = new ArrayList<Produit>();
        commandes = new ArrayList<Commande>();

        // lecture du fichier data/Produits.txt
        // pour chaque ligne on cree un Produit que l'on ajoute a stock
        initialiserStock("data/Produits.txt");

        // lecture du fichier data/Commandes.txt
        // pour chaque ligne on cree une commande ou on ajoute une reference
        // d'un produit a une commande existante.
        // AC AC
        initialiserCommandes("data/Commandes.txt");
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere tous les produits du stock
     * @return
     */
    public String listerTousProduits() {
        StringBuilder res = new StringBuilder();
        for (Produit prod : stock)
            res.append(prod.toString()).append("\n");

        return res.toString();
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere toutes les commandes
     * @return
     */
    public String listerToutesCommandes() {
        // Crée un objet StringBuilder pour construire la chaîne résultante de manière efficace
        StringBuilder res = new StringBuilder();

        // Initialise la variable "numero" à -1 pour le suivi du numéro de commande
        int numero = -1;

        // Parcours de la liste de commandes
        for (Commande commande : commandes) {
            // Vérifie si le numéro de commande a changé
            if (commande.getNumero() != numero) {
                // Ajoute une ligne de séparation entre les commandes (sauf pour la première)
                if (numero != -1) {
                    res.append("---------------------------------------------\n");
                }

                // Affiche les détails de la nouvelle commande (numéro, date, client)
                res.append("Commande        : ").append(commande.getNumero()).append("\n");
                numero = commande.getNumero();
                res.append("Date            : ").append(commande.getDate()).append("\n");
                res.append("Client          : ").append(commande.getClient()).append("\n");
                res.append("RefProduits     :\n");
            }

            // Parcours des références de produits associées à la commande
            for (String reference : commande.getReferences()) {
                res.append("   ").append(reference).append("\n");
            }
        }

        // Ajoute une ligne de séparation après la dernière commande si nécessaire
        if (numero != -1) {
            res.append("---------------------------------------------\n");
        }

        // Affiche la liste de commandes sur la console (peut être supprimée si non nécessaire)
        System.out.println(commandes);

        // Retourne la chaîne de caractères résultante représentant la liste des commandes
        return res.toString();
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere une commande
     * @param numero
     * @return
     */
    public String listerCommande(int numero) {
        // Crée un objet StringBuilder pour construire la chaîne résultante de manière efficace
        StringBuilder res = new StringBuilder();

        // Initialise la variable "currentNumero" à -1 pour le suivi du numéro de commande
        int currentNumero = -1;

        // Cette variable permettra de savoir si la commande a été trouvée
        boolean commandeTrouvee = false;

        // Parcours de la liste de commandes
        for (Commande commande : commandes) {
            // Vérifie si le numéro de commande a changé
            if (commande.getNumero() != currentNumero) {
                // Si le numéro de commande a changé et qu'on a déjà trouvé la commande recherchée, sort de la boucle
                if (currentNumero == numero) {
                    break;
                }
                currentNumero = commande.getNumero();
            }

            // Si le numéro de commande actuel correspond à celui recherché
            if (currentNumero == numero) {
                // Si la commande n'a pas encore été trouvée, affiche les détails de la commande
                if (!commandeTrouvee) {
                    res.append("Commande        : ").append(commande.getNumero()).append("\n");
                    res.append("Date            : ").append(commande.getDate()).append("\n");
                    res.append("Client          : ").append(commande.getClient()).append("\n");
                    res.append("RefProduits     :\n");
                    commandeTrouvee = true;
                }

                // Parcours des références de produits associées à la commande
                for (String reference : commande.getReferences()) {
                    res.append("   ").append(reference).append("\n");
                }
            }
        }

        // Si la commande n'a pas été trouvée, met à jour la chaîne résultante pour indiquer qu'elle n'a pas été trouvée
        if (!commandeTrouvee) {
            res = new StringBuilder("Cette commande n'a pas été trouvée.");
        }

        // Retourne la chaîne de caractères résultante représentant la commande recherchée ou le message d'absence
        return res.toString();
    }

    /**
     * Methode de recherche d'une commande
     * @param num
     * @return
     */
    public Commande chercherCommande(int num) {
        // Parcours de la liste de commandes
        for (Commande commande : commandes) {
            // Vérifie si le numéro de la commande actuelle correspond au numéro recherché
            if (commande.getNumero() == num) {
                // Si la correspondance est trouvée, renvoie l'objet Commande
                return commande;
            }
        }
        // Si aucune correspondance n'est trouvée, renvoie null
        return null;
    }

    /**
     * Methode d'initialisation du stock
     * @param nomFichier
     */
    private void initialiserStock(String nomFichier) {
        String[] lignes = Terminal.lireFichierTexte(nomFichier);
        for (String ligne : lignes) {
            String[] champs = ligne.split("[;]", 4);
            String reference = champs[0];
            String nom = champs[1];
            double prix = Double.parseDouble(champs[2]);
            int quantite = Integer.parseInt(champs[3]);
            Produit p = new Produit(reference,
                    nom,
                    prix,
                    quantite
            );
            stock.add(p);
        }
    }

    /**
     * Methode d'initialisation du stock
     * @param nomFichier
     */
    private void initialiserCommandes(String nomFichier) {
        // Lire le contenu du fichier texte et stocker chaque ligne dans un tableau de chaînes (lignes)
        String[] lignes = Terminal.lireFichierTexte(nomFichier);

        // Variables de suivi des numéros de commande
        int numCourant;
        int numPrecedent = -1;

        // Liste pour stocker les références de produits
        ArrayList<String> references = new ArrayList<>();

        // Objet Commande pour stocker les détails de chaque commande
        Commande commande;

        // Parcours de chaque ligne du fichier
        for (String ligne : lignes) {
            // Divise chaque ligne en champs en utilisant le délimiteur ";"
            String[] champs = ligne.split(";", 4);

            // Récupère le numéro de commande, la date et le nom du client à partir des champs
            numCourant = Integer.parseInt(champs[0]);
            String date = champs[1];
            String nom = champs[2];

            // Si le numéro de commande actuel est différent du précédent, cela signifie une nouvelle commande
            if (numCourant != numPrecedent) {
                // Réinitialise la liste des références de produits
                references = new ArrayList<>();

                // Crée un nouvel objet Commande et l'ajoute à la liste des commandes
                commande = new Commande(numCourant, date, nom, references);
                commandes.add(commande);
            }

            // Ajoute la référence de produit à la liste de références pour la commande actuelle
            references.add(champs[3]);

            // Met à jour le numéro de commande précédent pour la prochaine itération
            numPrecedent = numCourant;
        }
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere une commande non livree
     * @return
     */
    public String livrerCommandes() {
        // Initialise la chaîne de résultat avec un en-tête
        String res = "Les commandes non livrées :\n"
                + "============================\n";

        // Parcours de chaque commande dans la liste de commandes
        for (Commande commande : commandes) {
            // Vérifie si la commande n'a pas déjà été livrée
            if (!commande.isLivree()) {
                boolean aLivrer = true;
                int quantitesCommandees = 0;

                // Parcours des références de produits dans la commande
                for (String reference : commande.getReferences()) {
                    String[] tab = reference.split("=");
                    String nomRef = tab[0];
                    String qteComm = tab[1];
                    quantitesCommandees = Integer.parseInt(qteComm);

                    // Parcours des produits en stock
                    for (Produit p : stock) {
                        if (nomRef.equals(p.getReference())) {
                            if (p.getQuantite() < quantitesCommandees) {
                                // S'il n'y a pas suffisamment de stock, calcule la quantité manquante
                                int manque = quantitesCommandees - p.getQuantite();
                                res += "Commande : " + commande.getNumero() + ", il manque : " + manque + " " + p.getReference() + '\n';
                                aLivrer = false;
                            }
                        }
                    }
                }

                if (aLivrer) {
                    // Si tous les produits sont en stock, décrémente les quantités en stock
                    for (String ref : commande.getReferences()) {
                        for (Produit p : stock) {
                            if (p.getReference().equals(ref)) {
                                String[] tab = ref.split("=");
                                int quantiteCommande = Integer.parseInt(tab[1]);
                                int quantiteStock = p.getQuantite();
                                int quantiteRestante = quantiteStock - quantiteCommande;
                                p.setQuantite(quantiteRestante);
                            }
                        }
                    }
                    // Marque la commande comme livrée
                    commande.setLivree(true);
                }
            }
        }
        return res;
    }

    /**
     * Methode qui calcule la somme des ventes d'une commande
     * @param numCommande
     * @return
     */
    public double calculerVentes(int numCommande) {
        double totalVentes = 0.0; // Initialisation de la somme des ventes à zéro.

        if (numCommande > 0) {
            for (Commande commmade : commandes) {
                if (numCommande == commmade.getNumero()) {
                    for (String ref : commmade.getReferences()) {
                        for (Produit produit : stock) {
                            if (produit.getReference().equals(ref)) {
                                String[] tab = ref.split("=");
                                int quantiteCommande = Integer.parseInt(tab[1]);
                                double prix = produit.getPrix() * quantiteCommande;
                                totalVentes += prix;
                            }
                        }
                    }
                }
            }
        }
        // Renvoie le montant total des ventes pour la commande spécifiée.
        return totalVentes;
    }

    /**
     * Methode qui sauvegarde les donnees
     */
    public void sauvegarderFichiers() {
        // Initialise deux objets StringBuffer pour stocker les données des produits et des commandes
        StringBuffer sbP = new StringBuffer();
        StringBuffer sbC = new StringBuffer();

        // Parcours de la liste de produits (stock)
        for (Produit produit : stock) {
            // Ajoute les détails de chaque produit au StringBuffer sbP, séparés par des points-virgules
            sbP.append(produit.getReference()).append(";");
            sbP.append(produit.getNom()).append(";");
            sbP.append(produit.getPrix()).append(";");
            sbP.append(produit.getQuantite()).append("\n");
        }

        // Parcours de la liste de commandes
        for (Commande commande : commandes) {
            // Parcours des références de produits associées à chaque commande
            for (String ref : commande.getReferences()) {
                // Ajoute les détails de chaque commande et référence de produit associée au StringBuffer sbC
                sbC.append(commande.getNumero()).append(";");
                sbC.append(commande.getDate()).append(";");
                sbC.append(commande.getClient()).append(";");
                sbC.append(ref).append("\n");
            }
        }

        // Écrit les données des produits dans un fichier texte nommé "Produits.txt"
        Terminal.ecrireFichier("data/Produits.txt", sbP);

        // Écrit les données des commandes dans un fichier texte nommé "Commandes.txt"
        Terminal.ecrireFichier("data/Commandes.txt", sbC);
    }
}