class MedicamentVenteLibre extends Medicament {
    public MedicamentVenteLibre(String nom, double prix, int quantiteEnStock, String identifiant) {
        super(nom, prix, quantiteEnStock, identifiant);
    }

    @Override
    public String getType() {
        return "Médicament en vente libre";
    }
}
