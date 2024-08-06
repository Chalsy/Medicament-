class MedicamentSurOrdonnance extends Medicament {
    private String ordonnance;

    public MedicamentSurOrdonnance(String nom, double prix, int quantiteEnStock, String identifiant, String ordonnance) {
        super(nom, prix, quantiteEnStock, identifiant);
        this.ordonnance = ordonnance;
    }

    @Override
    public String getType() {
        return "Médicament sur ordonnance";
    }

    public String getOrdonnance() {
        return ordonnance;
    }
}
