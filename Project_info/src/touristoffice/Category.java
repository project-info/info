package touristoffice;

public enum Category {
	ONE("*"), TWO("**"), THREE("***"), FOUR("****"), FIVE("*****");
	
	private String out;
	
	private Category(String out) {
		this.out = out;
	}
	
	@Override
	public String toString() {
		return out;
	}
}
