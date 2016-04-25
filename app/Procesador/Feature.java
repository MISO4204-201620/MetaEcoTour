package Procesador;

import java.util.List;

public class Feature {
	private String name;
	private boolean present;
	private List<Feature> childs;

	public Feature(String name){
		this.name = name;
		this.present = false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public List<Feature> getChilds() {
		return childs;
	}

	public void setChilds(List<Feature> childs) {
		this.childs = childs;
	}

}
