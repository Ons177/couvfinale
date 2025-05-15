package entities;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Anonce {
        private int idAnonce;
        private String titre;
        private String description;
        private Date dateDisponibilite;
        private double prixParJour;
        private int vehiculeId;
        private int utilisateurIda;
        private String photoVehicule;
        private String emailContact;
        private String adresse;
        private double latitude;
        private double longitude;

        // Constructeur
        public Anonce(int idAnonce, String titre, String description, Date dateDisponibilite, double prixParJour,
                      int vehiculeId, int utilisateurId, String photoVehicule,
                      String adresse) {
            this.idAnonce = idAnonce;
            this.titre = titre;
            this.description = description;
            this.dateDisponibilite = dateDisponibilite;
            this.prixParJour = prixParJour;
            this.vehiculeId = vehiculeId;
            this.utilisateurIda = utilisateurId;
            this.photoVehicule = photoVehicule;
            this.adresse = adresse;

        }
        public Anonce(String titre, String description, Date dateDisponibilite, double prixParJour,
                      int vehiculeId, int utilisateurId, String photoVehicule,
                      String adresse, double latitude, double longitude) {
            this.titre = titre;
            this.description = description;
            this.dateDisponibilite = dateDisponibilite;
            this.prixParJour = prixParJour;
            this.vehiculeId = vehiculeId;
            this.utilisateurIda = utilisateurId;
            this.photoVehicule = photoVehicule;
            this.adresse = adresse;
            this.latitude = latitude;
            this.longitude = longitude;

        }
    public List<String> genererTags(Anonce a) {
        List<String> tags = new ArrayList<>();
        String texte = (a.getTitre() + " " + a.getDescription()).toLowerCase();

        Map<String, List<String>> motsClesParTag = new LinkedHashMap<>();
        motsClesParTag.put("Familiale", List.of("familial", "familiale", "7 places", "grande famille"));
        motsClesParTag.put("4x4", List.of("4x4", "suv", "tout terrain"));
        motsClesParTag.put("Économique", List.of("économique", "essence", "faible consommation"));
        motsClesParTag.put("Luxe", List.of("luxe", "premium", "haut de gamme", "confort"));

        for (Map.Entry<String, List<String>> entry : motsClesParTag.entrySet()) {
            String tag = entry.getKey();
            for (String motCle : entry.getValue()) {
                if (texte.contains(motCle)) {
                    tags.add(tag);
                    break; // évite les doublons si plusieurs mots-clés sont présents
                }
            }
        }

        return tags;
    }



    // Getters et Setters
        public int getIdAnonce() {
            return idAnonce;
        }

        public void setIdAnonce(int idAnonce) {
            this.idAnonce = idAnonce;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date  getDateDisponibilite() {
            return dateDisponibilite;
        }

        public void setDateDisponibilite(Date dateDisponibilite) {
            this.dateDisponibilite = dateDisponibilite;
        }

        public double getPrixParJour() {
            return prixParJour;
        }

        public void setPrixParJour(double prixParJour) {
            this.prixParJour = prixParJour;
        }

        public int getVehiculeId() {
            return vehiculeId;
        }

        public void setVehiculeId(int vehiculeId) {
            this.vehiculeId = vehiculeId;
        }

        public int getUtilisateurId() {
            return utilisateurIda;
        }

        public void setUtilisateurId(int utilisateurId) {
            this.utilisateurIda = utilisateurId;
        }

        public String getPhotoVehicule() {
            return photoVehicule;
        }

        public void setPhotoVehicule(String photoVehicule) {
            this.photoVehicule = photoVehicule;
        }

        public String getAdresse() {
            return adresse;
        }

        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }



        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        // Méthode toString pour afficher l'annonce
        @Override
        public String toString() {
            return "Anonce{" +
                    "idAnonce=" + idAnonce +
                    ", titre='" + titre + '\'' +
                    ", description='" + description + '\'' +
                    ", dateDisponibilite=" + dateDisponibilite +
                    ", prixParJour=" + prixParJour +
                    ", vehiculeId=" + vehiculeId +
                    ", utilisateurId=" + utilisateurIda +
                    ", photoVehicule='" + photoVehicule + '\'' +
                    ", emailContact='" + emailContact + '\'' +
                    ", adresse='" + adresse + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }


