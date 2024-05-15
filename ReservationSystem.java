import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.nio.file.Files;

public class ReservationSystem {

    private ArrayList<Accommodation> accommodations;

    // Requirement 1: Load data
    public ReservationSystem(String accPath, String roomPath, String roomOfAccPath) {
        this.accommodations = loadAccommodations(accPath, roomPath, roomOfAccPath);
    }

    public ArrayList<Accommodation> getAccommodations() {
        return accommodations;
    }

    // Requirement 1
    public boolean bool(String s) {
        if ("yes".equals(s)) {
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<Accommodation> loadAccommodations(String accPath, String roomPath, String roomOfAccPath) {
        ArrayList<Accommodation> acclist = new ArrayList<>();
        ArrayList<Room> roomlist = new ArrayList<>();
        Room[][] roomofacc = null;

        try (BufferedReader accReader = new BufferedReader(new FileReader(accPath));
             BufferedReader roomReader = new BufferedReader(new FileReader(roomPath));
             BufferedReader roomOfAccReader = new BufferedReader(new FileReader(roomOfAccPath))) {

            String accOut;
            while ((accOut = accReader.readLine()) != null) {
                String[] arr1 = accOut.split(",");
                if (arr1.length == 5) {
                    Accommodation acc = new Homestay(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], Float.valueOf(arr1[4]));
                    acclist.add(acc);
                }
                if (arr1.length == 6) {
                    Accommodation acc = new Hotel(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], Float.valueOf(arr1[5]), Integer.valueOf(arr1[4]));
                    acclist.add(acc);
                }
                if (arr1.length == 7) {
                    Accommodation acc = new Resort(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], Float.valueOf(arr1[6]), Integer.valueOf(arr1[4]), bool(arr1[5]));
                    acclist.add(acc);
                }
                if (arr1.length == 10) {
                    Accommodation acc = new Villa(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], bool(arr1[4]), bool(arr1[5]), bool(arr1[6]), bool(arr1[7]), Integer.valueOf(arr1[8]), Integer.valueOf(arr1[9]));
                    acclist.add(acc);
                }
                if (arr1.length == 11) {
                    Accommodation acc = new CruiseShip(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], bool(arr1[4]), bool(arr1[5]), bool(arr1[6]), bool(arr1[7]), Integer.valueOf(arr1[9]), Integer.valueOf(arr1[10]), bool(arr1[8]));
                    acclist.add(acc);
                }
            }
            
            String roomOut;
            while((roomOut=roomReader.readLine()) != null) {
                String[] arr2 = roomOut.split(",");
                Room room = new Room(Integer.valueOf(arr2[0]), arr2[1], Integer.valueOf(arr2[2]), arr2[3], Integer.valueOf(arr2[4]), Integer.valueOf(arr2[5]), Double.valueOf(arr2[6]), Double.valueOf(arr2[7]));
                roomlist.add(room);
            }

            String roomOfAccOut;
            while ((roomOfAccOut = roomOfAccReader.readLine()) != null) {
            String[] arr3 = roomOfAccOut.split(",");
            int accId = Integer.valueOf(arr3[0]);
            int roomId = Integer.valueOf(arr3[1]);
            Accommodation accommodation = null;
            int accindex = -1;
            int roomindex = -1;

            for (int i = 0; i < acclist.size(); i++) {
                if (acclist.get(i).getidluutru() == accId) {
                    accindex = i;
                    break;
                }
            }

            for (int j = 0; j < roomlist.size(); j++) {
                if (roomlist.get(j).getmadinhdanh() == roomId) {
                    roomindex = j;
                    break;
                }
            }

            if (accindex != -1 && roomindex != -1) {
                if (roomofacc == null) {
                    roomofacc = new Room[acclist.size()][roomlist.size()];
                }
                roomofacc[accindex][roomindex] = roomlist.get(roomindex);
            }
            return acclist;
            }
        } 
        catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace(); 
        } catch (IOException ioe) {
            ioe.printStackTrace(); 
        }
        return null;
    }  

    // Requirement 2
    public ArrayList<Accommodation> searchForRoom(String city, int numOfPeople) {
        /* Code here */
        return null;
    }

    // Requirement 3
    public ArrayList<Accommodation> searchForRoomByRange(String reservationPath, double priceFrom, double priceTo,
            Date checkin, Date checkout, String city, int numOfPeople) {
        /* Code here */
        return null;
    }

    // Requirement 4
    public ArrayList<Accommodation> searchInAdvance(String city, Integer numOfPeople, String roomType,
            Boolean privatePool, Integer starQuality, Boolean freeBreakfast, Boolean privateBar) {
        /* Code here */
        return null;
    }

    // Requirement 5
    public double performReservation(String reservationPath, Accommodation acc, Room room, Date checkin, Date checkout)
            throws Exception {
        /* Code here */
        return 0.0;
    }

    // Helper functions for req 5
    public long diffBetweenDays(long dateStart, long dateEnd) {
        Date date = new Date(dateStart * 1000);
        Date date1 = new Date(dateEnd * 1000);

        long diff = Math.abs(date1.getTime() - date.getTime());
        long numOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return numOfDays;
    }
}
