/*
  author @sudhiramara
*/


    /*
    ReviewVO holds the productID, name of the product, reviewcount and average of the ratings 
    */

public class ReviewVO {
	int productID;
	String name;
	int revCount;
	double average;

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRevCount() {
		return revCount;
	}

	public void setRevCount(int revCount) {
		this.revCount = revCount;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}
}
