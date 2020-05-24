import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PharmacieTest {

    private Client[] clients;
    private Medicament[] medicaments;
    private Pharmacie1 pharmacie;

    @Before
    public void setup() {
        clients = new Client[2];
        medicaments = new Medicament[2];

        clients[0] = new Client("Malfichu",0.0);
        clients[1] = new Client("Palichon",0.0);

        medicaments[0] = new Medicament("Aspiron", 20.40, 5);
        medicaments[1] = new Medicament("Rhinoplexil",19.15, 5);

        pharmacie = new Pharmacie1();
        pharmacie.setClients(clients);
        pharmacie.setMedicaments(medicaments);
    }

    @Test(expected= NullPointerException.class)
    public void medicamentInexistantDoitEchouer() {
        Pharmacie.verifierPresenceMedicament(medicaments, "Approxil");
    }

    @Test(expected = NullPointerException.class)
    public void clientInexistantDoitEchouer() {
        Pharmacie.verifierPresenceClient(clients, "Thomas");
    }

    @Test(expected = RuntimeException.class)
    public void achatSuperierStockDisponibleDoitEchouer() {
        medicaments[0].miseAJourMedicament(10);
    }

    @Test
    public void medicamentInexistantRetourneNull() {
        Medicament medicament = Pharmacie.lireMedicament(medicaments, "Approxil");
        Assert.assertNull(medicament);
    }

    @Test
    public void clientInexistantRetourneNull() {
        Client client = Pharmacie.lireClient(clients, "Thomas");
        Assert.assertNull(client);
    }

    @Test
    public void testMedicamentCorrectementRetrouve() {
        Medicament medicament = Pharmacie.lireMedicament(medicaments, "Aspiron");
        Assert.assertNotNull(medicament);
        Assert.assertEquals(5, medicament.getStock());
        Assert.assertEquals(20.40, medicament.getPrix(), 0.01);
    }

    @Test
    public void testClientCorrectementRetrouve() {
        Client client = Pharmacie.lireClient(clients, "Malfichu");
        Assert.assertNotNull(client);
        Assert.assertEquals(0.0, client.getCredit(), 0.01);
    }

    @Test
    public void acheterDoitReduireStockMedicamentEtChangerCreditClient() {
        Pharmacie.effectuerAchat(clients[0], medicaments[0], 30.0, 3);
        Assert.assertEquals(medicaments[0].getStock(), 2);
        Assert.assertEquals(clients[0].getCredit(), 31.19, 0.01);
    }

    @Test
    public void approvisionnerMedicamentDoitAugmenterStock() {
        Pharmacie.approvisionner(medicaments[1], 10);
        Assert.assertEquals(medicaments[1].getStock(), 15);
    }

    @Test
    public void achatGestionPharmacieStockInsuffisantDoitEchouer() {
        pharmacie.achat("Palichon", "Rhinoplexil", 200, 10);
        Assert.assertEquals(medicaments[1].getStock(), 5);
        Assert.assertEquals(clients[1].getCredit(), 0.0, 0.01);
    }

    @Test
    public void achatGestionPharmacieDoitReduireStockMedicamentEtChangerCreditClient() {
        pharmacie.achat("Palichon", "Rhinoplexil", 200, 5);
        Assert.assertEquals(medicaments[1].getStock(), 0);
        Assert.assertEquals(clients[1].getCredit(), -104.25, 0.01);
    }

    @Test
    public void approvisionnementGestionPharmacieDoitAugmenterStockMedicament() {
        pharmacie.approvisionnement("Rhinoplexil", 5);
        Assert.assertEquals(medicaments[1].getStock(), 10);
    }
}
