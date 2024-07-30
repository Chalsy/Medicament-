import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Map<String, Medicament> medicaments = new HashMap<>();

    private static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacie", "root", "");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void afficherMenu() {
        System.out.println("Bienvenue dans la gestion de la pharmacie :");
        System.out.println("1. Ajouter un médicament");
        System.out.println("2. Supprimer un médicament");
        System.out.println("3. Rechercher un médicament");
        System.out.println("4. Afficher la liste des médicaments par type");
        System.out.println("5. Modifier un médicament");
        System.out.println("6. Afficher la liste des médicaments par lettre");
        System.out.println("7. Afficher le nombre de médicaments en stock");
        System.out.println("8. Autres fonctionnalités");
        System.out.println("9. Quitter");
        System.out.print("Votre choix : ");
    }

    private static void ajouterMedicament(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Entrez le nom du médicament : ");
            String nom = scanner.nextLine();
            System.out.print("Entrez le prix du médicament : ");
            double prix = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Entrez la quantité en stock : ");
            int quantiteEnStock = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Entrez l'identifiant du médicament : ");
            String identifiant = scanner.nextLine();

            System.out.print("Le médicament est-il sur ordonnance ? (oui/non) : ");
            String reponse = scanner.nextLine();

            Medicament medicament;
            if (reponse.equalsIgnoreCase("oui")) {
                System.out.print("Entrez l'ordonnance : ");
                String ordonnance = scanner.nextLine();
                medicament = new MedicamentSurOrdonnance(nom, prix, quantiteEnStock, identifiant, ordonnance);
            } else {
                medicament = new MedicamentVenteLibre(nom, prix, quantiteEnStock, identifiant);
            }

            String sql = "INSERT INTO medicaments (nom, prix, quantiteEnStock, identifiant, type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, medicament.getNom());
            statement.setDouble(2, medicament.getPrix());
            statement.setInt(3, medicament.getQuantiteEnStock());
            statement.setString(4, medicament.getIdentifiant());
            statement.setString(5, medicament.getType());
            statement.executeUpdate();

            System.out.println("Médicament ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void supprimerMedicament(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Entrez l'identifiant du médicament à supprimer : ");
            String identifiant = scanner.nextLine();

            String sql = "DELETE FROM medicaments WHERE identifiant = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identifiant);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Médicament supprimé avec succès.");
            } else {
                System.out.println("Médicament non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void rechercherMedicament(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Rechercher par nom ou identifiant ? (nom/identifiant) : ");
            String choix = scanner.nextLine();

            if (choix.equalsIgnoreCase("nom")) {
                System.out.print("Entrez le nom du médicament : ");
                String nom = scanner.nextLine();
                String sql = "SELECT * FROM medicaments WHERE nom LIKE ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + nom + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Medicament medicament = creerMedicament(resultSet);
                    afficherMedicament(medicament);
                }
            } else if (choix.equalsIgnoreCase("identifiant")) {
                System.out.print("Entrez l'identifiant du médicament : ");
                String identifiant = scanner.nextLine();
                String sql = "SELECT * FROM medicaments WHERE identifiant = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, identifiant);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Medicament medicament = creerMedicament(resultSet);
                    afficherMedicament(medicament);
                } else {
                    System.out.println("Médicament non trouvé.");
                }
            } else {
                System.out.println("Choix invalide.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afficherParType() {
        try (Connection connection = getConnection()) {
            System.out.println("Médicaments sur ordonnance :");
            String sql = "SELECT * FROM medicaments WHERE type = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "MedicamentSurOrdonnance");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Medicament medicament = creerMedicament(resultSet);
                afficherMedicament(medicament);
            }

            System.out.println("\nMédicaments en vente libre :");
            statement.setString(1, "MedicamentVenteLibre");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Medicament medicament = creerMedicament(resultSet);
                afficherMedicament(medicament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void modifierMedicament(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Entrez l'identifiant du médicament à modifier : ");
            String identifiant = scanner.nextLine();

            String sql = "SELECT * FROM medicaments WHERE identifiant = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identifiant);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Medicament medicament = creerMedicament(resultSet);
                System.out.print("Entrez le nouveau nom du médicament : ");
                String nouveauNom = scanner.nextLine();
                System.out.print("Entrez le nouveau prix du médicament : ");
                double nouveauPrix = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Entrez la nouvelle quantité en stock : ");
                int nouvelleQuantite = scanner.nextInt();
                scanner.nextLine();

                sql = "UPDATE medicaments SET nom = ?, prix = ?, quantiteEnStock = ? WHERE identifiant = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, nouveauNom);
                statement.setDouble(2, nouveauPrix);
                statement.setInt(3, nouvelleQuantite);
                statement.setString(4, identifiant);
                statement.executeUpdate();

                System.out.println("Médicament modifié avec succès.");
            } else {
                System.out.println("Médicament non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afficherListeParLettre(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Entrez la lettre pour afficher la liste des médicaments : ");
            char lettre = scanner.nextLine().charAt(0);

            String sql = "SELECT * FROM medicaments WHERE nom LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lettre + "%");
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Médicaments commençant par la lettre " + lettre + " :");
            while (resultSet.next()) {
                Medicament medicament = creerMedicament(resultSet);
                afficherMedicament(medicament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afficherNombreMedicaments() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) AS total FROM medicaments";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int total = resultSet.getInt("total");
                System.out.println("Nombre total de médicaments en stock : " + total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afficherMedicament(Medicament medicament) {
        System.out.println("Type : " + medicament.getType());
        System.out.println("Nom : " + medicament.getNom());
        System.out.println("Prix : " + medicament.getPrix());
        System.out.println("Quantité en stock : " + medicament.getQuantiteEnStock());
        System.out.println("Identifiant : " + medicament.getIdentifiant());

        if (medicament instanceof MedicamentSurOrdonnance) {
            System.out.println("Ordonnance : " + ((MedicamentSurOrdonnance) medicament).getOrdonnance());
        }

        System.out.println();
    }

    private static Medicament creerMedicament(ResultSet resultSet) throws SQLException {
        String nom = resultSet.getString("nom");
        double prix = resultSet.getDouble("prix");
        int quantiteEnStock = resultSet.getInt("quantiteEnStock");
        String identifiant = resultSet.getString("identifiant");
        String type = resultSet.getString("type");

        if (type.equals("MedicamentSurOrdonnance")) {
            String ordonnance = resultSet.getString("ordonnance");
            return new MedicamentSurOrdonnance(nom, prix, quantiteEnStock, identifiant, ordonnance);
        } else {
            return new MedicamentVenteLibre(nom, prix, quantiteEnStock, identifiant);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine();

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
                    // Autres fonctionnalités
                    break;
                case 9:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
                    break;
            }
        }

        scanner.close();
    }
}