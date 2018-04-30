package LZW;



public class BigIntegerNode {
	//create a node class
	
		private Integer data;
		private String key;
		private BigIntegerNode nextNode;
		/**
		 * default constructor for Node
		 */
	  public BigIntegerNode() {
	  	   data = 0;
	  	   nextNode = null;
	  	   key = null;
	  }
	  
	  // constructor
	  public BigIntegerNode(Integer data, String key, BigIntegerNode nextNode) {
	  	    this.data = data;
	  	    this.nextNode = nextNode;
	  	    this.key = key;
	  }
	  
	  // function to set data to current node
	  public void setData(Integer data) {
	  	     this.data = data;
	  }
	  //set key
	  public void setKey(String key) {
	  	     this.key = key;
	  }
	  
	  // function to set link to the next node
	  public void setnextNode (BigIntegerNode nextNode) {
	  	      this.nextNode = nextNode;
	  }
	  
	  // get the data in the current node
	  public Integer getData() {
	  	      return this.data;
	  }
	  
	  public String getKey() {
		  return this.key;
	  }
	  // get the link in the current node
	  public BigIntegerNode getnextNode() {
	  	      if (this.nextNode == null) {
	  	    	      return null;
	  	      }
	  	    	  return this.nextNode;
	  	 }
	  
	  public String toString() {
	  	      return this.data.toString();
	  
	 }
	  
	  
	  
	  
	  
	  
	  
	  
	

}
