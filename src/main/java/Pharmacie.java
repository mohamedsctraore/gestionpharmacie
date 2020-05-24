import java.util.InputMismatchException;
import java.util.Scanner;

class Pharmacie {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Client[]  clients = new Client[2];
        Medicament[]  medicaments = new Medicament[2];

        clients[0] = new Client("Malfichu",0.0);
        clients[1] = new Client("Palichon",0.0);

        medicaments[0] = new Medicament("Aspiron", 20.40, 5);
        medicaments[1] = new Medicament("Rhinoplexil",19.15, 5);
        int choix;

        do {
            choix = menu();

            switch (choix) {
                case 1 :
                    achat(clients, medicaments);
                    break;
                case 2 :
                    approvisionnement(medicaments);
                    break;
                case 3 :
                    affichage(clients, medicaments);
                    break;
                case 4:
                    quitter();
            }
        }
        while (choix < 4);
    }

    // Les méthodes utilitaires
    private static void affichage(Client[] clients, Medicament[] medicaments) {
        affichageStock(medicaments);
        affichageCredit(clients);
    }

    private static void approvisionnement(Medicament[] medicaments) {
        System.out.println("Donnez le nom du médicament?:");
        String clavier = scanner.nextLine();
        Medicament medicament = lireMedicament(medicaments, clavier);

        System.out.println("Donner la quantité:?");
        clavier = scanner.nextLine();

        approvisionner(medicament, Integer.parseInt(clavier));
    }

    private static void achat(Client[] clients, Medicament[] medicaments) {
        Client client;
        Medicament medicament;
        String clavier;

        System.out.println("Nom du client?:");
        do {
            clavier = scanner.nextLine();
            client = lireClient(clients, clavier);
        } while (client == null);


        System.out.println("Donnez le nom du medicament?:");
        do {
            clavier = scanner.nextLine();
            medicament = lireMedicament(medicaments, clavier);
        } while (medicament == null);


        System.out.println("Quelle est le montant du paiement?");
        clavier = scanner.nextLine();
        double montantVerse = Double.parseDouble(clavier);

        System.out.println("Quelle est la quantité achetée?");
        clavier = scanner.nextLine();
        int quantite = Integer.parseInt(clavier);

        effectuerAchat(client, medicament, montantVerse, quantite);
    }

    private static void quitter() {
        System.out.println("Programme terminé");
        System.exit(0);
    }

    public static Medicament lireMedicament(Medicament[] medicaments, String medicamentATester) {
        Medicament presenceMedicament;
        try {
            presenceMedicament = verifierPresenceMedicament(medicaments, medicamentATester);
        } catch (NullPointerException e) {
            System.out.println("Medicament inconnu. Veuillez recommencer");
            presenceMedicament = null;
        }
        return presenceMedicament;
    }

    public static Client lireClient(Client[] clients, String clientATester) {
        Client presenceClient;
        try {
            presenceClient = verifierPresenceClient(clients, clientATester);
        } catch (NullPointerException e) {
            System.out.println("Client inconnu. Veuillez recommencer");
            presenceClient = null;
        }
        return presenceClient;
    }

    private static int menu() {
        int choix = 0;
        System.out.println();
        System.out.println();
        System.out.println("1 : Achat de médicament");
        System.out.println("2 : Approvisionnement en  médicaments");
        System.out.println("3 : Etats des stocks et des crédits");
        System.out.println("4 : Quitter");
        while ((choix != 1) && (choix != 2) && (choix != 3) && (choix != 4)) {
            try {
                choix = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre compris entre 1 et 4");
            }

            clean();
        }
        return choix;
    }

    // Autres Methodes
    static void effectuerAchat(Client client, Medicament medicament,
                                       double montantVerse, int quantite) {
        double montantAPayer = medicament.getPrix() * quantite;

        try {
            medicament.miseAJourMedicament(quantite);
            client.miseAJourCredit(montantAPayer - montantVerse);
        } catch (RuntimeException e) {
            System.out.println("Achat impossible. Quantité insuffisante");
        }
    }

    static void approvisionner(Medicament medicament, int quantite) {
        medicament.ajouter(quantite);
    }

    static Medicament verifierPresenceMedicament(Medicament[] medicaments, String medicamentATester) {
        for (Medicament medicament: medicaments) {
            if (medicament.getNom().equals(medicamentATester)) {
                return medicament;
            }
        }
        throw new NullPointerException("Aucun medicament trouvé");
    }

    static Client verifierPresenceClient(Client[] clients, String clientATester) {
        for (Client client: clients) {
            if (client.getNom().equals(clientATester)) {
                return client;
            }
        }
        throw new NullPointerException("Aucun client n'a été trouvé");
    }

    private static void affichageStock(Medicament[] medicaments) {
        System.out.println("Affichage des stocks");
        for (Medicament medicament : medicaments) {
            System.out.println("Stock du médicament " + medicament.getNom() + " :" + medicament.getStock());
        }
    }

    private static void affichageCredit(Client[] clients) {
        System.out.println("Affichage des crédits");
        for (Client client : clients) {
            System.out.println("Crédit du client " + client.getNom() + " :" + client.getCredit());
        }
    }

    private static void clean() {
        scanner.nextLine();
    }
}

// Autres classes à compléter
class Client {
    private String nom;
    private double credit;

    Client(String nom, double credit) {
        this.nom = nom;
        this.credit = credit;
    }

    public void setNom(String nom) {
        if (nom.equals(""))
            throw new RuntimeException("Le nom ne doit pas être vide");
        this.nom = nom;
    }

    String getNom() {
        return this.nom;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    double getCredit() {
        return this.credit;
    }

    void miseAJourCredit(double montant) {
        this.credit += montant;
    }
}

class Medicament {
    private String nom;
    private double prix;
    private int stock;

    Medicament(String nom, double prix, int stock) {
        this.nom = nom;
        this.prix = prix;
        this.stock = stock;
    }

    public void setNom(String nom) {
        if (nom.equals(""))
            throw new RuntimeException("Le nom ne doit pas être vide");
        this.nom = nom;
    }

    String getNom() {
        return this.nom;
    }

    public void setPrix(double prix) {
        if (prix < 0)
            throw new RuntimeException("Le prix ne peut pas être inferieur à 0");
        this.prix = prix;
    }

    public double getPrix() {
        return this.prix;
    }

    public void setStock(int stock) {
        if (stock < 0)
            throw new RuntimeException("Le stock ne peut pas être inferieur à 0");
        this.stock = stock;
    }

    int getStock() {
        return this.stock;
    }

    void ajouter(int approvisionnement) {
        this.stock += approvisionnement;
    }

    public void miseAJourMedicament(int quantite) {
        if (this.stock - quantite < 0)
            throw new RuntimeException("Achat impossible quantité insuffisante");

        this.stock -= quantite;
    }
}