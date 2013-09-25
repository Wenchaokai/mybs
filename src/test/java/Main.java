import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * ClassName:Main Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-9-4
 */
public class Main {
	public static void main(String[] args) {
		TreeMap<Double, String> tt = new TreeMap<Double, String>();
		tt.put(Double.parseDouble("100"), "100");
		tt.put(Double.parseDouble("101"), "101");
		tt.put(Double.parseDouble("99"), "99");
		tt.put(Double.parseDouble("102"), "102");
		for (Entry<Double, String> entry : tt.entrySet()) {
			System.out.print(entry.getValue());
		}
	}
}
