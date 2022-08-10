import java.util.*;
import java.io.*;

public class ScoreManager{
	
	private ArrayList<ScoreRecord> scoreRecords;
	private void merge(  ScoreRecord[] a, ScoreRecord [] l, ScoreRecord[] r, int left, int right) {
		int i = 0, j = 0, k = 0;
		System.out.print("[");
		while (i < left && j < right) {
			if (l[i].getScore() <= r[j].getScore()) {
				a[k++] = l[i++];
			}
			else {
				a[k++] = r[j++];
			}
			System.out.print(a[k] + ",");
		}
		while (i < left) {
			a[k++] = l[i++];
			System.out.print(a[k] + ",");
		}
		while (j < right) {
			a[k++] = r[j++];
			System.out.print(a[k] + ",");
		}
		System.out.println();
	}
	
	/**
	 * merge sort algorithm to sort all the records 
	 * @param a Parameter a
	 * @param n integer n  
	 */
	private void mergeSort(ScoreRecord [] a, int n){
		if (n < 2) {
			return;
		}
		int mid = n / 2;
		ScoreRecord[] l = new ScoreRecord[mid];
		ScoreRecord[] r = new ScoreRecord[n - mid];
	 
		for (int i = 0; i < mid; i++) {
			l[i] = a[i];
		}
		for (int i = mid; i < n; i++) {
			r[i - mid] = a[i];
		}
		mergeSort(l, mid);
		mergeSort(r, n - mid);
		merge(a, l, r, mid, n - mid);
	}

	public ScoreManager(){
		scoreRecords = new ArrayList<ScoreRecord> ();
	}
	
	/**
	 * read records, objects
	 * @param pathName 
	 * @return return information
	 * @throws IOException 
	 */
	public ArrayList<ScoreRecord> readRecords(String pathName) throws IOException{
		FileInputStream fileStream = new FileInputStream(pathName);
		/**
		 * Creates an ObjectInputStream
		 */
		ObjectInputStream input = new ObjectInputStream(fileStream);
		System.out.println(input.available());
		
		try{
			ScoreRecord record = (ScoreRecord)input.readObject(); 
			while(record != null && input.available() ==0){
				System.out.println(record);
				scoreRecords.add(record);
				record = (ScoreRecord)input.readObject(); 
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		input.close();
		return scoreRecords;
	}
	
	/**
	 * 
	 * @param inputList
	 * @return 
	 */
	public ArrayList<ScoreRecord> mergeSortRecords(ArrayList<ScoreRecord> inputList){
		ScoreRecord [] a = new ScoreRecord[inputList.size()];
		int i = 0;
		for(ScoreRecord record : inputList){
			a[i] = record;
			i++;
		}
		int n = inputList.size();
		mergeSort(a,n);
		return new ArrayList<ScoreRecord>(Arrays.asList(a));
	}
	
	/**
	 * Creates an ObjectOutputStream and writes objects to the output stream
	 * @param pathName
	 * @param sortedList
	 * @throws IOException
	 */
	public void saveScores(String pathName, ArrayList<ScoreRecord> sortedList) throws IOException{
		FileOutputStream file = new FileOutputStream(pathName);
		ObjectOutputStream output = new ObjectOutputStream(file);
		for(ScoreRecord record : sortedList)
			output.writeObject(record);
		output.close();
	}
	
	/**
	 * 
	 * @param pathName
	 * @param minScore
	 * @param maxScore
	 * @return
	 * @throws IOException
	 */
	public ArrayList<ScoreRecord> scoreSearch(String pathName, int minScore, int maxScore) throws IOException{
		ArrayList<ScoreRecord> records = new ArrayList<ScoreRecord>();
		RandomAccessFile file = new RandomAccessFile(pathName, "r");  
		
		for(int i = 0; i < file.length(); i += 50){
			file.seek(i);  
			byte[] bytes = new byte[50];  
			file.read(bytes);  
			try{
				if (Objects.nonNull(bytes)) {
					ByteArrayInputStream bis =  new ByteArrayInputStream(bytes);
					ObjectInput in = new ObjectInputStream(bis);
					ScoreRecord record = (ScoreRecord)in.readObject();
					System.out.println("visit: " + record);
					if(record.getScore() >= minScore && record.getScore() <= maxScore){
						records.add(record);
					}
				}
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}
        file.close();  
		return records;
	}
}