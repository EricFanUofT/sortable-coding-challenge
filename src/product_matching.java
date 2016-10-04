import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


public class product_matching {
	//A new class to store the product information from input file "products.txt"
	public static class Product{
		String product_name;
		String manufacturer;
		String family;
		String model;
		String announced_date;
		public Product(){
			this.product_name=null;
			this.manufacturer=null;
			this.family=null;
			this.model=null;
			this.announced_date=null;		
		}
	}
	//A new class to store the listing information from input file "listings.txt"
	public static class Listing{
		String listing;
		String title;
		String manufacturer;
		String currency;
		String price;
		public Listing(){
			this.listing=null;
			this.title=null;
			this.manufacturer=null;
			this.currency=null;
			this.price=null;
		}
	}
	//A method to parse the JSON objects from the input file "products.txt" 
	//and store the result in a LinkedList of Product objects
	public static LinkedList<Product> parse_products(FileReader fr) throws IOException{
		BufferedReader br=new BufferedReader(fr);
		LinkedList<Product> products_list=new LinkedList<Product>();
		String line=null;
		
		//store the keys of the JSON objects from the input file "products.txt"
		String[] keys={"{\"product_name\":\"","\",\"manufacturer\":\"","\",\"model\":\"","\",\"family\":\"","\",\"announced-date\":\"","\"}"};
		int[] key_length=new int[keys.length];
		for(int i=0;i<keys.length;i++){
			key_length[i]=keys[i].length();
		}
		
		int index_start, index_end;
		//parse the information from the JSON object into a Product object 
		//and store them in the linkedlist "product_list"
		while((line=br.readLine())!=null){
			Product p= new Product();
			//extract product name from each JSON object
			index_start=line.indexOf(keys[0])+key_length[0];
			index_end=line.indexOf(keys[1]);
			p.product_name=line.substring(index_start,index_end);
			
			//extract the manufacturer
			index_start=line.indexOf(keys[1])+key_length[1];
			index_end=line.indexOf(keys[2]);
			p.manufacturer=line.substring(index_start,index_end);
			
			//extract the model
			index_start=line.indexOf(keys[2])+key_length[2];
			index_end=line.indexOf(keys[3]);
			//if the "family" field is missing, the end is indexed by the next field which is "announced_date"
			if(index_end==-1){
				index_end=line.indexOf(keys[4]);
			}
			p.model=line.substring(index_start,index_end);
			
			//extract the family field(if exists)
			index_start=line.indexOf(keys[3])+key_length[3];
			index_end=line.indexOf(keys[4]);
			if(index_start!=-1+key_length[3]){
				p.family=line.substring(index_start,index_end);
			}
			
			//extract the announced_date
			index_start=line.indexOf(keys[4])+key_length[4];
			index_end=line.indexOf(keys[5]);
			p.announced_date=line.substring(index_start,index_end);
			
			products_list.add(p);
		}
		br.close();
		return products_list;
	}
	
	//A method to parse the JSON object from the input file "listings.txt" 
	//and store the result in a LinkedList of Listing objects
	public static LinkedList<Listing> parse_listings(FileReader fr) throws IOException{
		BufferedReader br=new BufferedReader(fr);
		LinkedList<Listing> listings_list=new LinkedList<Listing>();
		String line=null;
		
		//store the keys of the JSON objects from the input file "listings.txt"
		String[] keys={"{\"title\":\"","\",\"manufacturer\":\"","\",\"currency\":\"","\",\"price\":\"","\"}"};
		int[] key_length=new int[keys.length];
		for(int i=0;i<keys.length;i++){
			key_length[i]=keys[i].length();
		}
		
		int index_start, index_end;
		//parse the information from the JSON object into a Listing object
		//and store them in the linkedlist "listings_list"
		while((line=br.readLine())!=null){
			Listing l= new Listing();
			//store the entire listing object as a string
			l.listing=line;
						
			//extract title from each JSON object
			index_start=line.indexOf(keys[0])+key_length[0];
			index_end=line.indexOf(keys[1]);
			l.title=line.substring(index_start,index_end);
			
			//extract manufacturer
			index_start=line.indexOf(keys[1])+key_length[1];
			index_end=line.indexOf(keys[2]);
			l.manufacturer=line.substring(index_start,index_end);
			
			//extract the currency
			index_start=line.indexOf(keys[2])+key_length[2];
			index_end=line.indexOf(keys[3]);
			l.currency=line.substring(index_start,index_end);
			
			//extract the price
			index_start=line.indexOf(keys[3])+key_length[3];
			index_end=line.indexOf(keys[4]);
			l.price=line.substring(index_start,index_end);
			
			listings_list.add(l);
		}
		br.close();
		return listings_list;
	}
	
	//A method to match products with product listings by searching whether a listing title contains
	//the product family and model and also by matching the manufacturers
	public static void matching(FileWriter fw, LinkedList<Product> products_list, LinkedList<Listing> listings_list){
		PrintWriter print_line=new PrintWriter(fw);
		int ctr=0;
		//iterate through all the Product objects in the products_list
		for(Product p: products_list){
			String manufacturer=p.manufacturer;
			String family=p.family;
			String model=p.model;
			ArrayList<String> matched_listings=new ArrayList<String>();
			Iterator<Listing> i=listings_list.iterator();
			//iterate through all the remaining Listing objects to find ones that match the Product object
			while(i.hasNext()){
				Listing l=i.next();
				String listing=l.listing;
				String title=l.title;
				String listing_manufacturer=l.manufacturer;
				//ensure the manufacturers are the same
				if((listing_manufacturer.toLowerCase()).equals(manufacturer.toLowerCase())){
					//ensure the title includes the model number
					if((title.toLowerCase()).contains(" "+model.toLowerCase()+" ")){
						//if a family grouping is provided, ensure the title also includes it
						if(family!=null && !(title.toLowerCase()).contains(family.toLowerCase())){
							continue;
						}
						//ensure the model appears before words such as accessory 
						//(so that the product description matches the product itself rather than the accessory)
						int index=Math.max(title.toLowerCase().indexOf("accessory"),title.toLowerCase().indexOf("accessories"));
						if(index!=-1 && index < title.toLowerCase().indexOf(model.toLowerCase())){
							continue;
						}
		
						matched_listings.add(listing);
						//ensure one price listing at most matches one product
						i.remove();
					}
				}
			}
			//output the Result object to results.txt
			print_line.printf("{\"product_name\": \""+p.product_name+"\",\"listings\": " + Arrays.toString(matched_listings.toArray())+"}");
			print_line.println();
		}
		print_line.close();
	} 
	

	public static void main(String[] args) throws FileNotFoundException, IOException {
		//open and parse the input file "products.txt"
		FileReader fr=new FileReader("../input/products.txt");
		LinkedList<Product> products_list=parse_products(fr);
		//open and parse the input file "listings.txt"			
		fr=new FileReader("../input/listings.txt");
		LinkedList<Listing> listings_list=parse_listings(fr);
		
		//write to the output file results.txt
		FileWriter fw=new FileWriter("../output/results.txt");
		matching(fw, products_list, listings_list);
		
	}

}
