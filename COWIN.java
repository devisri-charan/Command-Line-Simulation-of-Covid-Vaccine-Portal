import java.util.*;

public class COWIN {

    static Map<Long,String> CitizenDict = new Hashtable<>();
    static Map<Integer,Integer> HospitalPincodeIDDict = new Hashtable<>();
    static Map<String,Integer> HospitalNamePincodeDict = new Hashtable<>();
    static Map<String,Integer> HospitalNameIDDict = new Hashtable<>();
    static Map<Integer,String> HospitalIDNameDict = new Hashtable<>();
    static Map<Integer,String> HospitalIDVaccineDict = new Hashtable<>();
    static Map<String,Integer> HospitalVaccineIDDict = new Hashtable<>();
    static Map<Integer,int[]> HospitalIDSlotDict = new Hashtable<>();
    static Map<Long,int[]> CitizenIDSlotDict = new Hashtable<>();
    static ArrayList<Integer> HospitalUniqueIds = new ArrayList<>();
    static ArrayList<Long> CitizenUniqueIds = new ArrayList<>();


    static class Vaccine{
        void AddVaccine(String VaccineName, int NumberOfTotalDosesRequired, int GapBetweenDoses, List<String> vaccine){
            vaccine.add(VaccineName);
            System.out.printf("Vaccine Name: %s, Number of Doses: %d, Gap Between Doses: %d\n",VaccineName,NumberOfTotalDosesRequired,GapBetweenDoses);
        }

        void ListOfVaccines(List<String> vaccine){
            for (int i = 0; i < vaccine.size(); i++) {
                System.out.println(i+"."+vaccine.get(i));
            }
        }

    }

    static class Hospital {
        int generateUniqueID() {
            Random random = new Random();
            StringBuilder UniqueID = new StringBuilder();

            UniqueID.append(random.nextInt(9) + 1);

            for (int i = 0; i < 5; i++) {
                UniqueID.append(random.nextInt(10));
            }

            return Integer.parseInt(UniqueID.toString());
        }

        void RegisterHospital(String HospitalName,int Pincode){
            int HospitalUniqueID = generateUniqueID();
            if (!HospitalUniqueIds.contains(HospitalUniqueID)){

                HospitalUniqueIds.add(HospitalUniqueID);

                HospitalNamePincodeDict.put(HospitalName,Pincode);

                HospitalPincodeIDDict.put(Pincode,HospitalUniqueID);

                HospitalNameIDDict.put(HospitalName,HospitalUniqueID);

                HospitalIDNameDict.put(HospitalUniqueID,HospitalName);

                System.out.println("Allocated Hospital ID is "+HospitalUniqueID);
                System.out.printf("Hospital Name: %s, PinCode: %d, Unique ID: %d\n",HospitalName,Pincode, HospitalUniqueID);
            }
            else
                System.out.println("Hospital is already registered");
        }

        void HospitalSearchByPincode(int Pincode){
//            System.out.println("zxcvbnm");
            for (Map.Entry<String,Integer> p : HospitalNamePincodeDict.entrySet()){
//                System.out.println("zxcvbnm");
                if (Pincode == p.getValue()){
                    System.out.println(HospitalNameIDDict.get(p.getKey()) + " " + p.getKey());
                }
            }
        }

        void HospitalSearchByVaccineName(String VaccineName){
            for (Map.Entry<Integer,String> v : HospitalIDVaccineDict.entrySet()){
//                System.out.println("zxcvbnm");
                if (Objects.equals(VaccineName, v.getValue())){
                    System.out.println(HospitalNameIDDict.get(v.getValue() + " " + HospitalIDNameDict.get(v.getKey())));
                }
            }
        }
    }

    static class Citizen{
        void RegisterCitizen(String CitizenName,int Age,long CitizenUniqueID){
            CitizenDict.put(CitizenUniqueID,CitizenName);
            CitizenUniqueIds.add(CitizenUniqueID);
            System.out.printf("Citizen Name: %s, Age: %d, Unique ID: %d\n",CitizenName,Age,CitizenUniqueID);
        }
        boolean VaccineTaken(long UniqueID,int Day,int quantity,Integer VaccineName){
            int[] Slot = new int[]{Day, quantity, VaccineName};

            return true;
        }

    }

    static class Slots{
        void CreateSlot(int HospitalID,int Day,int quantity,Integer VaccineIndex,String VaccineName){
//            String Slot = String.format("Day: %d, Available Quantity: %d, Vaccine: %s",Day,quantity,VaccineName);
            HospitalIDVaccineDict.put(HospitalID,VaccineName);
            int[] Slot = new int[]{Day, quantity, VaccineIndex};
            HospitalIDSlotDict.put(HospitalID,Slot);
        }
        void BookASlot(long CitizenID,int Day,int quantity,Integer VaccineIndex,String VaccineName){
            int[] Slot = new int[]{Day, quantity, VaccineIndex};
//            int[] existingSlot = HospitalIDSlotDict.get(CitizenID)
            int[] newSlot = new int[]{Day, quantity-1, VaccineIndex};
            HospitalIDSlotDict.replace(HospitalVaccineIDDict.get(VaccineName),Slot,newSlot);
            CitizenIDSlotDict.put(CitizenID,Slot);
        }
        void SlotsAvailableWithAHospital(int HospitalID){
            for (Map.Entry<Integer, int[]> v : HospitalIDSlotDict.entrySet()) {
                System.out.println(Arrays.toString(v.getValue()));
            }
        }

    }

    static class VaccinationStatus{
        void ViewVaccinationStatus(long PatientID,boolean VaccineTaken){
            if (VaccineTaken)
                System.out.println("FULLY VACCINATED");
            else
                System.out.println("PARTIALLY VACCINATED");

            System.out.println("Vaccine Given: ");
            System.out.println(CitizenIDSlotDict.get(PatientID)[2]);
            System.out.println("Number of Doses given: ");
            System.out.println(CitizenIDSlotDict.get(PatientID)[1]);
        }

    }

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        List<String> vaccine = new ArrayList<>();
        System.out.println("CoVin Portal initialized....");
        while (true){
            System.out.println("-----------------------------");
            System.out.println("""
                1. Add Vaccine
                2. Register Hospital
                3. Register Citizen
                4. Add Slot for Vaccination
                5. Book Slot for Vaccination
                6. List all slots for a hospital
                7. Check Vaccination Status
                8. Exit""");
            System.out.println("-----------------------------");
            int id = input.nextInt();
            Vaccine v = new Vaccine();
            Hospital h = new Hospital();
            Citizen c = new Citizen();
            Slots s = new Slots();
            VaccinationStatus vs= new VaccinationStatus();

            if (id == 1){
                /*1. Add Vaccine:

                Input: Name, Number of total doses required, Gap Between Doses

                Output: Display the added vaccine details
                 */
                System.out.print("Vaccine Name: ");
                String VaccineName = input.next();
                if (!vaccine.contains(VaccineName)) {
                    System.out.print("Number of Doses: ");
                    int NoOfDoses = input.nextInt();
                    if (NoOfDoses > 1) {
                        System.out.print("Gap between Doses: ");
                        int Gap = input.nextInt();
                        v.AddVaccine(VaccineName, NoOfDoses, Gap, vaccine);
                    } else {
                        v.AddVaccine(VaccineName, NoOfDoses, 0, vaccine);
                    }
                }
                else
                    System.out.println("Vaccine is already added");

            }

            if (id == 2){
                /*2. Register Hospital:

                Input: Name, Pincode

                Output: Display the added hospital details along with the generated unique hospital ID (A 6-digit number)
                 */
                System.out.print("Hospital Name: ");
                String HospitalName = input.next();
                System.out.print("PinCode: ");
                int Pincode = input.nextInt();
                h.RegisterHospital(HospitalName,Pincode);
            }

            if (id == 3){
                /*
                3. Register Citizen:

                Input: Name, Age, Unique ID (A 12 digit numeric ID like Aadhaar ID)

                Output: Display the citizen details and set his/her vaccination status as "REGISTERED."
                 */
                System.out.print("Citizen Name: ");
                String CitizenName = input.next();
                System.out.print("Age: ");
                int Age = input.nextInt();
                System.out.print("Unique ID: ");
                long UniqueID = input.nextLong();
                if (Age >= 18){
                    c.RegisterCitizen(CitizenName,Age,UniqueID);
                }
                else{
                    System.out.print("Only above 18 are allowed");
                }
            }

            if(id==4){
                /*4. Create Slots:

                Input: Hospital ID followed by the number of slots that the hospital wants to add.
                For each slot, enter the day number and quantity followed by selecting the vaccine for that slot.

                Output: Display the details of the added slot.
                */
                System.out.print("Enter Hospital ID: ");
                int HospitalID = input.nextInt();
                if (HospitalUniqueIds.contains(HospitalID)){
                    System.out.print("Enter number of Slots to be added: ");
                    int n = input.nextInt();
                    for(int i=0;i<n;i++) {
                        System.out.print("Enter Day Number: ");
                        int d = input.nextInt();
                        System.out.print("Enter Quantity: ");
                        int q = input.nextInt();
                        System.out.println("Select Vaccine");
                        v.ListOfVaccines(vaccine);
                        int l = input.nextInt();
                        System.out.println(vaccine.get(l));
                        HospitalIDVaccineDict.put(HospitalID,vaccine.get(l));
                        HospitalVaccineIDDict.put(vaccine.get(l),HospitalID);
                        s.CreateSlot(HospitalID,d,q,l,vaccine.get(l));
                        System.out.printf("Slot added by Hospital %s for Day: %d, Available Quantity: %d of Vaccine %s\n",HospitalID,d,q,vaccine.get(l));
                    }
                }
                else
                    System.out.println("Hospital is not registered");
            }

            if(id==5) {
                /*
                5. Book a Slot:

                Input: Unique ID of the citizen followed by 2 options to search: {By Pincode, By Vaccine}.
                A successful search should show a list of possible hospitals. Upon selecting the chosen hospital,
                their available slots must be reflected, and a chosen slot must be booked.

                Output: Display the citizen vaccinated along with the vaccine. Change the status of
                the citizen to PARTIALLY VACCINATED/FULLY VACCINATED accordingly.

                 */
                System.out.print("Enter patient Unique ID: ");
                long UniqueID = input.nextLong();
                if (CitizenUniqueIds.contains(UniqueID)) {
                    String CitizenName = CitizenDict.get(UniqueID);
                    System.out.println("""
                            1. Search by area
                            2. Search by Vaccine
                            3. Exit
                            """);
                    System.out.println("Enter option: ");
                    int o = input.nextInt();
                    if (o == 1) {
                        System.out.println("Enter PinCode: ");
                        int p = input.nextInt();
                        h.HospitalSearchByPincode(p);
                        System.out.println("Enter hospital id: ");
                        int HospitalID = input.nextInt();
                        String VaccineName = HospitalIDVaccineDict.get(HospitalID);
                        s.SlotsAvailableWithAHospital(HospitalID);
                        System.out.println("Choose Slot: ");
                        int slot = input.nextInt();
                        System.out.printf("%s vaccinated with %s\n", CitizenName, VaccineName);
                    }

                    if (o == 2) {
                        System.out.println("Enter Vaccine Name: ");
                        String vaccineName = input.next();
                        // Unique ID Hospital Name
                        h.HospitalSearchByVaccineName(vaccineName);
                        System.out.println("Enter hospital id: ");
                        int ID = input.nextInt();
                        String VaccineName = HospitalIDVaccineDict.get(ID);
                        s.SlotsAvailableWithAHospital(ID);
                        System.out.println("Choose Slot: ");
                        int slot = input.nextInt();
                        System.out.printf("%s vaccinated with %s\n", CitizenName, VaccineName);
                    }
                    if (o == 3) {
                        continue;
                    }
                }
                else
                    System.out.println("Citizen is not registered");
            }

            if(id==6){
                /*
                6. Slots available with a hospital

                Input: Hospital ID

                Output: List all slots for the chosen hospital
                 */
                System.out.print("Enter Hospital ID: ");
                int HospitalID = input.nextInt();
                s.SlotsAvailableWithAHospital(HospitalID);
            }

            if(id==7){
                /*
                7. Check vaccination status:

                Input: Citizen inputs his/her Unique ID to check current status

                Output: Display the current vaccination status: REGISTERED/
                PARTIALLY VACCINATED/FULLY VACCINATED along with the last date of vaccination,
                the due date of next vaccination (in case of partially vaccinated) and the vaccine administered.
                 */
                System.out.println("Enter Patient ID: ");
                long PatientID = input.nextLong();
                int [] vaccineDetails = CitizenIDSlotDict.get(PatientID);
                boolean VaccineTaken = c.VaccineTaken(PatientID,vaccineDetails[0],vaccineDetails[1],vaccineDetails[2]);
                vs.ViewVaccinationStatus(PatientID,VaccineTaken);
            }

            if (id == 8){
                /*
                8. Exit
                 */
                System.out.println("Thank you for visiting our website!");
                break;
            }
        }
    }
}
