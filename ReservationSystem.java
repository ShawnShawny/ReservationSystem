import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

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
        ArrayList<Accommodation> acclist= new ArrayList<>();
        ArrayList<Room> roomlist= new ArrayList<>();
        Map<Integer, List<Room>> accommodationMap= new HashMap<>();
        Set<Integer> key= new HashSet<>();
        

        try (BufferedReader accReader= new BufferedReader(new FileReader(accPath));
             BufferedReader roomReader= new BufferedReader(new FileReader(roomPath));
             BufferedReader roomOfAccReader= new BufferedReader(new FileReader(roomOfAccPath))) {

            String accOut;
            while ((accOut= accReader.readLine())!= null) {
                String[] arr1= accOut.split(",");
                switch (arr1.length) {
                case 5:
                    Accommodation homestay= new Homestay(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], Float.valueOf(arr1[4]));
                    acclist.add(homestay);
                    break;

                case 6:
                    Accommodation hotel= new Hotel(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], Float.valueOf(arr1[5]), Integer.valueOf(arr1[4]));
                    acclist.add(hotel);
                    break;
                
                case 7:
                    Accommodation resort= new Resort(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], Float.valueOf(arr1[6]), Integer.valueOf(arr1[4]), bool(arr1[5]));
                    acclist.add(resort);
                    break;
                
                case 10:
                    Accommodation villa= new Villa(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], bool(arr1[4]), bool(arr1[5]), bool(arr1[6]), bool(arr1[7]), Integer.valueOf(arr1[8]), Integer.valueOf(arr1[9]));
                    acclist.add(villa);
                    break;
                
                case 11:
                    Accommodation cruiseship= new CruiseShip(Integer.valueOf(arr1[0]), arr1[1], arr1[2], arr1[3], bool(arr1[4]), bool(arr1[5]), bool(arr1[6]), bool(arr1[7]), Integer.valueOf(arr1[9]), Integer.valueOf(arr1[10]), bool(arr1[8]));
                    acclist.add(cruiseship);
                    break;
                }
            }
            
            String roomOut;
            while((roomOut= roomReader.readLine()) != null) {
                String[] arr2= roomOut.split(",");
                Room room= new Room(Integer.valueOf(arr2[0]), arr2[1], Integer.valueOf(arr2[2]), arr2[3], Integer.valueOf(arr2[4]), Integer.valueOf(arr2[5]), Double.valueOf(arr2[6]), Double.valueOf(arr2[7]));
                roomlist.add(room);
            }

            String roomOfAccOut;
            while ((roomOfAccOut= roomOfAccReader.readLine()) != null) {
            String[] arr3= roomOfAccOut.split(",");
            int accId= Integer.valueOf(arr3[0]);
            int roomId= Integer.valueOf(arr3[1]);
            Accommodation accommodation= null;

            for (Accommodation acc: acclist) {
                if (acc.getidluutru()== accId) {
                    accommodation = acc;
                    break;
                }
            }

            Room room= null;
            for (Room rm: roomlist) {
            if (rm.getmadinhdanh()== roomId) {
                room= rm;
                break;
            }
            }

            if (accommodation!= null && room!= null) {
                List<Room> rooms= accommodationMap.get(accId);
                if (rooms== null) {
                    rooms= new ArrayList<>();
                    accommodationMap.put(accId, rooms);
                }
                rooms.add(room);
                if (!key.contains(accId)) {
                    key.add(accId);
                }
            }
        }
        for (Accommodation accom: acclist) {
            for (int i: key) {
                if (i == accom.getidluutru()) {
                    List<Room> roomsForAccommodation= accommodationMap.get(i);
                    ((CommonAccommodation) accom).setphong(new ArrayList<>(roomsForAccommodation));
                }
            }
        }
        return acclist;
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
        ArrayList<Accommodation> result= new ArrayList<>();
        ArrayList<Accommodation> luxuryAccommodation= new ArrayList<>();
        ArrayList<Accommodation> commonAccommodation= new ArrayList<>();
    
        for (Accommodation acc: accommodations) {
            if (acc.getthanhpholuutru().equals(city)) {
                if (acc instanceof LuxuryAccommodation) {
                    LuxuryAccommodation luxacc= (LuxuryAccommodation) acc;
                    if (luxacc.getmaxphucvu()>= numOfPeople) {
                        luxuryAccommodation.add(acc);
                    }
                }
            }
        }

        for (Accommodation acc: accommodations) {
            if (acc.getthanhpholuutru().equals(city)) {
                if (acc instanceof CommonAccommodation) {
                    CommonAccommodation comacc= (CommonAccommodation) acc;
                    for (Room room: comacc.getphong()) {
                        if (room.getmaxnguoi()>= numOfPeople) {
                            commonAccommodation.add(acc);
                        }
                    }
                }
            }
        }

        for (int i= 0; i< luxuryAccommodation.size()- 1; i++) {
            for (int j= 0; j< luxuryAccommodation.size()- i- 1; j++) {
                String ad1= luxuryAccommodation.get(j).gettenluutru();
                String ad2= luxuryAccommodation.get(j+ 1).gettenluutru();
                if (ad1.compareTo(ad2)> 0) {
                    Accommodation temp= luxuryAccommodation.get(j);
                    luxuryAccommodation.set(j, luxuryAccommodation.get(j+ 1));
                    luxuryAccommodation.set(j+ 1, temp);
                }
            }
        }

        for (int x= 0; x< commonAccommodation.size()- 1; x++) {
            for (int y= 0; y< commonAccommodation.size()- x- 1; y++) {
                String ad3= commonAccommodation.get(y).gettenluutru();
                String ad4= commonAccommodation.get(y+ 1).gettenluutru();
                if (ad3.compareTo(ad4)> 0) {
                    Accommodation temp= commonAccommodation.get(y);
                    commonAccommodation.set(y, commonAccommodation.get(y+ 1));
                    commonAccommodation.set(y+ 1, temp);
                }
            }
        }
        for (Accommodation acc: luxuryAccommodation) {
            result.add(acc);
        }
        for (Accommodation acc: commonAccommodation) {
            result.add(acc);
        }
        return result;
    }

    // Requirement 3
    public ArrayList<Accommodation> searchForRoomByRange(String reservationPath, double priceFrom, double priceTo,
        Date checkin, Date checkout, String city, int numOfPeople) {
        ArrayList<Accommodation> result= new ArrayList<>();
        ArrayList<Accommodation> luxuryAccommodation= new ArrayList<>();
        ArrayList<Accommodation> commonAccommodation= new ArrayList<>();
        ArrayList<Reservation> reservations= new ArrayList<>();
        try (BufferedReader reservationReader= new BufferedReader(new FileReader(reservationPath))) {
            String resOut;
            while ((resOut= reservationReader.readLine()) != null) {
                String[] array1= resOut.split(",");
                if (array1.length== 4) {
                    int reservationId = Integer.valueOf(array1[0]);
                    int accId= Integer.valueOf(array1[1]);
                    int roomId= -1;
                    long timecheckin= Long.valueOf(array1[2]);
                    long timecheckout= Long.valueOf(array1[3]);
                    Date datecheckin= new Date((timecheckin));
                    Date datecheckout= new Date((timecheckout));
                    Reservation reserv= new Reservation(reservationId, accId, roomId, datecheckin, datecheckout);
                    reservations.add(reserv);
                }

                if (array1.length== 5) {
                    int reservationId = Integer.valueOf(array1[0]);
                    int accId= Integer.valueOf(array1[1]);
                    int roomId= Integer.valueOf(array1[2]);
                    long timecheckin= Long.valueOf(array1[3]);
                    long timecheckout= Long.valueOf(array1[4]);
                    Date datecheckin= new Date((timecheckin));
                    Date datecheckout= new Date((timecheckout));
                    Reservation reserv= new Reservation(reservationId, accId, roomId, datecheckin, datecheckout);
                    reservations.add(reserv);
                }
            }    
        }catch (IOException ioe) {
                ioe.printStackTrace();
                return null; 
            }

        for (Accommodation acc: accommodations) {
            if (acc.getthanhpholuutru().equals(city)) {
                if (acc instanceof LuxuryAccommodation) {
                    LuxuryAccommodation luxacc= (LuxuryAccommodation) acc;
                    if (luxacc.getchiphi()>= priceFrom && luxacc.getchiphi()<= priceTo) {
                        if (luxacc.getmaxphucvu()>= numOfPeople && luxacc.getmaxphucvu()<= numOfPeople+ 2) {
                            boolean checkroomfree = true;
                            for (Reservation reservation: reservations) {
                                if (reservation.getAccId()== acc.getidluutru()) {
                                    if (!(checkin.compareTo(reservation.getCheckout())>= 0 || checkout.compareTo(reservation.getCheckin())<= 0)) {
                                        checkroomfree= false;
                                    }
                                }
                            }
                            if (checkroomfree) {
                                luxuryAccommodation.add(acc);
                            }
                        }
                    }
                }
            }
        }

        for (Accommodation acc: accommodations) {
            if (acc.getthanhpholuutru().equals(city)) {
                if (acc instanceof CommonAccommodation) {
                    CommonAccommodation comacc= (CommonAccommodation) acc;
                    for (Room room: comacc.getphong()) {
                        if (room.getchiphi()>= priceFrom && room.getchiphi()<= priceTo) {
                            if (room.getmaxnguoi()>= numOfPeople && room.getmaxnguoi()<= numOfPeople+ 2){
                                boolean checkroomfree = true;
                                for (Reservation reservation: reservations) {
                                    if (reservation.getRoomId()== room.getmadinhdanh()) {
                                        if (!(checkin.compareTo(reservation.getCheckout())>= 0 || checkout.compareTo(reservation.getCheckin())<= 0)) {
                                            checkroomfree= false;
                                        }
                                    }
                                }
                                if (checkroomfree) {
                                    commonAccommodation.add(acc);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i= 0; i< luxuryAccommodation.size()- 1; i++) {
            for (int j= 0; j< luxuryAccommodation.size()- i- 1; j++) {
                String ad1= luxuryAccommodation.get(j).gettenluutru();
                String ad2= luxuryAccommodation.get(j+ 1).gettenluutru();
                if (ad1.compareTo(ad2)< 0) {
                    Accommodation temp= luxuryAccommodation.get(j);
                    luxuryAccommodation.set(j, luxuryAccommodation.get(j+ 1));
                    luxuryAccommodation.set(j+ 1, temp);
                }
            }
        }

        for (int x= 0; x< commonAccommodation.size()- 1; x++) {
            for (int y= 0; y< commonAccommodation.size()- x- 1; y++) {
                String ad3= commonAccommodation.get(y).gettenluutru();
                String ad4= commonAccommodation.get(y+ 1).gettenluutru();
                if (ad3.compareTo(ad4)< 0) {
                    Accommodation temp= commonAccommodation.get(y);
                    commonAccommodation.set(y, commonAccommodation.get(y+ 1));
                    commonAccommodation.set(y+ 1, temp);
                }
            }
        }
        for (Accommodation acc: luxuryAccommodation) {
            result.add(acc);
        }
        for (Accommodation acc: commonAccommodation) {
            result.add(acc);
        }
        return result;
    }

    // Requirement 4
    public ArrayList<Accommodation> searchInAdvance(String city, Integer numOfPeople, String roomType,
            Boolean privatePool, Integer starQuality, Boolean freeBreakfast, Boolean privateBar) {
        ArrayList<Accommodation> search= new ArrayList<>();
        for (Accommodation acc: accommodations) {
            if (acc.getthanhpholuutru().equals(city)) {
                if (acc instanceof CommonAccommodation) {
                    CommonAccommodation comacc= (CommonAccommodation) acc;
                    for (Room room: comacc.getphong()) {
                        if (room.getmaxnguoi()>= numOfPeople ) {
                            if ((roomType== null || room.getloaiphong().equals(roomType))) {
                                if ((privatePool== null || acc instanceof Resort && ((Resort) acc).isbeboi()== privatePool)) {
                                    if (((starQuality== null) || (acc instanceof Resort && Integer.valueOf(((Resort) acc).getsaorating())== starQuality) 
                                    || (acc instanceof Hotel && Integer.valueOf(((Hotel) acc).getrating())== starQuality))) {
                                        search.add(comacc);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        for (Accommodation acc : accommodations) {
            if (acc.getthanhpholuutru().equals(city)) {
                if (acc instanceof LuxuryAccommodation) {
                    LuxuryAccommodation luxacc= (LuxuryAccommodation) acc;
                    if (luxacc.getchiphi() >= numOfPeople) {
                        if (roomType== null) {
                            if (privatePool== null || luxacc.isbeboi()== privatePool) {
                                if (starQuality== null) {
                                    if (freeBreakfast== null || luxacc.isbuasang()== freeBreakfast) {
                                        if (privateBar== null || (acc instanceof CruiseShip && ((CruiseShip) acc).isbarrieng()== privateBar)) {
                                            search.add(luxacc);
                                        }
                                    }   
                                }
                            }
                        }
                    }
                }
            }
        }
        return search;
    }

    // Requirement 5
    public double performReservation(String reservationPath, Accommodation acc, Room room, Date checkin, Date checkout)
            throws Exception {
        ArrayList<Reservation> reservations= new ArrayList<>();
        double tongtien= 0.0;
        try (BufferedReader reservationReader= new BufferedReader(new FileReader(reservationPath))) {
            String resOut;
            while ((resOut= reservationReader.readLine()) != null) {
                String[] array1= resOut.split(",");
                if (array1.length== 4) {
                    int reservationId= Integer.valueOf(array1[0]);
                    int accId= Integer.valueOf(array1[1]);
                    int roomId= -1;
                    long timecheckin= Long.valueOf(array1[2]);
                    long timecheckout= Long.valueOf(array1[3]);
                    Date datecheckin= new Date((timecheckin));
                    Date datecheckout= new Date((timecheckout));
                    Reservation reserv= new Reservation(reservationId, accId, roomId, datecheckin, datecheckout);
                    reservations.add(reserv);
                }
                if (array1.length== 5) {
                    int reservationId= Integer.valueOf(array1[0]);
                    int accId= Integer.valueOf(array1[1]);
                    int roomId= Integer.valueOf(array1[2]);
                    long timecheckin= Long.valueOf(array1[3]);
                    long timecheckout= Long.valueOf(array1[4]);
                    Date datecheckin= new Date((timecheckin));
                    Date datecheckout= new Date((timecheckout));
                    Reservation reserv= new Reservation(reservationId, accId, roomId, datecheckin, datecheckout);
                    reservations.add(reserv);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return 0.0;
        }
            boolean checkroomfree= true;
            for (Reservation reservation: reservations) {
                if (reservation.getAccId()== acc.getidluutru()) {
                    if (!(checkin.compareTo(reservation.getCheckout())>= 0 || checkout.compareTo(reservation.getCheckin())<= 0)) {
                        checkroomfree = false;
                    }
                }
            if (checkroomfree) {
                long checkintime= checkin.getTime();
                long checkouttime= checkout.getTime();
                long songaydat= diffBetweenDays(checkintime, checkouttime);
                if (songaydat< 1) {
                    songaydat= 1;
                } else {
                    songaydat= (long) Math.ceil(songaydat);
                }
                double tienkhongthue= room.getchiphi()* songaydat;
                tongtien= tienkhongthue+ (tienkhongthue* 0.08);

                try (PrintWriter writer= new PrintWriter(new FileWriter(reservationPath, true))) {
                    int newRes= reservations.size()+ 1; 
                    writer.println(newRes + "," + acc.getidluutru() + "," + room.getmadinhdanh() + "," + checkin.getTime() + "," + checkout.getTime());
                    break;
                }
            } else {
                throw new Exception("The room has already been booked during this time period");
            }
        }
        return tongtien;
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
