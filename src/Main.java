import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static Map<String, Medicament> medicaments = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Absorber le saut de ligne

            switch (choix) {
                case 1:
                    ajouterMedicament(scanner);
                    break;
                case 2:
                    supprimerMedicament(scanner);
                    break;
                case 3:
                    rechercherMedicament(scanner);
                    break;
                case 4:
                    afficherParType();
                    break;
                case 5:
                    modifierMedicament(scanner);
                    break;
                case 6:
                    afficherListeParLettre(scanner);
                    break;
                case 7:
                    afficherNombreMedicaments();
                    break;
                case 8:
                    // Ajouter d'autres fonctionnalités ici
                    break;
                case 9:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static final String FICHIER_JSON = "medicaments.json";

    private static Map<String, Medicament> Medicaments = new HashMap<>();

    private static void chargerMedicaments() {
        try (FileReader reader = new FileReader(FICHIER_JSON)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray medicamentsArray = jsonObject.getAsJsonArray("medicaments");

            for (int i = 0; i < medicamentsArray.size(); i++) {
                JsonObject medicamentObject = medicamentsArray.get(i).getAsJsonObject();
                String type = medicamentObject.get("type").getAsString();
                String nom = medicamentObject.get("nom").getAsString();
                double prix = medicamentObject.get("prix").getAsDouble();
                int quantiteEnStock = medicamentObject.get("quantiteEnStock").getAsInt();
                String identifiant = medicamentObject.get("identifiant").getAsString();

                Medicament medicament;
                if (type.equals("MedicamentSurOrdonnance")) {
                    String ordonnance = medicamentObject.get("ordonnance").getAsString();
                    medicament = new MedicamentSurOrdonnance(nom, prix, quantiteEnStock, identifiant, ordonnance);
                } else {
                    medicament = new MedicamentVenteLibre(nom, prix, quantiteEnStock, identifiant);
                }

                medicaments.put(identifiant, medicament);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des médicaments : " + e.getMessage());
        }
    }

    private static void sauvegarderMedicaments() {
        JsonObject jsonObject = new JsonObject();
        JsonArray medicamentsArray = new JsonArray();

        for (Medicament medicament : medicaments.values()) {
            JsonObject medicamentObject = new JsonObject();
            medicamentObject.addProperty("type", medicament.getClass().getSimpleName());
            medicamentObject.addProperty("nom", medicament.getNom());
            medicamentObject.addProperty("prix", medicament.getPrix());
            medicamentObject.addProperty("quantiteEnStock", medicament.getQuantiteEnStock());
            medicamentObject.addProperty("identifiant", medicament.getIdentifiant());

            if (medicament instanceof MedicamentSurOrdonnance) {
                medicamentObject.addProperty("ordonnance", ((MedicamentSurOrdonnance) medicament).getOrdonnance());
            }

            medicamentsArray.add(medicamentObject);
        }

        jsonObject.add("medicaments", medicamentsArray);

        try (FileWriter writer = new FileWriter(FICHIER_JSON)) {
            Gson gson = new Gson();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des médicaments : " + e.getMessage());
        }
    }

    private static void ajouterMedicament(Scanner scanner) {
        // Code existant
        medicaments.put(identifiant, medicament);
        sauvegarderMedicaments();
        System.out.println("Médicament ajouté avec succès.");
    }

    private static void supprimerMedicament(Scanner scanner) {
        // Code existant
        medicaments.remove(identifiant);
        sauvegarderMedicaments();
        System.out.println("Médicament supprimé avec succès.");
    }

    private static void modifierMedicament(Scanner scanner) {
        // Code existant
        sauvegarderMedicaments();
        System.out.println("Médicament modifié avec succès.");
    }

    private static void initialiser() {
        chargerMedicaments();
    }
}