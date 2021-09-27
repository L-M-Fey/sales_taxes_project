package sales_taxes_project;

import java.util.ArrayList;

public class Product {
	private int count;
	private String name;
	private double rawPrice;
	private double taxedPrice;
	private boolean noTax;
	private boolean imported;
	
	public Product(int count, String name, double rawPrice) {
		this.count = count;
		this.name = name;
		this.rawPrice = rawPrice;
		this.imported = isImported(name); // check if the product is considered "imported"
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRawPrice() {
		return rawPrice;
	}

	public void setRawPrice(double rawPrice) {
		this.rawPrice = rawPrice;
	}
	
	public double getTaxedPrice() {
		return taxedPrice;
	}

	public void setTaxedPrice(double taxedPrice) {
		this.taxedPrice = taxedPrice;
	}

	public boolean getNoTaxVal() {
		return noTax;
	}
	
	public boolean getImportedVal() {
		return imported;
	}
	
	public void hasNoTax(String[] checkForNoTax) {
		hasNoTax(getName(), checkForNoTax);
	}
	
	public void hasNoTax(String name, String[] checkForNoTax) {
		boolean hasNoTax = false;
		int checkCount = checkForNoTax.length;
		int index = 0;
		// check if product item belongs to group of items that ignores basic sales tax
		while (index < checkCount && !hasNoTax) {
			if (name.contains(checkForNoTax[index])) {
				hasNoTax = true; // it does, set the variable accordingly
			} else {
				index++;
			}
		}
		this.noTax = hasNoTax;
	}
	
	public void hasNoTax(ArrayList<String> checkForNoTax) {
		hasNoTax(getName(), checkForNoTax);
	}
	
	public void hasNoTax(String name, ArrayList<String> checkForNoTax) {
		boolean hasNoTax = false;
		int checkCount = checkForNoTax.size();
		int index = 0;
		// check if product item belongs to group of items that ignores basic sales tax
		while (index < checkCount && !hasNoTax) {
			if (name.contains(checkForNoTax.get(index))) {
				hasNoTax = true; // it does, set the variable accordingly
			} else {
				index++;
			}
		}
		this.noTax = hasNoTax;
	}
	
	public boolean isImported() {
		return isImported(getName());
	}

	public boolean isImported(String name) {
		boolean isImported = false;
		// check if the product name has the keyword "import" included in any way
		if (name.contains("import")) {
			isImported = true; // if it does, the product is considered "imported" (import sales tax gets added)
		}
		return isImported;
	}
}
