import java.util.InputMismatchException;
import java.util.Scanner;

public class GestionPharmacie {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String clavier;
        Client[]  clients = new Client[2];
        Medicament[]  medicaments = new Medicament[2];

        clients[0] = new Client("Malfichu",0.0);
        clients[1] = new Client("Palichon",0.0);

        medicaments[0] = new Medicament("Aspiron", 20.40, 5);
        medicaments[1] = new Medicament("Rhinoplexil",19.15, 5);

        Pharmacie1 pharmacie1 = new Pharmacie1();
        pharmacie1.setClients(clients);
        pharmacie1.setMedicaments(medicaments);

        int choix;

        do {
            choix = menu();

            switch (choix) {
                case 1 :
                    Client client;
                    Medicament medicament;

                    System.out.println("Nom du client?:");
                    do {
                        clavier = scanner.nextLine();
                        client = pharmacie1.lireClient(clavier);
                    } while (client == null);

                    System.out.println("Donnez le nom du medicament?:");
                    do {
                        clavier = scanner.nextLine();
                        medicament = pharmacie1.lireMedicament(clavier);
                    } while (medicament == null);

                    System.out.println("Quelle est le montant du paiement?");
                    clavier = scanner.nextLine();
                    double montantVerse = Double.parseDouble(clavier);

                    System.out.println("Quelle est la quantité achetée?");
                    clavier = scanner.nextLine();
                    int quantite = Integer.parseInt(clavier);

                    pharmacie1.achat(client.getNom(), medicament.getNom(), montantVerse, quantite);
                    break;
                case 2 :
                    System.out.println("Donnez le nom du médicament?:");
                    do {
                        clavier = scanner.nextLine();
                        medicament = pharmacie1.lireMedicament(clavier);
                    } while (medicament == null);

                    System.out.println("Donner la quantité:?");
                    clavier = scanner.nextLine();

                    pharmacie1.approvisionnement(medicament.getNom(), Integer.parseInt(clavier));
                    break;
                case 3 :
                    pharmacie1.affichage();
                    break;
                case 4:
                    pharmacie1.quitter();
            }
        }
        while (choix < 4);
    }

    static int menu() {
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

            scanner.nextLine();
        }
        return choix;
    }
}

class Pharmacie1 {
    private Client[] clients;
    private Medicament[] medicaments;

    void setClients(Client[] clients) {
        this.clients = clients;
    }

    void setMedicaments(Medicament[] medicaments) {
        this.medicaments = medicaments;
    }

    // Les méthodes utilitaires
    public void affichage() {
        affichageStock();
        affichageCredit();
    }

    public void approvisionnement(String medicamentATester, int quantite) {
        approvisionner(lireMedicament(medicamentATester), quantite);
    }

    public void achat(String client, String  medicament, double montant, int quantite) {
        effectuerAchat(lireClient(client), lireMedicament(medicament), montant, quantite);
    }

    void quitter() {
        System.out.println("Programme terminé");
        System.exit(0);
    }

    public Medicament lireMedicament(String medicamentATester) {
        Medicament presenceMedicament;
        try {
            presenceMedicament = verifierPresenceMedicament(medicamentATester);
        } catch (NullPointerException e) {
            System.out.println("Medicament inconnu. Veuillez recommencer");
            presenceMedicament = null;
        }
        return presenceMedicament;
    }

    public Client lireClient(String clientATester) {
        Client presenceClient;
        try {
            presenceClient = verifierPresenceClient(clientATester);
        } catch (NullPointerException e) {
            System.out.println("Client inconnu. Veuillez recommencer");
            presenceClient = null;
        }
        return presenceClient;
    }

    // Autres Methodes
    private void effectuerAchat(Client client, Medicament medicament, double montantVerse, int quantite) {
        double montantAPayer = medicament.getPrix() * quantite;

        try {
            medicament.miseAJourMedicament(quantite);
            client.miseAJourCredit(montantAPayer - montantVerse);
        } catch (RuntimeException e) {
            System.out.println("Achat impossible. Quantité insuffisante");
        }
    }

    private void approvisionner(Medicament medicament, int quantite) {
        medicament.ajouter(quantite);
    }

    private Medicament verifierPresenceMedicament(String medicamentATester) {
        for (Medicament medicament: this.medicaments) {
            if (medicament.getNom().equals(medicamentATester)) {
                return medicament;
            }
        }
        throw new NullPointerException("Aucun medicament trouvé");
    }

    private Client verifierPresenceClient(String clientATester) {
        for (Client client: this.clients) {
            if (client.getNom().equals(clientATester)) {
                return client;
            }
        }
        throw new NullPointerException("Aucun client n'a été trouvé");
    }

    private void affichageStock() {
        System.out.println("Affichage des stocks");
        for (Medicament medicament : this.medicaments) {
            System.out.println("Stock du médicament " + medicament.getNom() + " :" + medicament.getStock());
        }
    }

    private void affichageCredit() {
        System.out.println("Affichage des crédits");
        for (Client client : this.clients) {
            System.out.println("Crédit du client " + client.getNom() + " :" + client.getCredit());
        }
    }
}

