package LZW;

import java.math.BigInteger;
import java.util.LinkedList;

public class SinglyBigLinkedList {
	
	/**
	 * create a singly linked list 
	 * @author mingyiliang
	 *
	 */

		private BigIntegerNode head;
		private BigIntegerNode tail;
		public int size;
		
		// Default constructor for BigIntegerLinkedList
		 
		public SinglyBigLinkedList() {
			head = null;
			tail = null;
			size = 0;
		}
		
		/**
		 * function to check if this singly linked list is empty
		 * @param args
		 */
		public boolean isEmpty() {
			if(this.head == null) {
				return true;
			}else {
				return false;
			}
		}
	     
		/**
		 * add a new nod to the end of a list
		 */
		
		
	    
		public void addNodeEnd(Integer newVal, String key) {
			BigIntegerNode newNode = new BigIntegerNode(newVal, key, null);
			//System.out.println(newVal);
			if (head == null) {
				head = newNode;
				tail = head;
			}else {
				tail.setnextNode(newNode);
				tail = newNode;
			}
			
			
			
			
		}
		
		/**
		 * add a new nod to the begining of a list
		 * @param args
		 */
		public void addNodeBegin(Integer newVal, String key) {
			BigIntegerNode newNode = new BigIntegerNode(newVal,key, null);
			if (head == null) {
				head = newNode;
				tail = head;
			}else {
				newNode.setnextNode(head);
				head = newNode;
			}
		}
		
		/**
		 * function to count the number of nodes in the list
		 * @param args
		 */
		
		public int getSize() {
			int count = 1;
	        if (this.head == null) {
	            return 0;
	        }
	        BigIntegerNode iterator = this.head;
	        while ((iterator = iterator.getnextNode()) != null) {
	            count++;
	        }
	        return count;
		}
		
		/**
		 * delete nodes from the linked list
		 * @param args
		 */
		public boolean deleteNode(BigInteger newVal) {
			if (this.head == null) {
				return false;
			}
			BigIntegerNode iterator = this.head;
			BigIntegerNode prev = null;
			do {
	            if (iterator.getData().equals(newVal)) {
	                if (prev == null) { // If the node is head, we shift the head reference
	                    this.head = iterator.getnextNode();
	                } else {
	                    prev.setnextNode(iterator.getnextNode());
	                }
	                return true;
	            }
	            prev = iterator;
	        } while ((iterator = iterator.getnextNode()) != null);

	        return false;
		}
		/**
		 * function to return the node in given position
		 * @param args
		 */
		public BigIntegerNode get(int position) {
	        if (head == null) {
	            return null;
	        }

	        BigIntegerNode cursor = head;
	        for (int i = 0; i < position; i++) {
	            cursor = cursor.getnextNode();
	            if (cursor == null) {
	                return null;
	            }
	        }
	        return cursor;
	    

      }
 }