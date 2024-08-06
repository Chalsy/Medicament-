abstract class Medicament {
    protected String nom;
    protected double prix;
    protected int quantiteEnStock;
    protected String identifiant;

    public Medicament(String nom, double prix, int quantiteEnStock, String identifiant) {
        this.nom = nom;
        this.prix = prix;
        this.quantiteEnStock = quantiteEnStock;
        this.identifiant = identifiant;
    }

    public abstract String getType();

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public int getQuantiteEnStock() {
        return quantiteEnStock;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock = quantiteEnStock;
    }
}