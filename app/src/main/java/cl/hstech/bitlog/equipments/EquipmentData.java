package cl.hstech.bitlog.equipments;

public class EquipmentData {

    private String machine;
    private String picture;
    private String lastSeenLocation;

    public EquipmentData(String machine, String picture, String lastSeenLocation) {

        this.machine = machine;
        this.picture = picture;
        this.lastSeenLocation = lastSeenLocation;

    }

    public String getMachine() {
        return machine;
    }

    public String getPicture() {
        return picture;
    }

    public String getLastSeenLocation() {
        return lastSeenLocation;
    }
}
