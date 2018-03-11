package question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

class Node<T>{
	String key;
	T val;
	Node[] childs=new Node[TrieMap.size];

	public Node(String key,T val){
		this.key=key;
		this.val=val;

	}
	public Node(String key){
		this.key=key;
		val=null;
	}
}


public class TrieMap<T> extends TrieMapBase<T> {
	static int size;
	Node<T> root;
	int numOfNodes=1;
	static TrieMap<Object> tmap=new TrieMap<>(26);
	public TrieMap(int size){
		this.size=size;
		root=new Node<T>("");
	}

	/**
	 * Returns 	true if key appears in text as a substring;
	 * 			false, otherwise
	 * 
	 * Use Trie data structure to solve the problem
	 */
	public static boolean containsSubstr(String text, String key) {
		if(key.length()>text.length())
			return false;
		int keysize=key.length();
		while(keysize<=text.length()){
			tmap.insert(text.substring(0,keysize),"");
			text=text.substring(1);
		}
		if(tmap.search(key)!=null){
			tmap=new TrieMap<>(26);
			return true;
		}else{
			tmap=new TrieMap<>(26);
			return false;
		}

	}


	/**
	 * Returns how many times the word in the parameter appears in the book.
	 * Each word in book is separated by a white space. 
	 * 
	 * Use Trie data structure to solve the problem
	 */
	public static int wordCount(String book, String word) {	
		int counter=0;
		Scanner sc=new Scanner(book);
		while(sc.hasNext()){
			String a=sc.next();
			if(a.length()==word.length()&&tmap.containsSubstr(a, word))
				counter++;
		}
		return counter;
	}

	/**
	 * Returns the array of unique words in the book given as parameter.
	 * Each word in book is separated by a white space.
	 *  
	 * Use Trie data structure to solve the problem
	 */
	public static String[] uniqueWords(String book) {
		Scanner sc=new Scanner(book);
		LinkedList<String> l=new LinkedList<String>();
		while(sc.hasNext()){
			String a=sc.next();
			if(tmap.search(a)==null){
				l.add(a);
				tmap.insert(a, 7);

			}
		}
		String[] arr=new String[l.size()];
		for(int i=0;i<l.size();i++){
			arr[i]=l.get(i);
		}
		tmap=new TrieMap<>(26);
		return arr;
	}

	/**
	 * Recommends word completions based on the user history.
	 * 
	 * Among all the strings in the user history, the method takes 
	 * those that start with a given incomplete word S, 
	 * sort the words according to their frequencies (how many 
	 * times they are written), and recommend the 3 most frequently written ones.
	 * 
	 * @param userHistory 
	 * 			the words written previously by the user
	 * 
	 * @param incompleteWords 
	 * 			the list of strings to be autocompleted
	 * @return 
	 * 			a Sx3 array that contains the recommendations
	 * 			for each word to be autocompleted.
	 * 
	 * Use Trie data structure to solve the problem
	 */
	public static String[][] autoComplete(String[] userHistory, String[] incompleteWords){
		String[][] arr=new String[incompleteWords.length][3];
		String us="";
		for(int i=0;i<userHistory.length;i++){
			us+=" "+userHistory[i];
		}


		for(int i=0;i<incompleteWords.length;i++){
			int a2=0;
			String[] arr2;
			if(userHistory.length>=3)
				arr2=new String[userHistory.length];
			else
				arr2=new String[3];
			for(int j=0;j<userHistory.length;j++){			
				String in=incompleteWords[i];
				String key=userHistory[j];
				String a="";
				if(key.length()>=in.length())
					a=key.substring(0, in.length());
				if(TrieMap.containsSubstr(a, in)){
					boolean bool=false;
					for(int c=0;c<arr2.length;c++){
						if(arr2[c]!=null&&arr2[c].equals(key)){
							bool=true;
						}
					}
					if(!bool){
						arr2[a2]=key;
						a2++;
					}					
				}
			}

			for(int k=0; k < arr2.length; k++){  
				for(int j=1; j < (arr2.length-k); j++){ 
					if(arr2[j-1]!=null&&arr2[j]!=null&&TrieMap.wordCount(us,arr2[j-1])<TrieMap.wordCount(us,arr2[j])){  
						String  temp = arr2[j-1];  
						arr2[j-1] = arr2[j];  
						arr2[j] = temp; 
					}  
				}
			}

			for(int x=0;x<3;x++){
				arr[i][x]=arr2[x];
			}
		}
		return arr;
	}

	@Override
	public void insert(String key, T value) {
		// TODO Auto-generated method stub
		int keysize=key.length();
		String add="";
		Node cur=root;
		for(int i=0;i<keysize;i++){
			char c=key.charAt(0);
			add+=c;
			int a=c-97;
			if(a>size-1){
				return;
			}else{
				if(cur.childs[a]==null){
					cur.childs[a]=new Node(add);
					numOfNodes++;
				}
			}
			cur=cur.childs[a];
			if(keysize-i>1)
				key=key.substring(1);
		}
		cur.val=value;
	}

	@Override
	public boolean delete(String key) {
		// TODO Auto-generated method stub
		int keysize=key.length();
		Node cur=root;
		for(int i=0;i<keysize;i++){
			char c=key.charAt(0);
			int a=c-97;
			if(a>size-1)
				return false;
			else{
				if(cur.childs[a]!=null){
					cur=cur.childs[a];
				}else{
					return false;
				}
			}
			if(keysize-i>1)
				key=key.substring(1);
		}
		if(cur.val!=null){
			cur.val=null;
			return true;
		}else{
			return false;
		}
	}

	@Override
	public T search(String key) {
		// TODO Auto-generated method stub
		int keysize=key.length();
		Node cur=root;
		for(int i=0;i<keysize;i++){
			char c=key.charAt(0);
			int a=c-97;
			if(a>size-1)
				return null;
			else{
				if(cur.childs[a]!=null){
					cur=cur.childs[a];
				}else{
					return null;
				}
			}
			if(keysize-i>1)
				key=key.substring(1);
		}	
		return (T) cur.val;

	}

	@Override
	public int nodeCount() {
		// TODO Auto-generated method stub
		return numOfNodes;
	}

	@Override
	public ArrayList<T> returnBFSOrder() {
		// TODO Auto-generated method stub
		ArrayList<T> aList=new ArrayList<T>();
		Queue<Node<T>> q=new LinkedList<Node<T>>();
		q.add(root);
		while(!q.isEmpty()){
			Node<?> tempNode=(Node<?>) q.poll();
			if(tempNode.val!=null){
				aList.add((T) tempNode.val);
			}
			for(int i=0;i<size;i++){
				if(tempNode.childs[i]!=null){
					q.add(tempNode.childs[i]);
				}
			}
		}
		return aList;
	}
}
